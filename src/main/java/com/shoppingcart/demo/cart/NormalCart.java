package com.shoppingcart.demo.cart;

import java.math.BigDecimal;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("NORMAL")
public class NormalCart extends Cart {
	
	private static BigDecimal DISCOUNT = BigDecimal.valueOf(200);

	@Override
	BigDecimal applyDiscounts(BigDecimal total) {
		Integer amount = this.calculateAmount();
		total = applyGeneralDiscount(total);
		if (Integer.valueOf(10).compareTo(amount) < 0) {
			total = total.subtract(DISCOUNT);
		}
		return total;
	}

}
