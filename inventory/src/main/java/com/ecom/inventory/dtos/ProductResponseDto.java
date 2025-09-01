package com.ecom.inventory.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    private Long productId;
    private String name;
    private Double cost;
    private String supplier;
    private String supplierLocation;
    private String specs;
    private Double discount;       // percentage (e.g., 10 means 10%)
    private Double effectivePrice; // cost after applying discount
}
