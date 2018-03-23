package com.shoppingcart.demo.cart;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.shoppingcart.demo.customer.Customer;
import com.shoppingcart.demo.product.Product;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "carttype")
public abstract class Cart {

	@Id
	@GeneratedValue
	private Long id;
	

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
	private List<LineItem> linesItems = new ArrayList<LineItem>();
	
	private transient BigDecimal totalToPay = BigDecimal.ZERO;

	@ManyToOne
    @JoinColumn(name = "customerId")
	private Customer customer;
	
	private Boolean ordered = Boolean.FALSE;
	
	private BigDecimal finalTotal = BigDecimal.ZERO; 

	public Cart() {
		super();
	}
	
	public Cart(Long id, Customer customer, List<LineItem> linesItems) {
		this.id = id;
		this.customer = customer;
		this.linesItems = linesItems;
	}

	public Cart(Long id, Customer customer) {
		this.id = id;
		this.customer = customer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getTotalToPay() {
		return totalToPay;
	}

	public void setTotalToPay(BigDecimal totalToPay) {
		this.totalToPay = totalToPay;
	}

	
	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public List<LineItem> getLinesItems() {
		return linesItems;
	}

	public void setLinesItems(List<LineItem> linesItems) {
		this.linesItems = linesItems;
	}
	
	public Boolean getOrdered() {
		return ordered;
	}

	public void setOrdered(Boolean ordered) {
		this.ordered = ordered;
	}

	public BigDecimal getFinalTotal() {
		return finalTotal;
	}

	public void setFinalTotal(BigDecimal finalTotal) {
		this.finalTotal = finalTotal;
	}

	public  BigDecimal calculateTotal() {
		BigDecimal total = BigDecimal.ZERO;
		for (LineItem lineItem : this.getLinesItems()) {
			total = total.add(lineItem.getPrice().multiply(new BigDecimal(lineItem.getQuantity())));		
		}
		
		totalToPay = this.applyDiscounts(total);
		
		return totalToPay;
	}
	
	abstract BigDecimal applyDiscounts(BigDecimal total);
	
	Integer calculateAmount() {
		Integer amount = 0;
		for (LineItem lineItem : this.getLinesItems()) {
			amount += lineItem.getQuantity();		
		}
		return amount;
	}
	
	BigDecimal applyGeneralDiscount(BigDecimal total) {
		Integer amount = this.calculateAmount();
		BigDecimal chargeable = BigDecimal.ONE;
		if (Integer.valueOf(5).equals(amount)) {
			chargeable = BigDecimal.valueOf(0.8);
		}
		return total.multiply(chargeable);
	}
	
	public void addItem(Product product, Integer quantity) {
		Optional<LineItem> item =  linesItems.stream().filter(o -> o.getProduct().getId().equals(product.getId())).findFirst();
	    if (item.isPresent()) {
	    		LineItem lineItem = item.get();
	    		lineItem.setQuantity(lineItem.getQuantity() + quantity);
	    } else {
	    		linesItems.add(new LineItem(this, product, quantity, product.getPrice()));
	    }
	}
	
	public void removeItem(Product product) {
		Optional<LineItem> item =  linesItems.stream().filter(o -> o.getProduct().getId().equals(product.getId())).findFirst();
	    if (item.isPresent()) {
	    		linesItems.remove(item.get());
	    }
	}

	public void checkout() {
		ordered = Boolean.TRUE;
		finalTotal = this.calculateTotal();
	}

}
