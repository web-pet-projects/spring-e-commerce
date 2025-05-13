package com.ecommerce.project.event;

import com.ecommerce.project.config.AppOperation;
import com.ecommerce.project.model.Cart;
import com.ecommerce.project.model.CartItem;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.repository.CartItemRepository;
import com.ecommerce.project.repository.CartRepository;
import com.ecommerce.project.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Component
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class CartEventListener {
    private final CartService cartService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleProductUpdated(ProductUpdateEvent productUpdateEvent) {

        Product product = productUpdateEvent.getProduct();
        AppOperation operation = productUpdateEvent.getOperation();
        boolean isUpdate = AppOperation.UPDATE.equals(operation);
        if (isUpdate) {
            cartService.updateProductInCarts(product.getProductId());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, fallbackExecution = true)
    public void handleProductRemoved(ProductUpdateEvent productUpdateEvent) {
        Product product = productUpdateEvent.getProduct();
        AppOperation operation = productUpdateEvent.getOperation();
        boolean isRemoved = AppOperation.REMOVE.equals(operation);
        if (isRemoved) {
            cartService.removeProductFromCarts(product.getProductId());
        }
    }
}
