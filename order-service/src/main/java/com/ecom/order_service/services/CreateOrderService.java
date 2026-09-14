package com.ecom.order_service.services;

import org.springframework.stereotype.Service;

import com.ecom.order_service.dto.Order;
import com.ecom.order_service.kafka.OrderCreatedEvent;
import com.ecom.order_service.kafka.OrderKafkaProducer;
import com.ecom.order_service.repository.OrderRepository;

@Service
public class CreateOrderService {

	private final OrderRepository orderRepository;
	private final OrderKafkaProducer kafkaProducer;

	public CreateOrderService(OrderRepository orderRepository, OrderKafkaProducer kafkaProducer) {
		this.orderRepository = orderRepository;
		this.kafkaProducer = kafkaProducer;
	}

	public Order createOrder(Order order) {

		// Set initial status
		order.setStatus("CREATED");

		// Save order
		Order savedOrder = orderRepository.save(order);

		// Create Kafka event
		OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getProductId(),
				savedOrder.getQuantity());

		// Send event to Kafka
		kafkaProducer.sendOrderCreated(event);

		return savedOrder;
	}

	public Order getOrderById(Long id) {
		return orderRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found: " + id));
	}

}
