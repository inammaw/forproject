package com.sturent.service;

import com.sturent.dao.CartDAO;
import com.sturent.model.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartService {
    private final CartDAO cartDAO;

    public CartService() { this(new CartDAO()); }
    public CartService(CartDAO cartDAO) { this.cartDAO = cartDAO; }

    public boolean addToCart(CartItem item) throws SQLException {
        if (item == null || item.getUserId() <= 0 || item.getItemId() <= 0 || item.getQuantity() <= 0)
            throw new IllegalArgumentException("Invalid cart item.");
        return cartDAO.add(item);
    }

    public List<CartItem> getCart(int userId) throws SQLException {
        return cartDAO.findByUserId(userId);
    }

    public boolean updateQuantity(int cartItemId, int quantity) throws SQLException {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be greater than zero.");
        return cartDAO.updateQuantity(cartItemId, quantity);
    }

    public boolean removeFromCart(int cartItemId) throws SQLException {
        return cartDAO.remove(cartItemId);
    }

    public boolean clearCart(int userId) throws SQLException {
        return cartDAO.clearUserCart(userId);
    }
}
