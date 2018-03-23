package com.shoppingcart.demo.cart;

import java.math.BigDecimal;
import java.util.ListIterator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("VIP")
public class VIPCart extends Cart {
	private static BigDecimal DISCOUNT = BigDecimal.valueOf(700);


	@Override
	BigDecimal applyDiscounts(BigDecimal total) {
		total = applyGeneralDiscount(total);
		Integer amount = this.calculateAmount();
		if (Integer.valueOf(10).compareTo(amount) < 0) {
			BigDecimal bonus = findBonus();
			total = total.subtract(bonus);
			
			if (total.compareTo(DISCOUNT) < 0) {
				return BigDecimal.ZERO;
			}
			total = total.subtract(DISCOUNT);
		}
			
		return total;
	}

	private BigDecimal findBonus() {
		if (!this.getLinesItems().isEmpty()) {
			final ListIterator<LineItem> itr = this.getLinesItems().listIterator();
			LineItem min = itr.next();
			while (itr.hasNext()) {
				final LineItem curr = itr.next();
				if (curr.getPrice().compareTo(min.getPrice()) < 0) {
					min = curr;
				}
			}
			return min.getPrice().multiply(new BigDecimal(min.getQuantity()));
		}

		return BigDecimal.ZERO;

	}

}
