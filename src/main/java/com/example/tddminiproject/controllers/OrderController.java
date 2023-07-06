package com.example.tddminiproject.controllers;

import com.example.tddminiproject.models.Order;
import com.example.tddminiproject.repositories.OrderRepository;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            DefaultMessageCodesResolver resolver = new DefaultMessageCodesResolver();
            for (ObjectError error : bindingResult.getAllErrors()) {
                String[] codes = resolver.resolveMessageCodes(error.getCode(), error.getObjectName(), error.getDefaultMessage(), String.class);
                for (String code : codes) {
                    errors.add(code);
                }
            }
            return ResponseEntity.badRequest().body(errors);
        }

        Order savedOrder = orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @Valid @RequestBody Order updatedOrder, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        return orderRepository.findById(id)
                .map(existingOrder -> {
                    existingOrder.setCustomerName(updatedOrder.getCustomerName());
                    existingOrder.setOrderDate(updatedOrder.getOrderDate());
                    existingOrder.setShippingAddress(updatedOrder.getShippingAddress());
                    existingOrder.setTotal(updatedOrder.getTotal());
                    orderRepository.save(existingOrder);
                    return ResponseEntity.ok(existingOrder);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        // Implement logic to delete an order by its ID
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
