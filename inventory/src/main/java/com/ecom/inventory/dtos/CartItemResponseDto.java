package com.ecom.inventory.dtos;


import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemResponseDto {
    private Long productId;
    private String productName;
    private Double unitPrice;
    private Double discount;
    private Double effectivePrice;
    private Integer quantity;
    private Double lineTotal;
}

