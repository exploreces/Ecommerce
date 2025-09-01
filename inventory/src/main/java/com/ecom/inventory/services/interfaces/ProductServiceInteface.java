package com.ecom.inventory.services.interfaces;


import com.ecom.inventory.dtos.requestdto.ProductRequestDto;
import com.ecom.inventory.dtos.responsedto.ProductResponseDto;

import java.util.List;

public interface ProductServiceInteface {

    ProductResponseDto addProduct(ProductRequestDto product);

    ProductResponseDto updateProduct(Long productId, ProductRequestDto product);

    void deleteProduct(Long productId);

    ProductResponseDto applyDiscount(Long productId, Double discount);

    List<ProductResponseDto> getAllProducts(int page , int size);

    ProductResponseDto getProductById(Long productId);
}

