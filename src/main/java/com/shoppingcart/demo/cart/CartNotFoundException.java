package com.shoppingcart.demo.cart;

public class CartNotFoundException extends RuntimeException {

	public CartNotFoundException(String exception) {
		super(exception);
	}

}
