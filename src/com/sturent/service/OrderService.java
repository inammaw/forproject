package com.sturent.service;

import com.sturent.dao.OrderDAO;
import com.sturent.model.Order;
import com.sturent.model.OrderItem;

import java.sql.SQLException;
import java.util.List;

public class OrderService {
    private final OrderDAO orderDAO;

    public OrderService() { this(new OrderDAO()); }
    public OrderService(OrderDAO orderDAO) { this.orderDAO = orderDAO; }

    public int placeOrder(Order order) throws SQLException {
        if (order == null || order.getUserId() <= 0 || order.getItems().isEmpty())
            throw new IllegalArgumentException("Order must contain a valid user and at least one item.");

        double total = 0;
        for (OrderItem item : order.getItems()) {
            if (item.getQuantity() <= 0 || item.getItemId() <= 0)
                throw new IllegalArgumentException("Invalid order item.");
            total += item.getSubtotal();
        }
        order.setTotalAmount(total);
        if (order.getStatus() == null || order.getStatus().isBlank())
            order.setStatus("PENDING");

        return orderDAO.createOrder(order);
    }

    public Order getOrder(int orderId) throws SQLException { return orderDAO.findById(orderId); }
    public List<Order> getOrdersForUser(int userId) throws SQLException { return orderDAO.findByUserId(userId); }

    public boolean updateStatus(int orderId, String status) throws SQLException {
        if (status == null || status.isBlank()) throw new IllegalArgumentException("Status is required.");
        return orderDAO.updateStatus(orderId, status);
    }

    public boolean cancelOrder(int orderId) throws SQLException {
        return orderDAO.updateStatus(orderId, "CANCELLED");
    }
}
