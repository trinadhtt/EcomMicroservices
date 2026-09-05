package com.ecom.inventory_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecom.inventory_service.model.Inventory;
import com.ecom.inventory_service.repository.InventoryRepository;
@Service
public class InventoryService {
	
	@Autowired
	InventoryRepository inventoryRepo;

	public Inventory createInventory(Inventory inventory) {
		return inventoryRepo.save(inventory);
	}

	public Inventory updateInventory(Long id, Inventory inventory) {
		Inventory existingInventory = inventoryRepo.findByProductId(id);
	    existingInventory.setProductId(inventory.getProductId());
	    existingInventory.setQuantity(inventory.getQuantity());
	    return inventoryRepo.save(existingInventory);
	}

}
