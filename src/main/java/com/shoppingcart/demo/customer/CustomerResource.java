package com.shoppingcart.demo.customer;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class CustomerResource {

	@Autowired
	private CustomerRepository customerRepository;

	@GetMapping("/customers")
	public List<Customer> retrieveAllCustomers() {
		return customerRepository.findAll();
	}
	
	@GetMapping("/customers/{id}")
	public Customer retrieveCustomer(@PathVariable long id) {
		Optional<Customer> customer = customerRepository.findById(id);

		if (!customer.isPresent())
			throw new CustomerNotFoundException("Customer not found: id-" + id);

		return customer.get();
	}
	
	@DeleteMapping("/customers/{id}")
	public void deleteCustomer(@PathVariable long id) {
		customerRepository.deleteById(id);
	}
	
	@PostMapping("/customers")
	public ResponseEntity<Object> createCustomer(@RequestBody Customer customer) {
		Customer saved = customerRepository.save(customer);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(saved.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/customers/{id}")
	public ResponseEntity<Object> updateCustomer(@RequestBody Customer customer, @PathVariable long id) {

		Optional<Customer> customerOptional = customerRepository.findById(id);

		if (!customerOptional.isPresent())
			return ResponseEntity.notFound().build();

		customer.setId(id);
		
		customerRepository.save(customer);

		return ResponseEntity.noContent().build();
	}
}
