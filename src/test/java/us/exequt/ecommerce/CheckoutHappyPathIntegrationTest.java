package us.exequt.ecommerce;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import us.exequt.ecommerce.cart.dto.AddCartItemRequest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CheckoutHappyPathIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Test
    void happyPathFromCartCreationToOrderPaid() throws Exception {

        // create the cart
        MvcResult cartResult = mockMvc.perform(post("/carts"))
                .andExpect(status().isCreated())
                .andReturn();

        // get the id of the created cart
        String cartId = JsonPath.read(cartResult.getResponse().getContentAsString(), "$.data.id");

        // add two items with a total price of 50 and verify it
        mockMvc.perform(post("/carts/{cartId}/items", cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AddCartItemRequest.builder()
                                .productId(UUID.randomUUID())
                                .quantity(2)
                                .price(new BigDecimal("25.00"))
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalPrice").value(50.00));

        // checkout the cart and verify the cart got locked and it's status is now inactive
        mockMvc.perform(post("/carts/{cartId}/checkout", cartId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("INACTIVE"));

        // get all the orders to find out the order id and verify the order is in CREATED status and has the correct cart id
        MvcResult ordersResult = mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].status").value("CREATED"))
                .andExpect(jsonPath("$.data[0].cartId").value(cartId))
                .andReturn();

        // get the order id
        String orderId = JsonPath.read(ordersResult.getResponse().getContentAsString(), "$.data[0].id");

        // start payment flow for the order and verify the order status is now PENDING_PAYMENT
        mockMvc.perform(post("/orders/{orderId}/payment/start", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PENDING_PAYMENT"));

        // get all payment attempts to find out the attempt id and verify it's linked to the correct order id
        MvcResult paymentsResult = mockMvc.perform(get("/payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].orderId").value(orderId))
                .andReturn();

        // get the payment attempt id
        String attemptId = JsonPath.read(paymentsResult.getResponse().getContentAsString(), "$.data[0].id");

        // confirm the payment attempt and verify the order status is now PAID and the total price is correct
        mockMvc.perform(post("/mock-payments/{attemptId}/confirm", attemptId))
                .andExpect(status().isOk());

        // get the order by id and verify the order status is PAID and the total price is correct
        mockMvc.perform(get("/orders/{orderId}", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("PAID"))
                .andExpect(jsonPath("$.data.totalPrice").value(50.00));
    }
}
