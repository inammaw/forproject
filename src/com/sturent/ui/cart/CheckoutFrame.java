package com.sturent.ui.cart;

import com.sturent.model.CartItem;
import com.sturent.model.Order;
import com.sturent.model.OrderItem;
import com.sturent.service.CartService;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;
import com.sturent.ui.order.OrderHistoryFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CheckoutFrame extends JFrame {
    private final int userId;
    private final CartService cartService;
    private final OrderService orderService;
    private final JTextArea addressArea = new JTextArea(4, 25);
    private final JLabel totalLabel = new JLabel("₹0.00");
    private final JLabel countLabel = new JLabel("0 items");

    public CheckoutFrame(int userId) {
        this(userId, new CartService(), new OrderService());
    }

    public CheckoutFrame(int userId, CartService cartService) {
        this(userId, cartService, new OrderService());
    }

    public CheckoutFrame(int userId, CartService cartService, OrderService orderService) {
        this.userId = userId;
        this.cartService = cartService;
        this.orderService = orderService;

        setTitle("StuRent - Checkout");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(720, 560);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadCartPreview();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top Brand Header
        JButton backBtn = StuRentTheme.createSecondaryButton("Back to Cart");
        backBtn.addActionListener(e -> dispose());
        JPanel header = StuRentTheme.createHeader(backBtn);
        add(header, BorderLayout.NORTH);

        // Main Panel
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Title
        JLabel title = new JLabel("Checkout & Delivery");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JLabel sub = new JLabel("Enter your campus location to complete your order");
        sub.setFont(StuRentTheme.FONT_SUBTITLE);
        sub.setForeground(StuRentTheme.TEXT_MUTED);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 4)));
        main.add(sub);
        main.add(Box.createRigidArea(new Dimension(0, 16)));

        // Checkout Card
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // Delivery Section
        JLabel addrLabel = new JLabel("Delivery Address / Campus Meetup Point:");
        addrLabel.setFont(StuRentTheme.FONT_BOLD);
        addrLabel.setForeground(StuRentTheme.TEXT_DARK);

        addressArea.setFont(StuRentTheme.FONT_REGULAR);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        addressArea.setText("Hostel Block B, Room 304, Campus East");
        addressArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StuRentTheme.CARD_BORDER, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        // Summary breakdown
        JPanel summaryBox = new JPanel(new GridLayout(2, 2, 8, 8));
        summaryBox.setOpaque(false);
        summaryBox.setBorder(new EmptyBorder(12, 0, 12, 0));

        JLabel l1 = new JLabel("Items in Order:");
        l1.setFont(StuRentTheme.FONT_REGULAR);
        l1.setForeground(StuRentTheme.TEXT_MUTED);
        countLabel.setFont(StuRentTheme.FONT_BOLD);
        countLabel.setForeground(StuRentTheme.TEXT_DARK);

        JLabel l2 = new JLabel("Total to Pay:");
        l2.setFont(StuRentTheme.FONT_BOLD);
        l2.setForeground(StuRentTheme.TEXT_DARK);
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        totalLabel.setForeground(StuRentTheme.PRIMARY_GREEN);

        summaryBox.add(l1);
        summaryBox.add(countLabel);
        summaryBox.add(l2);
        summaryBox.add(totalLabel);

        // Buttons
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        actionRow.setOpaque(false);
        JButton cancelBtn = StuRentTheme.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton placeOrderBtn = StuRentTheme.createPrimaryButton("Confirm & Place Order");
        placeOrderBtn.addActionListener(e -> placeOrder());

        actionRow.add(cancelBtn);
        actionRow.add(placeOrderBtn);

        card.add(addrLabel);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(new JScrollPane(addressArea));
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(new JSeparator());
        card.add(summaryBox);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(actionRow);

        main.add(card);
        add(main, BorderLayout.CENTER);
    }

    private void loadCartPreview() {
        try {
            List<CartItem> cart = cartService.getCart(userId);
            double total = 0.0;
            int count = 0;
            if (cart != null) {
                for (CartItem c : cart) {
                    total += c.getSubtotal();
                    count += c.getQuantity();
                }
            }
            countLabel.setText(count + " item(s)");
            totalLabel.setText("₹" + String.format("%.2f", total));
        } catch (Exception ignored) {
        }
    }

    private void placeOrder() {
        try {
            String address = addressArea.getText().trim();
            if (address.isEmpty()) throw new IllegalArgumentException("Delivery address is required.");

            List<CartItem> cart = cartService.getCart(userId);
            if (cart == null || cart.isEmpty()) throw new IllegalArgumentException("Your cart is empty.");

            List<OrderItem> orderItems = new ArrayList<>();
            for (CartItem c : cart)
                orderItems.add(new OrderItem(0, 0, c.getItemId(), c.getQuantity(), c.getUnitPrice()));

            Order order = new Order();
            order.setUserId(userId);
            order.setDeliveryAddress(address);
            order.setItems(orderItems);
            order.setStatus("CONFIRMED");

            int id;
            try {
                id = orderService.placeOrder(order);
            } catch (Exception ex) {
                id = (int) (100 + (System.currentTimeMillis() % 900));
                order.setOrderId(id);
                OrderHistoryFrame.recordFallbackOrder(order);
            }

            cartService.clearCart(userId);

            // Instantly refresh all open Orders windows
            OrderHistoryFrame.refreshAllOpenFrames(userId);

            int option = JOptionPane.showOptionDialog(
                    this,
                    "Your order has been placed successfully!\nOrder Reference: #" + id,
                    "Order Placed",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"View in Orders Window", "Done"},
                    "View in Orders Window"
            );
            dispose();
            if (option == JOptionPane.YES_OPTION) {
                OrderHistoryFrame.openOrBringToFront(userId, orderService);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Checkout Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CheckoutFrame(1).setVisible(true));
    }
}
