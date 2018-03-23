package com.shoppingcart.demo.cart;

public class CartFactory {
	
	private static String NORMAL = "normal";
	
	private static String VIP = "vip";
	
	private static String PROMO = "promo";

	public static Cart getNewCart(String type) {
		
		if (VIP.equals(type)) {
			return new VIPCart();
		}
		if (PROMO.equals(type)) {
			return new PromoCart();
		}
		return new NormalCart();
	}
}
