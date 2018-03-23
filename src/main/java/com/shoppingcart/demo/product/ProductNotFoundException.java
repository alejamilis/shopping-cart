package com.shoppingcart.demo.product;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(String exception) {
		super(exception);
	}
}
