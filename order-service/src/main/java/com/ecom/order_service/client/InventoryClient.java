package com.ecom.order_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ecom.order_service.config.InventoryFeignClientConfig;
import com.ecom.order_service.dto.Inventory;

@FeignClient(name="inventory-service", url= "http://localhost:8081",configuration = InventoryFeignClientConfig.class)
public interface InventoryClient {
	
    @GetMapping("/inventory/{productId}")
    Inventory getInventory(@PathVariable Long productId);
    
    @PutMapping("/inventory/update/{id}")
	Inventory updateInventory(@PathVariable Long id,@RequestBody Inventory inventory);
	
}
