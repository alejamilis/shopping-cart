package com.shoppingcart.demo.cart;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("PROMO")
public class PromoCart extends Cart {
	
	private static BigDecimal DISCOUNT = BigDecimal.valueOf(500);
	
	@Override
	BigDecimal applyDiscounts(BigDecimal total) {
		total = applyGeneralDiscount(total);
		Integer amount = this.calculateAmount();
		if (Integer.valueOf(10).compareTo(amount) < 0) {
			if (total.compareTo(DISCOUNT) < 0) {
				return BigDecimal.ZERO;
			}
			total = total.subtract(DISCOUNT);
		}
		return total;
	}

}
