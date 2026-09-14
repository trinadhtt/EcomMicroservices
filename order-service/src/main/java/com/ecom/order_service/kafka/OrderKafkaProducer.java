package com.ecom.order_service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderKafkaProducer {

	private static final String TOPIC = "order-send";

	private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

	public OrderKafkaProducer(KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate) {

		this.kafkaTemplate = kafkaTemplate;
	}

	public void sendOrderCreated(OrderCreatedEvent event) {

		kafkaTemplate.send(TOPIC, event.getOrderId().toString(), event);

		System.out.println("Order event sent: " + event.getOrderId());
	}
}
