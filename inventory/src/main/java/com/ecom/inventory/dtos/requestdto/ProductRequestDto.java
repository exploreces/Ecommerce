package com.ecom.inventory.dtos.requestdto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    private String name;
    private Double cost;
    private String supplier;
    private String supplierLocation;
    private String specs;
    private Integer count;
    private Double discount; // optional at creation
}

