package com.ecom.inventory.dtos;


import lombok.*;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartSummaryResponseDto {
    private Long userId;
    private List<CartItemResponseDto> items;
    private Double cartTotal;
}

