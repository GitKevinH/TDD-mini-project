package com.example.tddminiproject.repositories;

import com.example.tddminiproject.models.Order;
import com.example.tddminiproject.repositories.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Optional;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private OrderRepository orderRepository;

    //Testing for saving orders. The order is saved and then checked by checking if there is an ID after saving.
    @Test
    public void testSaveOrder() {
        Order order = new Order("Smith Smithenson", LocalDate.now(), "313 Detroit Ave", 100.0);

        Order savedOrder = orderRepository.save(order);
        Assertions.assertNotNull(savedOrder.getId());
    }

    //Testing for getting orders by ID, first the order is created, then the findByID method is run and saved into the variable to ensure it is not null and that the foundOrder and original order have the same name.
    @Test
    public void testFindOrderById() {
        Order order = new Order("Smith Smithenson", LocalDate.now(), "313 Detroit Ave", 100.0);
        orderRepository.save(order);

        Order foundOrder = orderRepository.findById(order.getId()).orElse(null);
        Assertions.assertNotNull(foundOrder);
        Assertions.assertEquals(order.getCustomerName(), foundOrder.getCustomerName());
    }

    //Testing for update order method, first it creates an order amd then updates the order with new details. Then the order is checked to be updated by assertion.
    @Test
    public void testUpdateOrder() {
        Order order = new Order("Smith Smithenson", LocalDate.now(), "313 Detroit Ave", 100.0);
        orderRepository.save(order);

        // Modify the order
        order.setCustomerName("John Johnson");
        order.setTotal(200.0);

        Order updatedOrder = orderRepository.save(order);
        Assertions.assertEquals("John Johnson", updatedOrder.getCustomerName());
        Assertions.assertEquals(200.0, updatedOrder.getTotal());
    }

    //Testing for delete order method, first it creates an order and then deletes the order and checks to see if the order is still existent by assertion.
    @Test
    public void testDeleteOrder() {
        Order order = new Order("Smith Smithenson", LocalDate.now(), "313 Detroit Ave", 100.0);
        orderRepository.save(order);

        orderRepository.delete(order);

        Optional<Order> optionalOrder = orderRepository.findById(order.getId());
        Assertions.assertFalse(optionalOrder.isPresent());
    }


}

