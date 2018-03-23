package com.shoppingcart.demo.product;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Product {

	@Id
	@GeneratedValue
	private Long id;
	private String description;
	private BigDecimal price;

	public Product(Long id, String description, BigDecimal price) {
		super();
		this.id = id;
		this.description = description;
		this.price = price;
	}


	public Product() {
		super();
	}
	
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}
}
