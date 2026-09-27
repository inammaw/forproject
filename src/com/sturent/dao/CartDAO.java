package com.sturent.dao;

import com.sturent.config.DBConnection;
import com.sturent.model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public boolean add(CartItem item) throws SQLException {
        String checkSql = "SELECT cart_item_id, quantity FROM cart WHERE user_id = ? AND item_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement checkPs = con.prepareStatement(checkSql)) {
            checkPs.setInt(1, item.getUserId());
            checkPs.setInt(2, item.getItemId());
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next()) {
                    int existingId = rs.getInt("cart_item_id");
                    int newQty = rs.getInt("quantity") + item.getQuantity();
                    return updateQuantity(existingId, newQty);
                }
            }
        }

        String sql = "INSERT INTO cart (user_id, item_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, item.getUserId());
            ps.setInt(2, item.getItemId());
            ps.setInt(3, item.getQuantity());
            ps.setDouble(4, item.getUnitPrice());
            return ps.executeUpdate() > 0;
        }
    }

    public List<CartItem> findByUserId(int userId) throws SQLException {
        String sql = "SELECT cart_item_id, user_id, item_id, quantity, unit_price FROM cart WHERE user_id = ?";
        List<CartItem> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(map(rs));
            }
        }
        return result;
    }

    public CartItem findById(int cartItemId) throws SQLException {
        String sql = "SELECT cart_item_id, user_id, item_id, quantity, unit_price FROM cart WHERE cart_item_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    public boolean updateQuantity(int cartItemId, int quantity) throws SQLException {
        String sql = "UPDATE cart SET quantity = ? WHERE cart_item_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean remove(int cartItemId) throws SQLException {
        String sql = "DELETE FROM cart WHERE cart_item_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, cartItemId);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean clearUserCart(int userId) throws SQLException {
        String sql = "DELETE FROM cart WHERE user_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() > 0;
        }
    }

    private CartItem map(ResultSet rs) throws SQLException {
        return new CartItem(
            rs.getInt("cart_item_id"),
            rs.getInt("user_id"),
            rs.getInt("item_id"),
            rs.getInt("quantity"),
            rs.getDouble("unit_price")
        );
    }
}
