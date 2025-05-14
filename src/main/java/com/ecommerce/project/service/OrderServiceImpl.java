package com.ecommerce.project.service;

import com.ecommerce.project.exception.ResourceNotFoundException;
import com.ecommerce.project.model.*;
import com.ecommerce.project.payload.OrderDTO;
import com.ecommerce.project.payload.OrderItemDTO;
import com.ecommerce.project.payload.OrderRequest;
import com.ecommerce.project.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class OrderServiceImpl implements OrderService {
    private final CartRepository cartRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDTO placeOrder(String email, String paymentMethod, OrderRequest orderRequest) {
        Cart cart = cartRepository.findByUserEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "user email", email));

        Address address = addressRepository.findById(orderRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", orderRequest.getAddressId()));

        Order order = Order.builder()
                .date(LocalDate.now())
                .totalAmount(cart.getTotalPrice())
                .status("Order accepted!")
                .shippingAddress(address)
                .email(email)
                .build();

        Payment payment = Payment.builder()
                .pgPaymentId(orderRequest.getPgPaymentId())
                .pgPaymentStatus(orderRequest.getPgPaymentStatus())
                .pgPaymentResponse(orderRequest.getPgResponseMessage())
                .pgName(orderRequest.getPgName())
                .method(paymentMethod)
                .order(order)
                .build();

        paymentRepository.save(payment);
        order.setPayment(payment);
        Order savedOrder = orderRepository.save(order);

        Set<OrderItem> orderItems = new HashSet<>();
        List<Product> products = new ArrayList<>();

        List<CartItem> cartItems = cart.getCartItems();
        List<CartItem> orderedCartItems = new ArrayList<>();
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());

            OrderItem orderItem = OrderItem.builder()
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getPrice())
                    .discount(cartItem.getDiscount())
                    .order(savedOrder)
                    .build();

            products.add(product);
            orderItems.add(orderItem);
            orderedCartItems.add(cartItem);
            cart.setTotalPrice(cart.getTotalPrice() - cartItem.getPrice());
        }

        orderItemRepository.saveAll(orderItems);
        productRepository.saveAll(products);
        orderedCartItems.forEach(cartItem -> cart.getCartItems().remove(cartItem));
        cartRepository.save(cart);

        OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        Set<OrderItemDTO> orderItemDTOS = new HashSet<>();
        for (OrderItem orderItem : orderItems) {
            OrderItemDTO orderItemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
            orderItemDTOS.add(orderItemDTO);
        }
        orderDTO.setOrderItems(orderItemDTOS);
        return orderDTO;
    }
}
