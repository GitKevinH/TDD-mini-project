package com.example.tddminiproject.controllers;

import com.example.tddminiproject.models.Order;
import com.example.tddminiproject.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    // GET /orders
    @GetMapping
    public List<Order> getAllOrders() {
        // Implement logic to retrieve and return all orders from the repository
        return orderRepository.findAll();
    }

    // POST /orders
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        // Implement logic to create a new order and save it to the repository
        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    // GET /orders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        // Implement logic to retrieve an order by its ID from the repository
        return orderRepository.findById(id)
                .map(order -> ResponseEntity.ok().body(order))
                .orElse(ResponseEntity.notFound().build());
    }

    // PUT /orders/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order updatedOrder) {
        // Implement logic to update an existing order by its ID
        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setCustomerName(updatedOrder.getCustomerName());
                    existingOrder.setOrderDate(updatedOrder.getOrderDate());
                    existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
                    existingOrder.setTotal(updatedOrder.getTotal());
                    Order savedOrder = orderRepository.save(existingOrder);
                    return ResponseEntity.ok().body(savedOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {

        Optional<Order> optionalOrder = orderRepository.findById(id);
        if (optionalOrder.isPresent()) {
            Order existingOrder = optionalOrder.get();
            orderRepository.delete(existingOrder);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

