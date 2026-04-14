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
import us.exequt.ecommerce.payment.domain.PaymentStatus;
import us.exequt.ecommerce.payment.dto.PaymentAttemptResultRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class DuplicateWebhookIntegrationTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    private String orderId;
    private String attemptId;

    @BeforeEach
    void setupUpToPaymentPending() throws Exception {
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

        // checkout cart
        mockMvc.perform(post("/carts/{cartId}/checkout", cartId))
                .andExpect(status().isOk());

        // get order id
        String ordersResult = mockMvc.perform(get("/orders"))
                .andReturn().getResponse().getContentAsString();
        orderId = JsonPath.read(ordersResult, "$.data[0].id");

        // start payment for the order, which creates a payment attempt in PENDING_PAYMENT state
        mockMvc.perform(post("/orders/{orderId}/payment/start", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING_PAYMENT"));

        // get attempt id
        String attemptsResult = mockMvc.perform(get("/payments"))
                .andReturn().getResponse().getContentAsString();
        attemptId = JsonPath.read(attemptsResult, "$.data[0].id");
    }

    @Test
    void duplicateConfirmWebhookIsRejectedOrderRemainsCorrect() throws Exception {
        PaymentAttemptResultRequest webhook = PaymentAttemptResultRequest.builder()
                .attemptId(UUID.fromString(attemptId))
                .result(PaymentStatus.SUCCESS)
                .build();

        // first webhook, accepted, order becomes PAID
        mockMvc.perform(post("/payments/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhook)))
                .andExpect(status().isOk());

        // verify order is PAID
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(jsonPath("$.data.status").value("PAID"));

        // second webhook, rejected as duplicate
        mockMvc.perform(post("/payments/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // order is still PAID
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(jsonPath("$.data.status").value("PAID"));

        // exactly one attempt exists, in SUCCESS state
        mockMvc.perform(get("/payments"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].paymentStatus").value("SUCCESS"));
    }

    @Test
    void duplicateFailedWebhookIsRejectedOrderRemainsCorrect() throws Exception {
        PaymentAttemptResultRequest webhook = PaymentAttemptResultRequest.builder()
                .attemptId(UUID.fromString(attemptId))
                .result(PaymentStatus.FAILED)
                .build();

        // first webhook, accepted, order becomes PAYMENT_FAILED
        mockMvc.perform(post("/payments/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhook)))
                .andExpect(status().isOk());

        // verify order is PAYMENT_FAILED
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(jsonPath("$.data.status").value("PAYMENT_FAILED"));

        // // second webhook, rejected as duplicate
        mockMvc.perform(post("/payments/webhook")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(webhook)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));

        // order is still  PAYMENT_FAILED
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(jsonPath("$.data.status").value("PAYMENT_FAILED"));

        // exactly one attempt exists, in FAILED state
        mockMvc.perform(get("/payments"))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].paymentStatus").value("FAILED"));
    }
}
