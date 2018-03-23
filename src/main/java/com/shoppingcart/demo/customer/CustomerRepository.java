package com.shoppingcart.demo.customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
	
	@Query("SELECT c FROM Customer c where c.dni = :dni") 
    Optional<Customer> findByDni(@Param("dni") String dni);

}
