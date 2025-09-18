package com.ecom.inventory.controllers;

import com.ecom.inventory.dtos.requestdto.ProductRequestDto;
import com.ecom.inventory.dtos.responsedto.ProductResponseDto;
import com.ecom.inventory.services.interfaces.ProductServiceInteface;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")

@RequestMapping("/api/products")
public class ProductController {

    private final ProductServiceInteface productService;

    public ProductController(ProductServiceInteface productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> addProduct(@RequestBody ProductRequestDto product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable Long productId,
                                                 @RequestBody ProductRequestDto updatedProduct) {
        return ResponseEntity.ok(productService.updateProduct(productId, updatedProduct));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{productId}/discount")
    public ResponseEntity<ProductResponseDto> applyDiscount(@PathVariable Long productId,
                                                 @RequestParam Double discount) {
        return ResponseEntity.ok(productService.applyDiscount(productId, discount));
    }


    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(
            @RequestParam(value="page" , defaultValue = "0")int page,
            @RequestParam(value="size" , defaultValue = "3")int size)
    {
        return ResponseEntity.ok(productService.getAllProducts(page , size));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
