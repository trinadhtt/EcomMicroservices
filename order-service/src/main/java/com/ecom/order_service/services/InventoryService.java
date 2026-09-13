package com.ecom.order_service.services;

import java.util.concurrent.CompletableFuture;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import com.ecom.order_service.client.InventoryClient;
import com.ecom.order_service.dto.Inventory;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.bulkhead.annotation.Bulkhead.Type;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;

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
	@RateLimiter(name="inventoryService", fallbackMethod = "fallBackRateLimiterMethod")
	public Inventory placeOrderFeign(Long productId) {
		//retry demo how its work
		System.out.println("Get Inventory for ProductId:"+ productId);
	    Inventory result = invClient.getInventory(productId);
		return result ;
	}
	
	public Inventory fallBackRateLimiterMethod(Long productId, Throwable throwable) {
		//rate Limiter demo how its work
		System.out.println("Fall back method  for Rate Limiter ProductId"+ productId);
		return new Inventory(productId, 5);
	}
	
	
	@CircuitBreaker(name="inventoryServiceCircuitBreaker", fallbackMethod = "fallBackCircuitBreakerMethod")
	public Inventory placeOrderFeignCircuitBreaker(Long productId) {
		//retry demo how its work
		System.out.println("Get Inventory for ProductId:"+ productId);
	    Inventory result = invClient.getInventory(productId);
		return result ;
	}
	
	public Inventory fallBackCircuitBreakerMethod(Long productId, Throwable throwable) {
		//Circuit breaker demo how its work
		System.out.println("Fall back method  for Circuit Breaker ProductId"+ productId);
		return new Inventory(productId, 5);
	}
	
	@TimeLimiter(name="inventoryServiceTimeLimiter", fallbackMethod = "fallBackTimeLimiterMethod")
	public CompletableFuture<Inventory> placeOrderFeignTimeLimiter(Long productId) {
		//retry demo how its work
		System.out.println("Get Inventory for ProductId:"+ productId);
		return CompletableFuture.supplyAsync(() -> invClient.getInventory(productId));
	}
	
	public CompletableFuture<Inventory> fallBackTimeLimiterMethod(Long productId, Throwable throwable) {
		//Time Limiter demo how its work
		System.out.println("Fall back method  for Time Limiter ProductId"+ productId);
		Inventory inv= new Inventory(productId, 5);
		return CompletableFuture.completedFuture(inv);
	}
	
	@Bulkhead(name="inventoryServiceBulkHead", fallbackMethod = "fallBackBulkHeadMethod")
	public Inventory placeOrderFeignBulkHead(Long productId) {
		//Bulk Head semaphore demo how its work
		System.out.println("Get Inventory for ProductId:"+ productId +" |Tread" + Thread.currentThread().getName());
	    Inventory result = invClient.getInventory(productId);
		return result ;
	}
	
	public Inventory fallBackBulkHeadMethod(Long productId, Throwable throwable) {
		//Bulk Head demo how its work
		System.out.println("Fall back method  for Bulk Head ProductId"+ productId + " | Reason" + throwable.getClass().getName());
		return new Inventory(productId, 5);
	}
	
	@Bulkhead(name="inventoryServiceThreadpoolBulkHead", type = Type.THREADPOOL,fallbackMethod = "bulkHeadThreadPoolFallBackMethod")
	public CompletableFuture<Inventory> placeOrderFeignBulkHeadThreadPool(Long productId) {
		//Bulk Head semaphore demo how its work
		System.out.println("Get Inventory for ProductId:"+ productId +" |Tread" + Thread.currentThread().getName());
		return CompletableFuture.supplyAsync(()->invClient.getInventory(productId)) ;
	}
	
	public CompletableFuture<Inventory> bulkHeadThreadPoolFallBackMethod(Long productId, Throwable throwable) {
		//Bulk Head demo how its work
		System.out.println("Fall back method  for Bulk Head Thread pool ProductId"+ productId + " | Reason" + throwable.getClass().getName());
		return CompletableFuture.completedFuture(new Inventory(productId, 0));
	}
}
