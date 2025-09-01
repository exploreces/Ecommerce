package com.ecom.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String name;
    private Double cost;
    private String supplier;
    private String supplierLocation;
    private String specs;
    private Integer count;
    private Double discount;
}
