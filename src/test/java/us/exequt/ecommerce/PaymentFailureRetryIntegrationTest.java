package us.exequt.ecommerce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import us.exequt.ecommerce.cart.dto.AddCartItemRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PaymentFailureRetryIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    private String orderId;

    @BeforeEach
    void setupCartAndOrder() throws Exception {
        // create cart
        String cartResult = mockMvc.perform(post("/carts"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // get cart id
        String cartId = JsonPath.read(cartResult, "$.data.id");

        // add items to the cart with a total of 50
        mockMvc.perform(post("/carts/{cartId}/items", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AddCartItemRequest.builder()
                                .productId(UUID.randomUUID())
                                .quantity(2)
                                .price(new BigDecimal("25.00"))
                                .build())))
                .andExpect(status().isOk());

        // checkout the cart and verify it returns 200
        mockMvc.perform(post("/carts/{cartId}/checkout", cartId))
                .andExpect(status().isOk());

        // get all orders to find the order id
        String ordersResult = mockMvc.perform(get("/orders"))
                .andReturn().getResponse().getContentAsString();
        orderId = JsonPath.read(ordersResult, "$.data[0].id");
    }

    @Test
    void paymentFailureThenRetryOrderEndsAsPaid() throws Exception {

        // start payment for the order and verify it returns 200 and order is in PENDING_PAYMENT
        mockMvc.perform(post("/orders/{orderId}/payment/start", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING_PAYMENT"));

        // get all payment attempts to find the attempt id
        String paymentsResult = mockMvc.perform(get("/payments"))
                .andReturn().getResponse().getContentAsString();

        // get the attempt id
        String firstAttemptId = JsonPath.read(paymentsResult, "$.data[0].id");

        // use the mock to reject the payment attempt
        mockMvc.perform(post("/mock-payments/{attemptId}/fail", firstAttemptId))
                .andExpect(status().isOk());

        // verify order is now in PAYMENT_FAILED
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PAYMENT_FAILED"));

        // retry payment for the same order, status should go back to PENDING_PAYMENT
        mockMvc.perform(post("/orders/{orderId}/payment/start", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING_PAYMENT"));

        // verify we have two payment attempts now, one FAILED and one PENDING
        String paymentsAfterRetry = mockMvc.perform(get("/payments"))
                .andExpect(jsonPath("$.data.length()").value(2))
                .andReturn().getResponse().getContentAsString();

        // get the id of the second attempt which should be in PENDING status
        List<String> pendingIds = JsonPath.read(paymentsAfterRetry, "$.data[?(@.paymentStatus == 'PENDING')].id");
        String secondAttemptId = pendingIds.get(0);

        // confirm the second attempt
        mockMvc.perform(post("/mock-payments/{attemptId}/confirm", secondAttemptId))
                .andExpect(status().isOk());

        // get the order by id and verify the order status is PAID and the total price is correct
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PAID"))
                .andExpect(jsonPath("$.data.totalPrice").value(50.00));

        // verify we have two attempts, one FAILED and one SUCCESS, no state corruption
        mockMvc.perform(get("/payments"))
                .andExpect(jsonPath("$.data[?(@.paymentStatus == 'FAILED')]", hasSize(1)))
                .andExpect(jsonPath("$.data[?(@.paymentStatus == 'SUCCESS')]", hasSize(1)));
    }

    @Test
    void cannotStartPaymentWhenAttemptAlreadyPending() throws Exception {

        // start payment, and verify it creates a PENDING_PAYMENT attempt
        mockMvc.perform(post("/orders/{orderId}/payment/start", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING_PAYMENT"));

        // second start while first is still PENDING and verify it is rejected with 4xx
        mockMvc.perform(post("/orders/{orderId}/payment/start", orderId))
                .andExpect(status().is4xxClientError());
    }
}
