package com.ecom.inventory.services.interfaces;

import com.ecom.inventory.dtos.*;

import java.util.List;

public interface InventoryServiceInterface {



        // Cart
        CartSummaryResponseDto addToCart(CartItemRequestDto request);
        CartSummaryResponseDto updateCartItem(CartItemUpdateRequestDto request);
        CartSummaryResponseDto removeFromCart(Long userId, Long productId);
        CartSummaryResponseDto getUserCart(Long userId);
        CartSummaryResponseDto clearCart(Long userId);

        // Orders
        List<OrderResponseDto> checkout(CheckoutRequestDto request);
        OrderResponseDto getOrderById(Long orderId);
        List<OrderResponseDto> getOrdersByUser(Long userId);
        void cancelOrder(Long orderId); // sets status=cancelled
    }


