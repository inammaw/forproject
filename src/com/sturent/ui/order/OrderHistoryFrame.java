package com.sturent.ui.order;

import com.sturent.model.Order;
import com.sturent.service.OrderService;

import javax.swing.*;
import java.awt.*;

public class OrderHistoryFrame extends JFrame {
    private final int userId;
    private final OrderService service;
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public OrderHistoryFrame(int userId) { this(userId, new OrderService()); }

    public OrderHistoryFrame(int userId, OrderService service) {
        this.userId = userId; this.service = service;
        setTitle("Order History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); setLocationRelativeTo(null);
        buildUI(); loadOrders();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        JList<String> list = new JList<>(model);
        add(new JScrollPane(list), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadOrders());
        add(refresh, BorderLayout.SOUTH);
    }

    private void loadOrders() {
        model.clear();
        try {
            for (Order o : service.getOrdersForUser(userId))
                model.addElement("Order #" + o.getOrderId() + " | " + o.getStatus() + " | ₹" + o.getTotalAmount());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
