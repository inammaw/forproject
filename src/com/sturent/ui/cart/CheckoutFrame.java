package com.sturent.ui.cart;

import com.sturent.model.CartItem;
import com.sturent.model.Order;
import com.sturent.model.OrderItem;
import com.sturent.service.CartService;
import com.sturent.service.OrderService;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutFrame extends JFrame {
    private final int userId;
    private final CartService cartService;
    private final OrderService orderService;
    private final JTextArea addressArea = new JTextArea(4, 30);

    public CheckoutFrame(int userId) {
        this(userId, new CartService(), new OrderService());
    }

    public CheckoutFrame(int userId, CartService cartService, OrderService orderService) {
        this.userId = userId;
        this.cartService = cartService;
        this.orderService = orderService;
        setTitle("Checkout");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350);
        setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("Checkout", SwingConstants.CENTER), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(5, 5));
        center.add(new JLabel("Delivery address:"), BorderLayout.NORTH);
        center.add(new JScrollPane(addressArea), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);

        JButton placeOrder = new JButton("Place Order");
        placeOrder.addActionListener(e -> placeOrder());
        JPanel bottom = new JPanel();
        bottom.add(placeOrder);
        add(bottom, BorderLayout.SOUTH);
    }

    private void placeOrder() {
        try {
            String address = addressArea.getText().trim();
            if (address.isEmpty()) throw new IllegalArgumentException("Delivery address is required.");

            List<CartItem> cart = cartService.getCart(userId);
            if (cart.isEmpty()) throw new IllegalArgumentException("Your cart is empty.");

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem c : cart)
                orderItems.add(new OrderItem(0, 0, c.getItemId(), c.getQuantity(), c.getUnitPrice()));

            Order order = new Order();
            order.setUserId(userId);
            order.setDeliveryAddress(address);
            order.setItems(orderItems);
            order.setStatus("PENDING");

            int id = orderService.placeOrder(order);
            cartService.clearCart(userId);
            JOptionPane.showMessageDialog(this, "Order placed successfully. Order #" + id);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Checkout Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
