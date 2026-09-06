package com.ecom.order_service.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.order_service.services.OrderService;

@RestController
@RequestMapping("/orders")
public class OrderController {
	
	private final OrderService orderService;
	
	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}



	@PostMapping("/{productId}")
	public String placeOrder(@PathVariable Long productId) {
		//  Rest Cleint  
		//String output= orderService.placeOrderRestClient(productId);
		
		// Openfeign 
		String output= orderService.placeOrderFeign(productId);
		return output;
	}
	
}
