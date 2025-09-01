package com.ecom.inventory.dtos;



import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CartItemRequestDto {
    @NotNull private Long userId;
    @NotNull private Long productId;
    @Min(1)  private Integer quantity;
}

