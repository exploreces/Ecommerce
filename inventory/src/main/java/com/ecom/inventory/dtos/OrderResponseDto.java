package com.ecom.inventory.dtos;


import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderResponseDto {
    private Long orderId;
    private Long userId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private Double unitPrice;
    private Double discount;       // percentage
    private Double effectivePrice; // after discount
    private Double totalAmount;    // effectivePrice * quantity
    private Boolean paid;
    private String status;         // created, placed, shipped, delivered, cancelled ...
}

