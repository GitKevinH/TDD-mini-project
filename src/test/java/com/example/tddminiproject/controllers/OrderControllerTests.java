package com.example.tddminiproject.controllers;

import com.example.tddminiproject.models.Order;
import com.example.tddminiproject.repositories.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@WebMvcTest(OrderController.class)
public class OrderControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    private Order order;

    //Object created before each test
    @BeforeEach
    public void setup() {
        order = new Order("Smithy Smithenson", LocalDate.now(), "313 Detroit Ave", 100.0);
    }

    //Test to ensure get order mapping works with iteration
    @Test
    public void testGetAllOrders() throws Exception {
        Mockito.when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName").value("Smithy Smithenson"));
    }

    //Test to ensure create order mapping works
    @Test
    public void testCreateOrder() throws Exception {
        String orderJson = "{ \"customerName\": \"Smithy Smithenson\", \"orderDate\": \"2023-01-01\", \"shippingAddress\": \"313 Detroit Ave\", \"total\": 100.0 }";

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Smithy Smithenson"));
    }

    //Test to ensure specific get order mapping works
    @Test
    public void testGetOrderById() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Smithy Smithenson"));
    }

    //Test to ensure update order mapping works
    @Test
    public void testUpdateOrder() throws Exception {
        Long orderId = 1L;
        String updatedOrderJson = "{ \"customerName\": \"John Johnson\", \"orderDate\": \"2023-01-02\", \"shippingAddress\": \"704 Charlotte Drive\", \"total\": 200.0 }";

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedOrderJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("John Johnson"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200.0));
    }

    //Test to ensure delete order mapping works
    @Test
    public void testDeleteOrder() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.delete("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    //Validation and error testing for a missing customername after implementing validation on controller and model
    @Test
    public void testCreateOrderWithMissingCustomerNameReturnsBadRequest() throws Exception {
        String orderJson = "{\"orderDate\":\"2023-01-01\",\"shippingAddress\":\"123 Main St\",\"total\":100.0}";

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Validation and error testing for a negative total after implementing validation on controller and model
    @Test
    public void testCreateOrderWithNegativeTotalReturnsBadRequest() throws Exception {
        Long orderId = 1L;
        String orderJson = "{\"customerName\":\"John Doe\",\"orderDate\":\"2023-01-01\",\"shippingAddress\":\"123 Main St\",\"total\":-100.0}";

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    //Validation and error testing for  updating an order with no customer name after implementing validation on controller and model
    @Test
    public void testUpdateOrderWithEmptyCustomerNameReturnsBadRequest() throws Exception {
        Long orderId = 1L;
        String updatedOrderJson = "{\"customerName\":\"\",\"orderDate\":\"2023-01-02\",\"shippingAddress\":\"456 Elm St\",\"total\":200.0}";

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(new Order()));
        mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedOrderJson))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}
