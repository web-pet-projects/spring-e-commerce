package com.ecommerce.project.service;

import com.ecommerce.project.exception.APIException;
import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.model.User;
import com.ecommerce.project.payload.CartDTO;
import com.ecommerce.project.payload.CartItemDTO;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.repository.UserRepository;
import com.ecommerce.project.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final AuthUtils authUtils;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final ModelMapper modelMapper;

    private Cart createCart() {
        return cartRepository.findByUserEmail(authUtils.loggedInEmail())
                .orElseGet(() -> {
                    User currentUser = userRepository.findByUsername(authUtils.loggedInUsername())
                            .orElseThrow(() -> new ResourceNotFoundException("User", "username", authUtils.loggedInUsername()));
                    Cart newCart = Cart.builder()
                            .user(currentUser)
                            .totalPrice(0.0)
                            .cartItems(new ArrayList<>())
                            .build();
                    return cartRepository.save(newCart);
                });
    }

    @Override
    @Transactional
    public CartDTO addProductToCart(Long productId, Integer quantity) {
        Cart cart = createCart();

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        CartItem cartItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), productId)
                .orElse(null);

        if (cartItem != null) {
            throw new APIException("Product is already in cart");
        }
        // Validate product quantity in storage
        if (product.getQuantity() == 0) {
            throw new APIException("Product is not available");
        }

        if (product.getQuantity() < quantity) {
            throw new APIException("Product quantity is too small");
        }

        Double cartItemPrice = product.getSpecialPrice() * quantity;
        CartItem newCartItem = CartItem.builder()
                .cart(cart)
                .product(product)
                .quantity(quantity)
                .price(cartItemPrice)
                .discount(product.getDiscount())
                .build();
        cartItemRepository.save(newCartItem);

        cart.setTotalPrice(cart.getTotalPrice() + cartItemPrice);
        List<CartItem> cartItems = cart.getCartItems();
        cartItems.add(newCartItem);
        cartRepository.save(cart);

        // Map to DTO
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<CartItemDTO> cartItemDTOs = cartItems.stream()
                .map(ci -> modelMapper.map(ci, CartItemDTO.class))
                .toList();
        cartDTO.setCartItems(cartItemDTOs);
        return cartDTO;

    }

    @Override
    public List<CartDTO> fetchAllCarts() {
        List<Cart> carts = cartRepository.findAll();
        if (carts.isEmpty()) {
            throw new APIException("No cart found");
        }

        return carts.stream().map(c -> modelMapper.map(c, CartDTO.class)).toList();
    }

    @Override
    public CartDTO fetchCart() {
        String email = authUtils.loggedInEmail();
        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "user email", email));
        Cart foundCart = cartRepository.findByUserEmailAndCartId(email, cart.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cart.getCartId()));
        return modelMapper.map(foundCart, CartDTO.class);
    }

    @Override
    @Transactional
    public CartDTO updateProductQuantityInCart(Long productId, String operation) {
        String email = authUtils.loggedInEmail();

        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "user email", email));

        CartItem cartItem = cartItemRepository.findByCartCartIdAndProductProductId(cart.getCartId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        int quantityToUpdate = operation.equalsIgnoreCase("delete") ? -1 : 1;
        int updatedQuantity = cartItem.getQuantity() + quantityToUpdate;
        Product product = cartItem.getProduct();

        if (product.getQuantity() < 0) {
            throw new APIException("Product is not available");
        }
        if (updatedQuantity == 0) {
            return removeProductFromCart(productId, cart.getCartId());
        }
        if (updatedQuantity < 0) {
            throw new APIException("Quantity is too small");
        }
        if (updatedQuantity > product.getQuantity()) {
            throw new APIException("Quantity is too large");
        }

        cartItem.setQuantity(updatedQuantity);
        cartItem.setPrice(product.getSpecialPrice() * updatedQuantity);
        cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice() + product.getSpecialPrice() * quantityToUpdate);
        cartRepository.save(cart);
        return modelMapper.map(cart, CartDTO.class);

    }

    @Override
    @Transactional
    public CartDTO removeProductFromCart(Long productId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "id", cartId));
        CartItem cartItem = cartItemRepository.findByCartCartIdAndProductProductId(cartId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));

        cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice());
        cart.getCartItems().remove(cartItem);

        cartRepository.save(cart);
        return modelMapper.map(cart, CartDTO.class);
    }

    @Override
    public void removeProductFromCarts(Long productId) {
        List<Cart> carts = cartRepository.findAllByProductId(productId);
        for (Cart cart : carts) {
            List<CartItem> cartItems = cart.getCartItems();
            cartItems.removeIf(ci -> ci.getProduct().getProductId().equals(productId));
            double totalPrice = cartItems.stream().map(CartItem::getPrice).reduce(0.0, Double::sum);
            cart.setTotalPrice(totalPrice);
        }
        cartRepository.saveAll(carts);
    }

    @Override
    @Transactional
    public void updateProductInCarts(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", productId));
        List<CartItem> cartItems = cartItemRepository.findAllByProductProductId(productId);

        for (CartItem cartItem : cartItems) {
            cartItem.setProduct(product);
            cartItem.setDiscount(product.getDiscount());
            cartItem.setPrice(product.getSpecialPrice() * cartItem.getQuantity());
        }
        cartItemRepository.saveAll(cartItems);

        List<Cart> carts = cartRepository.findAllByProductId(productId);
        for (Cart cart : carts) {
            double totalPrice = cart.getCartItems().stream().map(CartItem::getPrice).reduce(0.0, Double::sum);
            cart.setTotalPrice(totalPrice);
        }
        cartRepository.saveAll(carts);
    }


}
