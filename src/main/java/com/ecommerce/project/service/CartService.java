package com.ecommerce.project.service;

import com.ecommerce.project.payload.CartDTO;

import java.util.List;

public interface CartService {
    CartDTO addProductToCart(Long productId, Integer quantity);
    List<CartDTO> fetchAllCarts();

    CartDTO fetchCart();
    
    CartDTO updateProductQuantityInCart(Long productId, String operation);

    CartDTO removeProductFromCart(Long productId, Long cartId);
    void removeProductFromCarts(Long productId);
    void updateProductInCarts(Long productId);
}
