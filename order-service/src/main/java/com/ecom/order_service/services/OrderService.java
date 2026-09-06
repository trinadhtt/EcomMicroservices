package com.ecom.order_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.ecom.order_service.client.InventoryClient;
import com.ecom.order_service.dto.Inventory;
import com.ecom.order_service.exceptions.MyCustomRuntimeException;

@Service
public class OrderService {
	
	
	private final RestClient restClient;
	private final InventoryClient invClient;
	
	
	public OrderService(RestClient restClient,InventoryClient invClient) {
		this.invClient = invClient;
		this.restClient = restClient;
	}

	public String placeOrderRestClient(Long productId) {
		
		//Calling inventory service to check stock details
		
		/*
		 * String response= restClient.get()
		 * .uri("http://localhost:8081/inventory/{productId}",productId) .retrieve()
		 * .body(String.class);
		 */ 
		// refer this documentation: "https://docs.spring.io/spring-framework/reference/integration/rest-clients.html1"
		
		// Rest Client Implementation
		ResponseEntity<Inventory> result= restClient.get()
				.uri("http://localhost:8081/inventory/{productId}",productId)
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> { 
					throw new MyCustomRuntimeException(response.getStatusCode(), response.getHeaders()); 
				})
				.toEntity(Inventory.class);
		updateInventory(result.getBody(),productId);
		System.out.println("Response status: " + result.getStatusCode()); 
		System.out.println("Response headers: " + result.getHeaders()); 
		System.out.println("Contents: " + result.getBody()); 
		return result.getBody() != null  && result.getBody().getQuantity()>0? "Order Placed Succesful" :"Product Out of stock";
		
	}
	
	public String placeOrderFeign(Long productId) {
	    Inventory result = invClient.getInventory(productId);
	    System.out.println("Response Open feign : " +result.getQuantity() ); 
	    updateInventoryFeign(result,productId);
		return result != null && result.getQuantity() > 0 ? "Order Placed Succesful"
				: "Product Out of stock";
	}

	private void updateInventory(Inventory inventory, Long productId) {
		inventory.setQuantity(inventory.getQuantity()-1);
		ResponseEntity<Inventory> response= restClient.put()
				.uri("http://localhost:8081/inventory/update/{productId}",productId)
				.body(inventory)
				.retrieve()
				.toEntity(Inventory.class);
		System.out.println("Contents: " + response.getBody()); 
	}
	
	private void updateInventoryFeign(Inventory inventory, Long productId) {
		inventory.setQuantity(inventory.getQuantity()-1);
		Inventory response=invClient.updateInventory(productId, inventory);
		System.out.println("Contents update feign: " + response); 
	}

}
