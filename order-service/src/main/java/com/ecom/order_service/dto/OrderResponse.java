package com.ecom.order_service.dto;
public record OrderResponse(
        Long orderId,
        Long productId,
        Integer quantity,
        Double amount
) {
	
}