package com.example.tddminiproject.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

    @BeforeEach
    public void setup() {
        order = new Order("John Doe", LocalDate.now(), "123 Main St", 100.0);
    }

    @Test
    public void testGetAllOrders() throws Exception {
        Mockito.when(orderRepository.findAll()).thenReturn(Arrays.asList(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerName").value("John Doe"));
    }

    @Test
    public void testCreateOrder() throws Exception {
        String orderJson = "{ \"customerName\": \"John Doe\", \"orderDate\": \"2023-01-01\", \"shippingAddress\": \"123 Main St\", \"total\": 100.0 }";

        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(orderJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    public void testGetOrderById() throws Exception {
        Long orderId = 1L;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        mockMvc.perform(MockMvcRequestBuilders.get("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("John Doe"));
    }

    @Test
    public void testUpdateOrder() throws Exception {
        Long orderId = 1L;
        String updatedOrderJson = "{ \"customerName\": \"Jane Smith\", \"orderDate\": \"2023-01-02\", \"shippingAddress\": \"456 Elm St\", \"total\": 200.0 }";

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(orderRepository.save(Mockito.any(Order.class))).thenReturn(order);

        mockMvc.perform(MockMvcRequestBuilders.put("/orders/{id}", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedOrderJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("Jane Smith"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total").value(200.0));
    }
}

