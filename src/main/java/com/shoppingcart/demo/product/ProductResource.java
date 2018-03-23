package com.shoppingcart.demo.product;

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
public class ProductResource {

	@Autowired
	private ProductRepository productRepository;

	@GetMapping("/products")
	public List<Product> retrieveAllProducts() {
		return productRepository.findAll();
	}
	
	@GetMapping("/products/{id}")
	public Product retrieveProduct(@PathVariable long id) {
		Optional<Product> product = productRepository.findById(id);

		if (!product.isPresent())
			throw new ProductNotFoundException("Product not found: id-" + id);

		return product.get();
	}
	
	@DeleteMapping("/products/{id}")
	public void deleteProduct(@PathVariable long id) {
		productRepository.deleteById(id);
	}
	
	@PostMapping("/products")
	public ResponseEntity<Object> createProduct(@RequestBody Product product) {
		Product saved = productRepository.save(product);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(saved.getId()).toUri();

		return ResponseEntity.created(location).build();

	}
	
	@PutMapping("/products/{id}")
	public ResponseEntity<Object> updateProduct(@RequestBody Product product, @PathVariable long id) {

		Optional<Product> productOptional = productRepository.findById(id);

		if (!productOptional.isPresent())
			return ResponseEntity.notFound().build();

		product.setId(id);
		
		productRepository.save(product);

		return ResponseEntity.noContent().build();
	}
}
