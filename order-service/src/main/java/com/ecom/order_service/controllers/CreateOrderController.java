package com.ecom.order_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.order_service.dto.Order;
import com.ecom.order_service.services.CreateOrderService;

@RestController
@RequestMapping("/orders")
public class CreateOrderController {

	private final CreateOrderService orderService;

	public CreateOrderController(CreateOrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> createOrder(@RequestBody Order order) {
		Order savedOrder = orderService.createOrder(order);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Order> getOrder(@PathVariable Long id) {
		Order order = orderService.getOrderById(id);
		return ResponseEntity.ok(order);
	}
	
}
