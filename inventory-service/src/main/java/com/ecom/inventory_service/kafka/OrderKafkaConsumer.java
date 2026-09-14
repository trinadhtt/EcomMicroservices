package com.ecom.inventory_service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaConsumer {

	@KafkaListener(topics = "order-send", groupId = "inventory-service")
	public void consume(OrderCreatedEvent event) {
		System.out.println("Received order: " + event.getOrderId());
		System.out.println("Product: " + event.getProductId());
		System.out.println("Quantity: " + event.getQuantity());
	}
}
