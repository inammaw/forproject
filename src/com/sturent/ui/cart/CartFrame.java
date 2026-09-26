package com.sturent.ui.cart;

import com.sturent.model.CartItem;
import com.sturent.service.CartService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CartFrame extends JFrame {
    private final CartService cartService;
    private final int userId;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();
    private final JList<String> cartList = new JList<>(listModel);

    public CartFrame(int userId) {
        this(userId, new CartService());
    }

    public CartFrame(int userId, CartService cartService) {
        this.userId = userId;
        this.cartService = cartService;
        setTitle("My Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 550);
        setLocationRelativeTo(null);
        buildUI();
        loadCart();
    }

    private void buildUI() {
        setLayout(new BorderLayout(10, 10));
        add(new JLabel("My Cart", SwingConstants.CENTER), BorderLayout.NORTH);
        add(new JScrollPane(cartList), BorderLayout.CENTER);

        JPanel actions = new JPanel();
        JButton refresh = new JButton("Refresh");
        JButton remove = new JButton("Remove Selected");
        JButton checkout = new JButton("Checkout");
        actions.add(refresh);
        actions.add(remove);
        actions.add(checkout);
        add(actions, BorderLayout.SOUTH);

        refresh.addActionListener(e -> loadCart());
        remove.addActionListener(e -> removeSelected());
        checkout.addActionListener(e -> new CheckoutFrame(userId).setVisible(true));
    }

    private void loadCart() {
        listModel.clear();
        try {
            List<CartItem> items = cartService.getCart(userId);
            for (CartItem item : items)
                listModel.addElement("Cart #" + item.getCartItemId() + " | Item #" + item.getItemId()
                        + " | Qty: " + item.getQuantity() + " | ₹" + item.getSubtotal());
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void removeSelected() {
        String selected = cartList.getSelectedValue();
        if (selected == null) return;
        try {
            int id = Integer.parseInt(selected.substring(selected.indexOf('#') + 1, selected.indexOf(" |")));
            cartService.removeFromCart(id);
            loadCart();
        } catch (Exception ex) {
            showError(ex);
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
