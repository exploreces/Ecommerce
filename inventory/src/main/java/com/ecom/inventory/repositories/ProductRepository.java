package com.ecom.inventory.repositories;

import com.ecom.inventory.entity.Product;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.name = :name AND p.supplier = :supplier")
    Optional<Product> findByNameAndSupplier(@Param("name") String name,
                                            @Param("supplier") String supplier);

    @Override
    Page<Product> findAll(Pageable pageable);
}
