package com.shoppingcart.demo.cart;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
	
	@Query("SELECT c FROM Cart c where c.customer.id = :id") 
    List<Cart> findCartsByCustomerId(@Param("id") Long id);
	
	@Query("SELECT li FROM LineItem li where li.cart.customer.dni = :dni") 
	List<LineItem> findItemsByCustomerDni(@Param("dni") String dni);

}
