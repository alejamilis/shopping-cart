package com.shoppingcart.demo.cart;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.shoppingcart.demo.product.Product;

@Entity
public class LineItem {
	
	@Id
	@GeneratedValue
	private Long id;
	
	@ManyToOne
    @JoinColumn(name = "cartId")
	@JsonIgnore
	private Cart cart;
	
	@ManyToOne
    @JoinColumn(name = "productId")
	private Product product;
	
	private Integer quantity;
	
	private BigDecimal price;

	public LineItem() {
		super();
	}

	public LineItem(Cart cart, Product product, Integer quantity, BigDecimal price) {
		this.cart = cart;
		this.product = product;
		this.quantity = quantity;
		this.price = price;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Cart getCart() {
		return cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
