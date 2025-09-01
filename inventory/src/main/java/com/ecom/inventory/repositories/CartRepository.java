package com.ecom.inventory.repositories;

import com.ecom.inventory.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart , Long> {
}
