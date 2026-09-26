package com.sturent.ui.order;

import com.sturent.model.Order;
import com.sturent.service.OrderService;

import javax.swing.*;
import java.awt.*;

public class OrderStatusFrame extends JFrame {
    private final int orderId;
    private final OrderService service;
    private final JLabel status = new JLabel("Loading...", SwingConstants.CENTER);

    public OrderStatusFrame(int orderId) { this(orderId, new OrderService()); }

    public OrderStatusFrame(int orderId, OrderService service) {
        this.orderId = orderId; this.service = service;
        setTitle("Order Status");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 250); setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(status, BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadStatus());
        add(refresh, BorderLayout.SOUTH);
        loadStatus();
    }

    private void loadStatus() {
        try {
            Order o = service.getOrder(orderId);
            status.setText(o == null ? "Order not found" : "Order #" + orderId + " — " + o.getStatus());
        } catch (Exception ex) { status.setText(ex.getMessage()); }
    }
}
