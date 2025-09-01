package com.ecom.inventory.dtos.requestdto;


import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class CheckoutRequestDto {
    @NotNull private Long userId;
    private Boolean payNow;
}

