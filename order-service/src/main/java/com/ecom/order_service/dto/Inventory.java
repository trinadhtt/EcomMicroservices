package com.ecom.order_service.dto;

public class Inventory {

	public Inventory(Long productId, Integer quantity) {
		super();
		this.productId = productId;
		this.quantity = quantity;
	}

	private Long productId;
	private Integer quantity;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
