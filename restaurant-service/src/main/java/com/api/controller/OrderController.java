package com.api.controller;

import com.api.dto.OrderRequestDTO;
import com.api.model.Order;
import com.api.service.OrderService;
import com.api.util.EStatus;
import com.api.dto.Response;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/restaurant")
public class OrderController {

    OrderService orderService;

    @Autowired
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/get-order/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer orderId) {
        Optional<Order> order = orderService.findById(orderId);
        if (order.isEmpty()) {
            return new Response(EStatus.ERROR, "No order with such id").wrap();
        }
        return ResponseEntity.ok(order.get());
    }

    @PostMapping("/make-order")
    public ResponseEntity<?> makeOrder(@Valid @RequestBody OrderRequestDTO order) {
        return orderService.makeOrder(order).wrap();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors().stream().map(fe -> fe.getField() + ": " + fe.getDefaultMessage()).toList();
    }

}
