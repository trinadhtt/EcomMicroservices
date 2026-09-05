package com.ecom.inventory_service.controllers;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecom.inventory_service.model.Inventory;
import com.ecom.inventory_service.repository.InventoryRepository;
import com.ecom.inventory_service.services.InventoryService;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
	
   private  InventoryService inventoryService;
   private  InventoryRepository inventoryRepo;
	
   public InventoryController(InventoryService inventoryService, InventoryRepository inventoryRepo) {
	   this.inventoryService= inventoryService;
	   this.inventoryRepo= inventoryRepo;
   }
	
	
	@GetMapping("/{productId}")
	public Inventory checkInventory(@PathVariable Long productId) {
		return inventoryRepo.findByProductId(productId);
	}
	
	@PostMapping("/create")
	public Inventory addInventory(@RequestBody Inventory inventory) {
		Inventory product =inventoryService.createInventory(inventory);
		return product;
	}
	
	@PutMapping("/update/{id}")
	public Inventory updateInventory(@PathVariable Long id,@RequestBody Inventory inventory) {
		return inventoryService.updateInventory(id,inventory);
	}

}
