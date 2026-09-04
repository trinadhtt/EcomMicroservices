package com.ecom.order_service.services;

import org.springframework.stereotype.Service;

@Service
public class OrderService {
	
	public String placeOrder(String productId) {
		//ToDo call inventory service
		return "Order Placed";
		
	}

}
