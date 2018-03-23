package com.shoppingcart.demo.cart;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.shoppingcart.demo.customer.Customer;
import com.shoppingcart.demo.customer.CustomerNotFoundException;
import com.shoppingcart.demo.customer.CustomerRepository;
import com.shoppingcart.demo.product.Product;
import com.shoppingcart.demo.product.ProductNotFoundException;
import com.shoppingcart.demo.product.ProductRepository;

@RestController
public class CartResource {

	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private ProductRepository productRespository;
	

	@GetMapping("/carts")
	public List<Cart> retrieveAllCarts() {
		return cartRepository.findAll();
	}
	
	@GetMapping("/carts/{id}")
	public Cart retrieveCart(@PathVariable long id) {
		Optional<Cart> cart = cartRepository.findById(id);

		if (!cart.isPresent())
			throw new CartNotFoundException("Cart not found: id-" + id);
		
		return cart.get();
	}
	
	@DeleteMapping("/carts/{id}")
	public void deleteCart(@PathVariable long id) {
		cartRepository.deleteById(id);
	}
	
	@GetMapping("/customers/{customerId}/carts")
	public List<Cart> retrieveCustomerCarts(@PathVariable long customerId) {
		return cartRepository.findCartsByCustomerId(customerId);
	}
	
	@PostMapping("/customers/{dni}/carts")
	public Long createCart(@RequestBody String type, @PathVariable String dni) {
		Optional<Customer> customer = customerRepository.findByDni(dni);
		if (!customer.isPresent())
			throw new CustomerNotFoundException("Customer not found: dni "+dni);
		
		Cart cart = CartFactory.getNewCart(type);
		cart.setCustomer(customer.get());
		Cart saved = cartRepository.save(cart);

		return saved.getId();

	}
	
	@DeleteMapping("/customers/{customerId}/carts/{cartId}")
	public String deleteCart(@PathVariable long customerId, @PathVariable("cartId") Long cartId) {
		Optional<Cart> oCart = cartRepository.findById(cartId);
		if (!oCart.isPresent())
			throw new CartNotFoundException("Cart not found: id-" + cartId);
		
		cartRepository.delete(oCart.get());

		return "Cart " + cartId + " deleted successfully";

	}
	
	@PutMapping("/customers/{customerId}/carts/{cartId}/add/{productId}")
	public Cart addProduct(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId,
			@RequestBody Integer quantity) {
		Optional<Cart> oCart = cartRepository.findById(cartId);
		if (!oCart.isPresent())
			throw new CartNotFoundException("Cart not found: id-" + cartId);
		
		Optional<Product> oProduct = productRespository.findById(productId);
		if (!oProduct.isPresent()) {
			throw new ProductNotFoundException("Product not found: id-" + productId);
		}
		
		Cart cart = oCart.get();
		Product product = oProduct.get();
		cart.addItem(product, quantity);
		cart.calculateTotal();
		cartRepository.save(cart);
		return cart;
	}
	
	@PutMapping("/customers/{customerId}/carts/{cartId}/remove/{productId}")
	public Cart removeProduct(@PathVariable("cartId") Long cartId, @PathVariable("productId") Long productId ){
		Optional<Cart> oCart = cartRepository.findById(cartId);
		if (!oCart.isPresent())
			throw new CartNotFoundException("Cart not found: id-" + cartId);
		
		Optional<Product> oProduct = productRespository.findById(productId);
		if (!oProduct.isPresent()) {
			throw new ProductNotFoundException("Product not found: id-" + productId);
		}
		
		Cart cart = oCart.get();
		Product product = oProduct.get();
		cart.removeItem(product);
		cart.calculateTotal();
		cartRepository.save(cart);
		return cart;
	}
	
	@PostMapping("/customers/{customerId}/carts/{cartId}/checkout")
	public Cart checkout(@PathVariable("cartId") Long cartId) {
		Optional<Cart> oCart = cartRepository.findById(cartId);
		if (!oCart.isPresent())
			throw new CartNotFoundException("Cart not found: id-" + cartId);
	
		Cart cart = oCart.get();
		cart.checkout();
		cartRepository.save(cart);
		return cart;
	}
	
	
	@GetMapping("/customers/{dni}/expensive")
	public List<Product> getMostExpensive(@PathVariable("dni") String dni) {
		List<LineItem> items = cartRepository.findItemsByCustomerDni(dni);
		
		List<Product> products = items.stream().sorted(new Comparator<LineItem>() {
	        public int compare(LineItem o1, LineItem o2) {
	            int answer = o1.getProduct().getPrice().compareTo(o2.getProduct().getPrice());
	            return -1 * answer;
	        }
	    }).map(o -> o.getProduct()).collect(Collectors.toList());;
		
	    return products.subList(0, 3);
	}
}
