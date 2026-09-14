package com.ecom.order_service.services;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecom.order_service.dto.CreateOrderRequest;
import com.ecom.order_service.dto.Order;
import com.ecom.order_service.dto.OrderResponse;
import com.ecom.order_service.exception.OrderNotFoundException;
import com.ecom.order_service.kafka.OrderCreatedEvent;
import com.ecom.order_service.kafka.OrderKafkaProducer;
import com.ecom.order_service.repository.OrderRepository;

@Service
public class CreateOrderService {

	private final OrderRepository orderRepository;
	private final OrderKafkaProducer orderKafkaProducer;

	public CreateOrderService(OrderRepository orderRepository, OrderKafkaProducer orderKafkaProducer) {

		this.orderRepository = orderRepository;
		this.orderKafkaProducer = orderKafkaProducer;
	}

	/**
	 * Create Order
	 *
	 * PostgreSQL = Source of Truth Kafka = Publish OrderCreatedEvent Redis = Used
	 * for subsequent reads
	 */
	@Transactional
	public Order createOrder(Order order) {

		// 1. Create entity
		order.setStatus("CREATED");

		// 2. Save to PostgreSQL
		Order savedOrder = orderRepository.save(order);

		// 3. Publish event to Kafka
		OrderCreatedEvent event = new OrderCreatedEvent(savedOrder.getId(), savedOrder.getProductId(),
				savedOrder.getQuantity());
		orderKafkaProducer.sendOrderCreated(event);
		// 4. Return response
		return savedOrder;
	}

	/**
	 * Get Order
	 *
	 * First request:
	 *
	 * Redis MISS ↓ PostgreSQL ↓ Redis
	 *
	 * Subsequent request:
	 *
	 * Redis HIT
	 */
	@Cacheable(value = "orders", key = "#orderId")
	@Transactional(readOnly = true)
	public OrderResponse getOrder(Long orderId) {

		System.out.println("========== FETCHING FROM POSTGRESQL ==========");

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

		return convertToResponse(order);
	}

	/**
	 * Update Order
	 *
	 * PostgreSQL updated first. Redis cache is then evicted.
	 */
	@CachePut(value = "orders", key = "#orderId")
	@Transactional
	public OrderResponse updateOrder(Long orderId, CreateOrderRequest request) {

		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

		order.setProductId(request.productId());
		order.setQuantity(request.quantity());
		order.setPrice(request.amount());

		Order updatedOrder = orderRepository.save(order);

		return convertToResponse(updatedOrder);
	}

	/**
	 * Delete Order
	 *
	 * Delete from PostgreSQL ↓ Remove Redis cache
	 */
	@CacheEvict(value = "orders", key = "#orderId")
	@Transactional
	public void deleteOrder(Long orderId) {

		/*
		 * if (!orderRepository.existsById(orderId)) { throw new
		 * RuntimeException("Order not found: " + orderId); }
		 */
		Order order = orderRepository.findById(orderId)
	            .orElseThrow(() ->
	                    new OrderNotFoundException(
	                            "Order not found: " + orderId));
		orderRepository.deleteById(orderId);
	}

	/**
	 * Convert Entity -> DTO
	 */
	private OrderResponse convertToResponse(Order order) {

		return new OrderResponse(order.getId(), order.getProductId(), order.getQuantity(), order.getPrice());
	}
}