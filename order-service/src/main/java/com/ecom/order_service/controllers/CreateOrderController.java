package com.ecom.order_service.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.order_service.dto.CreateOrderRequest;
import com.ecom.order_service.dto.Order;
import com.ecom.order_service.dto.OrderResponse;
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

	  
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(
                orderService.getOrder(orderId)
        );
    }


    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long orderId,
            @RequestBody CreateOrderRequest request) {

        return ResponseEntity.ok(
                orderService.updateOrder(
                        orderId,
                        request
                )
        );
    }


 
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(
            @PathVariable Long orderId) {

        orderService.deleteOrder(orderId);

        return ResponseEntity.noContent().build();
    }
}
