package com.sturent.model;

public class CartItem {
    private int cartItemId;
    private int userId;
    private int itemId;
    private int quantity;
    private double unitPrice;

    public CartItem() {}

    public CartItem(int cartItemId, int userId, int itemId, int quantity, double unitPrice) {
        this.cartItemId = cartItemId;
        this.userId = userId;
        this.itemId = itemId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getSubtotal() { return unitPrice * quantity; }
}
