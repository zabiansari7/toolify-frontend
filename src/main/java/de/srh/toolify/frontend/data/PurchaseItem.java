package de.srh.toolify.frontend.data;

import java.math.BigDecimal;

public class PurchaseItem {
	
	private Long productId;
	private int quantity;
	private BigDecimal purchasePrice;
	private String productName;
	
	public PurchaseItem(Long productId, int quantity, BigDecimal purchasePrice, String productName) {
		this.productId = productId;
		this.quantity = quantity;
		this.purchasePrice = purchasePrice;
		this.productName = productName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}
	
	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
}
