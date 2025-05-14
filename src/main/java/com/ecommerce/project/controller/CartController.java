package com.ecommerce.project.controller;

import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.security.response.MessageResponse;
import com.ecommerce.project.service.CartService;
import com.ecommerce.project.utils.AuthUtils;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartController {
    private final CartService cartService;
    private final AuthUtils authUtils;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(@PathVariable Long productId,
                                                    @PathVariable Integer quantity) {
        CartDTO cartDTO = cartService.addProductToCart(productId, quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getAllCarts() {
        List<CartDTO> cartDTOList = cartService.fetchAllCarts();
        return new ResponseEntity<>(cartDTOList, HttpStatus.FOUND);
    }

    @GetMapping("/carts/users")
    public ResponseEntity<CartDTO> getCart() {
        CartDTO cartDTO = cartService.fetchCart();
        return new ResponseEntity<>(cartDTO, HttpStatus.FOUND);
    }

    @PutMapping("/carts/products/{productId}/quantity/{operation}")
    public ResponseEntity<CartDTO> updateProductQuantityInCart(@PathVariable Long productId,
                                                               @PathVariable String operation) {
        CartDTO cartDTO = cartService.updateProductQuantityInCart(productId, operation);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }

    @DeleteMapping("/carts/{cartId}/products/{productId}")
    public ResponseEntity<CartDTO> deleteProductFromCart(@PathVariable Long cartId,
                                                         @PathVariable Long productId) {

        CartDTO cartDTO = cartService.removeProductFromCart(productId, cartId);
        return new ResponseEntity<>(cartDTO, HttpStatus.OK);
    }
}
