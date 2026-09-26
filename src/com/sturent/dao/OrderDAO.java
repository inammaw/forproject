package com.sturent.dao;

import com.sturent.config.DBConnection;
import com.sturent.model.Order;
import com.sturent.model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int createOrder(Order order) throws SQLException {
        String orderSql = "INSERT INTO orders (user_id, total_amount, status, delivery_address) VALUES (?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                int orderId;
                try (PreparedStatement ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, order.getUserId());
                    ps.setDouble(2, order.getTotalAmount());
                    ps.setString(3, order.getStatus() == null ? "PENDING" : order.getStatus());
                    ps.setString(4, order.getDeliveryAddress());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) throw new SQLException("Order ID was not generated.");
                        orderId = keys.getInt(1);
                    }
                }

                try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                    for (OrderItem item : order.getItems()) {
                        ps.setInt(1, orderId);
                        ps.setInt(2, item.getItemId());
                        ps.setInt(3, item.getQuantity());
                        ps.setDouble(4, item.getUnitPrice());
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();
                order.setOrderId(orderId);
                return orderId;
            } catch (SQLException ex) {
                con.rollback();
                throw ex;
            } finally {
                con.setAutoCommit(true);
            }
        }
    }

    public Order findById(int orderId) throws SQLException {
        String sql = "SELECT order_id, user_id, total_amount, status, delivery_address, created_at FROM orders WHERE order_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Order order = mapOrder(rs);
                order.setItems(findItems(con, orderId));
                return order;
            }
        }
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT order_id, user_id, total_amount, status, delivery_address, created_at FROM orders WHERE user_id = ? ORDER BY created_at DESC";
        List<Order> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItems(con, order.getOrderId()));
                    result.add(order);
                }
            }
        }
        return result;
    }

    public boolean updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status = ? WHERE order_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int orderId) throws SQLException {
        String sql = "DELETE FROM orders WHERE order_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    private List<OrderItem> findItems(Connection con, int orderId) throws SQLException {
        String sql = "SELECT order_item_id, order_id, item_id, quantity, unit_price FROM order_items WHERE order_id = ?";
        List<OrderItem> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(new OrderItem(
                        rs.getInt("order_item_id"),
                        rs.getInt("order_id"),
                        rs.getInt("item_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price")
                    ));
                }
            }
        }
        return result;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("created_at");
        return new Order(
            rs.getInt("order_id"),
            rs.getInt("user_id"),
            rs.getDouble("total_amount"),
            rs.getString("status"),
            rs.getString("delivery_address"),
            timestamp == null ? null : timestamp.toLocalDateTime()
        );
    }
}
