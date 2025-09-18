package com.ecom.inventory.controllers;


import com.ecom.inventory.dtos.requestdto.CartItemRequestDto;
import com.ecom.inventory.dtos.requestdto.CartItemUpdateRequestDto;
import com.ecom.inventory.dtos.requestdto.CheckoutRequestDto;
import com.ecom.inventory.dtos.responsedto.CartSummaryResponseDto;
import com.ecom.inventory.dtos.responsedto.OrderResponseDto;
import com.ecom.inventory.services.interfaces.InventoryServiceInterface;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryServiceInterface inventoryService;

    public InventoryController(InventoryServiceInterface inventoryService) {
        this.inventoryService = inventoryService;
    }

    // ---------- Cart ----------
    @PostMapping("/cart/items")
    public ResponseEntity<CartSummaryResponseDto> addToCart(@Valid @RequestBody CartItemRequestDto request) {
        return ResponseEntity.ok(inventoryService.addToCart(request));
    }

    @PutMapping("/cart/items")
    public ResponseEntity<CartSummaryResponseDto> updateCartItem(@Valid @RequestBody CartItemUpdateRequestDto request) {
        return ResponseEntity.ok(inventoryService.updateCartItem(request));
    }

    @DeleteMapping("/cart/items")
    public ResponseEntity<CartSummaryResponseDto> removeFromCart(@RequestParam Long userId,
                                                                 @RequestParam Long productId) {
        return ResponseEntity.ok(inventoryService.removeFromCart(userId, productId));
    }

    @GetMapping("/cart")
    public ResponseEntity<CartSummaryResponseDto> getUserCart(@RequestParam Long userId) {
        return ResponseEntity.ok(inventoryService.getUserCart(userId));
    }

    @DeleteMapping("/cart")
    public ResponseEntity<CartSummaryResponseDto> clearCart(@RequestParam Long userId) {
        return ResponseEntity.ok(inventoryService.clearCart(userId));
    }

    // ---------- Orders ----------
    @PostMapping("/orders/checkout")
    public ResponseEntity<List<OrderResponseDto>> checkout(@Valid @RequestBody CheckoutRequestDto request) {
        return ResponseEntity.ok(inventoryService.checkout(request));
    }


    @GetMapping("/orders/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(inventoryService.getOrderById(orderId));
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUser(@RequestParam Long userId) {
        return ResponseEntity.ok(inventoryService.getOrdersByUser(userId));
    }

    @DeleteMapping("/orders/{orderId}")
    public ResponseEntity<Void> cancelOrder(@PathVariable Long orderId) {
        inventoryService.cancelOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}

