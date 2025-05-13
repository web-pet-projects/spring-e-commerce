package com.ecommerce.project.repository;

import com.ecommerce.project.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1")
    Optional<Cart> findByUserEmail(String email);

    @Query("SELECT c FROM Cart c WHERE c.user.email = ?1 AND c.cartId = ?2")
    Optional<Cart> findByUserEmailAndCartId(String email, Long cartId);

    @Query("SELECT DISTINCT c FROM Cart c LEFT JOIN FETCH c.cartItems ci LEFT JOIN FETCH ci.product WHERE c.cartId " +
            "IN (SELECT c2.cartId FROM Cart c2 JOIN c2.cartItems ci2 WHERE ci2.product.productId = ?1)")
    List<Cart> findAllByProductId(Long productId);
}
