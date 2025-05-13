package com.ecommerce.project.event;

import com.ecommerce.project.config.AppOperation;
import com.ecommerce.project.model.Product;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class ProductUpdateEvent extends ApplicationEvent {
    private final Product product;
    private final AppOperation operation;

    public ProductUpdateEvent(Object source, Product product, AppOperation operation) {
        super(source);
        this.product = product;
        this.operation = operation;
    }

}
