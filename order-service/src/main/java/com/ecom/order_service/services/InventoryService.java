package com.ecom.order_service.services;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.ecom.order_service.client.InventoryClient;
import com.ecom.order_service.dto.Inventory;

@Service
public class InventoryService {
private final InventoryClient invClient;
	
	
	public InventoryService(InventoryClient invClient) {
		this.invClient = invClient;
	}
	
	@Retryable(
		    retryFor = Exception.class,
		    maxAttempts = 3,
		    backoff = @Backoff(delay = 2000))
	public Inventory placeOrderFeign(Long productId) {
		//retry demo how its work
		System.out.println("Get Inventory for ProductId:"+ productId);
	    Inventory result = invClient.getInventory(productId);
		return result ;
	}
	
}
