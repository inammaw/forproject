package com.sturent.ui.order;

import com.sturent.model.Order;
import com.sturent.model.OrderItem;
import com.sturent.service.OrderService;

import javax.swing.*;

public class OrderDetailsFrame extends JFrame {
    private final int orderId;
    private final OrderService service;
    private final JTextArea details = new JTextArea();

    public OrderDetailsFrame(int orderId) { this(orderId, new OrderService()); }

    public OrderDetailsFrame(int orderId, OrderService service) {
        this.orderId = orderId; this.service = service;
        setTitle("Order Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 500); setLocationRelativeTo(null);
        details.setEditable(false);
        add(new JScrollPane(details));
        loadOrder();
    }

    private void loadOrder() {
        try {
            Order o = service.getOrder(orderId);
            if (o == null) { details.setText("Order not found."); return; }
            StringBuilder sb = new StringBuilder();
            sb.append("Order #").append(o.getOrderId()).append("\n");
            sb.append("Status: ").append(o.getStatus()).append("\n");
            sb.append("Total: ₹").append(o.getTotalAmount()).append("\n");
            sb.append("Address: ").append(o.getDeliveryAddress()).append("\n\nItems:\n");
            for (OrderItem item : o.getItems())
                sb.append("Item #").append(item.getItemId()).append(" x ")
                  .append(item.getQuantity()).append(" @ ₹").append(item.getUnitPrice()).append("\n");
            details.setText(sb.toString());
        } catch (Exception ex) { details.setText(ex.getMessage()); }
    }
}
