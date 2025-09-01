package com.ecom.inventory.services;

import com.ecom.inventory.dtos.*;
import com.ecom.inventory.entity.Cart;
import com.ecom.inventory.entity.Order;
import com.ecom.inventory.entity.Product;
import com.ecom.inventory.entity.User;
import com.ecom.inventory.exceptions.InsufficientStockException;
import com.ecom.inventory.repositories.*;
import com.ecom.inventory.services.interfaces.InventoryServiceInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class InventoryService implements InventoryServiceInterface {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;

    public InventoryService(UserRepository userRepository,
                            ProductRepository productRepository,
                            CartRepository cartRepository,
                            OrderRepository orderRepository,
                            ObjectMapper objectMapper) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
        this.orderRepository = orderRepository;
        this.objectMapper = objectMapper;
    }

    // ---------------- CART ----------------

    @Override
    public CartSummaryResponseDto addToCart(CartItemRequestDto request) {
        User user = getUser(request.getUserId());
        Product product = getProduct(request.getProductId());

        if (product.getCount() < request.getQuantity()) {
            throw new IllegalArgumentException("The required quantity is not available");
        }

        Optional<Cart> existing = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(user.getUserId())
                        && c.getProduct().getProductId().equals(product.getProductId()))
                .findFirst();

        Cart line = existing.orElseGet(() -> Cart.builder()
                .user(user).product(product).quantity(0).build());

        line.setQuantity(line.getQuantity() + request.getQuantity());
        cartRepository.save(line);

        return buildCartSummary(user.getUserId());
    }

    @Override
    public CartSummaryResponseDto updateCartItem(CartItemUpdateRequestDto request) {
        User user = getUser(request.getUserId());
        Product product = getProduct(request.getProductId());

        Cart line = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(user.getUserId())
                        && c.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        line.setQuantity(request.getQuantity());
        cartRepository.save(line);

        return buildCartSummary(user.getUserId());
    }

    @Override
    public CartSummaryResponseDto removeFromCart(Long userId, Long productId) {
        User user = getUser(userId);
        Product product = getProduct(productId);

        Cart line = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(user.getUserId())
                        && c.getProduct().getProductId().equals(product.getProductId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartRepository.delete(line);
        return buildCartSummary(user.getUserId());
    }

    @Override
    public CartSummaryResponseDto getUserCart(Long userId) {
        getUser(userId); // validation
        return buildCartSummary(userId);
    }

    @Override
    public CartSummaryResponseDto clearCart(Long userId) {
        User user = getUser(userId);
        List<Cart> lines = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(user.getUserId()))
                .toList();

        cartRepository.deleteAll(lines);
        return CartSummaryResponseDto.builder()
                .userId(userId).items(Collections.emptyList()).cartTotal(0.0).build();
    }

    // ---------------- ORDERS ----------------

    @Override
    public List<OrderResponseDto> checkout(CheckoutRequestDto request) {
        User user = getUser(request.getUserId());

        List<Cart> cartItems = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(user.getUserId()))
                .toList();

        if (cartItems.isEmpty()) {
            throw new ResourceNotFoundException("Cart is empty");
        }

        List<Order> createdOrders = new ArrayList<>();

        for (Cart cartLine : cartItems) {
            Product product = cartLine.getProduct();

            if (product.getCount() < cartLine.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
            }

            // reduce stock
            product.setCount(product.getCount() - cartLine.getQuantity());
            productRepository.save(product);

            // create order
            Order order = Order.builder()
                    .user(user)
                    .product(product)
                    .quantity(cartLine.getQuantity())
                    .status("PLACED")
                    .paid(Boolean.TRUE.equals(request.getPayNow()))
                    .totalAmount(calcEffectivePrice(product) * cartLine.getQuantity())
                    .build();

            createdOrders.add(orderRepository.save(order));
        }

        cartRepository.deleteAll(cartItems);

        return createdOrders.stream()
                .map(order -> objectMapper.convertValue(order, OrderResponseDto.class))
                .toList();
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        if (!"CANCELLED".equals(order.getStatus())) {
            order.setStatus("CANCELLED");
            orderRepository.save(order);

            // regain stock
            Product product = order.getProduct();
            product.setCount(product.getCount() + order.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponseDto getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        return objectMapper.convertValue(order, OrderResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDto> getOrdersByUser(Long userId) {
        User user = getUser(userId);
        return orderRepository.findAll().stream()
                .filter(o -> o.getUser().getUserId().equals(user.getUserId()))
                .map(o -> objectMapper.convertValue(o, OrderResponseDto.class))
                .toList();
    }

    // ---------------- HELPERS ----------------

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
    }

    private double calcEffectivePrice(Product p) {
        double price = Optional.ofNullable(p.getCost()).orElse(0.0);
        Double disc = p.getDiscount();
        if (disc != null && disc > 0) {
            price -= (price * (disc / 100.0));
        }
        return price;
    }

    private CartSummaryResponseDto buildCartSummary(Long userId) {
        List<Cart> lines = cartRepository.findAll().stream()
                .filter(c -> c.getUser().getUserId().equals(userId))
                .toList();

        List<CartItemResponseDto> items = lines.stream()
                .map(line -> {
                    double effectivePrice = calcEffectivePrice(line.getProduct());
                    double lineTotal = effectivePrice * line.getQuantity();

                    CartItemResponseDto dto = objectMapper.convertValue(line, CartItemResponseDto.class);
                    dto.setEffectivePrice(effectivePrice);
                    dto.setLineTotal(lineTotal);
                    return dto;
                })
                .collect(Collectors.toList());

        double cartTotal = items.stream().mapToDouble(CartItemResponseDto::getLineTotal).sum();

        return CartSummaryResponseDto.builder()
                .userId(userId)
                .items(items)
                .cartTotal(cartTotal)
                .build();
    }
}
