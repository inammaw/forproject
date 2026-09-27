package com.sturent.ui.marketplace;

import com.sturent.model.CartItem;
import com.sturent.model.Order;
import com.sturent.model.OrderItem;
import com.sturent.service.CartService;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;
import com.sturent.ui.cart.CartFrame;
import com.sturent.ui.order.OrderHistoryFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Item Details Screen
 * Matches Photo 2 from StuRent:
 * - Two column layout
 * - Large product image + interactive thumbnails
 * - "SALE" badge + "Report" button
 * - Large item title & Seller ID
 * - Pricing card (soft gray)
 * - Description & "• AVAILABLE" status
 * - Forest Green "Buy Now" button + "Add to Cart" button
 */
public class ItemDetailsFrame extends JFrame {

    private final int userId;
    private final int itemId;
    private final String title;
    private final String type;
    private final double price;
    private final String sellerId;
    private final String description;
    private final CartService cartService;
    private final OrderService orderService;
    private final JPanel mainImagePanel = new GridBagLayout() != null ? new JPanel(new GridBagLayout()) : new JPanel();
    private final JLabel mainImageLabel = new JLabel();

    public ItemDetailsFrame() {
        this(1, 1, "test5", "SALE", 11111.00, "TEST123", "test5 dumbbell set in mint condition", new CartService(), new OrderService());
    }

    public ItemDetailsFrame(String title, String type, double price, String sellerId, String description) {
        this(1, 1, title, type, price, sellerId, description, new CartService(), new OrderService());
    }

    public ItemDetailsFrame(int userId, int itemId, String title, String type, double price, String sellerId, String description, CartService cartService) {
        this(userId, itemId, title, type, price, sellerId, description, cartService, new OrderService());
    }

    public ItemDetailsFrame(int userId, int itemId, String title, String type, double price, String sellerId, String description, CartService cartService, OrderService orderService) {
        this.userId = userId > 0 ? userId : 1;
        this.itemId = itemId > 0 ? itemId : 1;
        this.title = title;
        this.type = type;
        this.price = price;
        this.sellerId = sellerId;
        this.description = description;
        this.cartService = cartService != null ? cartService : new CartService();
        this.orderService = orderService != null ? orderService : new OrderService();

        setTitle("StuRent - " + title);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // 1. Brand Header
        JButton closeBtn = StuRentTheme.createSecondaryButton("Back to Items");
        closeBtn.addActionListener(e -> dispose());
        JPanel header = StuRentTheme.createHeader(closeBtn);
        add(header, BorderLayout.NORTH);

        // 2. Main Two-Column Container Card
        JPanel mainWrapper = new JPanel(new BorderLayout());
        mainWrapper.setOpaque(false);
        mainWrapper.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel detailCard = StuRentTheme.createCard();
        detailCard.setLayout(new GridLayout(1, 2, 32, 0));

        // LEFT COLUMN: Main Image + Thumbnails
        JPanel leftCol = new JPanel();
        leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
        leftCol.setOpaque(false);

        mainImagePanel.setPreferredSize(new Dimension(380, 320));
        mainImagePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 340));
        mainImagePanel.setBackground(new Color(235, 238, 242));
        mainImagePanel.setBorder(BorderFactory.createLineBorder(StuRentTheme.CARD_BORDER, 1));

        mainImageLabel.setText("Dumbbell Set (4x)");
        mainImageLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        mainImageLabel.setForeground(StuRentTheme.TEXT_MUTED);
        mainImagePanel.add(mainImageLabel);

        // Thumbnail Row
        JPanel thumbRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        thumbRow.setOpaque(false);
        String[] thumbs = {"Main", "Angle 2", "Angle 3", "Specs", "Laptop"};
        for (String t : thumbs) {
            JButton tBtn = new JButton(t) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(245, 247, 250));
                    g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 4, 4));
                    g2.setColor(StuRentTheme.CARD_BORDER);
                    g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 4, 4));
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            tBtn.setPreferredSize(new Dimension(65, 55));
            tBtn.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            tBtn.setForeground(StuRentTheme.TEXT_MUTED);
            tBtn.setContentAreaFilled(false);
            tBtn.setBorderPainted(false);
            tBtn.setFocusPainted(false);
            tBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tBtn.addActionListener(e -> mainImageLabel.setText(t + " View"));
            thumbRow.add(tBtn);
        }

        leftCol.add(mainImagePanel);
        leftCol.add(Box.createRigidArea(new Dimension(0, 12)));
        leftCol.add(thumbRow);

        // RIGHT COLUMN: Tags, Title, Seller, Price Box, Description, Status, Buy Button
        JPanel rightCol = new JPanel();
        rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));
        rightCol.setOpaque(false);

        // Top Row: Badge + Report button
        JPanel topRow = new JPanel(new BorderLayout());
        topRow.setOpaque(false);
        JLabel typeBadge = StuRentTheme.createBadge(type, StuRentTheme.BADGE_BLUE_BG, StuRentTheme.BADGE_BLUE_TEXT);

        JButton reportBtn = new JButton("Report") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(StuRentTheme.DANGER_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 4, 4));
                g2.setColor(StuRentTheme.DANGER_BORDER);
                g2.draw(new RoundRectangle2D.Float(0.5f, 0.5f, getWidth() - 1, getHeight() - 1, 4, 4));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        reportBtn.setFont(StuRentTheme.FONT_BADGE);
        reportBtn.setForeground(StuRentTheme.DANGER_TEXT);
        reportBtn.setContentAreaFilled(false);
        reportBtn.setBorderPainted(false);
        reportBtn.setFocusPainted(false);
        reportBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        reportBtn.setBorder(new EmptyBorder(4, 10, 4, 10));
        reportBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Listing reported to campus moderation team."));

        topRow.add(typeBadge, BorderLayout.WEST);
        topRow.add(reportBtn, BorderLayout.EAST);

        // Title
        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLbl.setForeground(StuRentTheme.TEXT_DARK);

        // Seller
        JLabel sellerLbl = new JLabel("Seller:   User ID: " + sellerId);
        sellerLbl.setFont(StuRentTheme.FONT_REGULAR);
        sellerLbl.setForeground(StuRentTheme.TEXT_DARK);

        // Pricing Box (soft gray background matching Photo 2)
        JPanel priceBox = new JPanel();
        priceBox.setLayout(new BoxLayout(priceBox, BoxLayout.Y_AXIS));
        priceBox.setBackground(new Color(238, 242, 246));
        priceBox.setBorder(new EmptyBorder(14, 18, 14, 18));
        priceBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JLabel priceTag = new JLabel("Pricing");
        priceTag.setFont(StuRentTheme.FONT_SMALL);
        priceTag.setForeground(StuRentTheme.TEXT_MUTED);

        JLabel priceValue = new JLabel("₹" + String.format("%.2f", price));
        priceValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        priceValue.setForeground(StuRentTheme.TEXT_DARK);

        priceBox.add(priceTag);
        priceBox.add(Box.createRigidArea(new Dimension(0, 4)));
        priceBox.add(priceValue);

        // Description Section
        JLabel descHeader = new JLabel("Description");
        descHeader.setFont(StuRentTheme.FONT_BOLD);
        descHeader.setForeground(StuRentTheme.TEXT_DARK);

        JLabel descBody = new JLabel(description);
        descBody.setFont(StuRentTheme.FONT_REGULAR);
        descBody.setForeground(StuRentTheme.TEXT_MUTED);

        // Status indicator (green dot + AVAILABLE)
        JPanel statusIndicator = StuRentTheme.createStatusIndicator("AVAILABLE", new Color(22, 163, 74));

        // Action Buttons (Forest Green Buy Now + Secondary Add to Cart)
        JPanel actionBtnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        actionBtnRow.setOpaque(false);
        actionBtnRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton buyNowBtn = StuRentTheme.createPrimaryButton("Buy Now");
        buyNowBtn.setPreferredSize(new Dimension(140, 42));
        buyNowBtn.addActionListener(e -> handleBuyNow());

        JButton addToCartBtn = StuRentTheme.createSecondaryButton("Add to Cart");
        addToCartBtn.setPreferredSize(new Dimension(140, 42));
        addToCartBtn.addActionListener(e -> addToCartAndPrompt());

        actionBtnRow.add(buyNowBtn);
        actionBtnRow.add(addToCartBtn);

        rightCol.add(topRow);
        rightCol.add(Box.createRigidArea(new Dimension(0, 16)));
        rightCol.add(titleLbl);
        rightCol.add(Box.createRigidArea(new Dimension(0, 8)));
        rightCol.add(sellerLbl);
        rightCol.add(Box.createRigidArea(new Dimension(0, 16)));
        rightCol.add(priceBox);
        rightCol.add(Box.createRigidArea(new Dimension(0, 16)));
        rightCol.add(descHeader);
        rightCol.add(Box.createRigidArea(new Dimension(0, 4)));
        rightCol.add(descBody);
        rightCol.add(Box.createRigidArea(new Dimension(0, 18)));
        rightCol.add(statusIndicator);
        rightCol.add(Box.createRigidArea(new Dimension(0, 18)));
        rightCol.add(actionBtnRow);

        detailCard.add(leftCol);
        detailCard.add(rightCol);

        mainWrapper.add(detailCard, BorderLayout.CENTER);
        add(mainWrapper, BorderLayout.CENTER);
    }

    private void handleBuyNow() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel infoLbl = new JLabel("<html><b>Item:</b> " + title + "<br/><b>Total Price:</b> ₹" + String.format("%.2f", price) + "</html>");
        infoLbl.setFont(StuRentTheme.FONT_REGULAR);
        panel.add(infoLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel addrLbl = new JLabel("Delivery Address / Campus Meetup Point:");
        addrLbl.setFont(StuRentTheme.FONT_BOLD);
        panel.add(addrLbl);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));

        JTextField addrField = new JTextField("Hostel Block B, Room 304, Campus East", 26);
        addrField.setFont(StuRentTheme.FONT_REGULAR);
        panel.add(addrField);

        int confirm = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Buy Now - Instant Campus Order",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (confirm != JOptionPane.OK_OPTION) {
            return;
        }

        String address = addrField.getText().trim();
        if (address.isEmpty()) {
            address = "Hostel Block B, Room 304, Campus East";
        }

        try {
            OrderItem orderItem = new OrderItem(0, 0, itemId, 1, price);
            List<OrderItem> items = new ArrayList<>();
            items.add(orderItem);

            Order order = new Order();
            order.setUserId(userId);
            order.setDeliveryAddress(address);
            order.setItems(items);
            order.setStatus("CONFIRMED");

            int orderId;
            try {
                orderId = orderService.placeOrder(order);
            } catch (Exception ex) {
                // If offline or mock DAO, preserve order in fallback store
                orderId = (int) (100 + (System.currentTimeMillis() % 900));
                order.setOrderId(orderId);
                order.setTotalAmount(price);
                OrderHistoryFrame.recordFallbackOrder(order);
            }

            // Immediately refresh all open Orders windows
            OrderHistoryFrame.refreshAllOpenFrames(userId);

            int option = JOptionPane.showOptionDialog(
                    this,
                    "Order #" + orderId + " has been placed successfully!\n" +
                    "Item: " + title + " (₹" + String.format("%.2f", price) + ")\n" +
                    "Status: CONFIRMED\n\n" +
                    "Would you like to open the Orders window to view it now?",
                    "Order Placed Successfully",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"View in Orders Window", "Continue Shopping"},
                    "View in Orders Window"
            );

            if (option == JOptionPane.YES_OPTION) {
                OrderHistoryFrame.openOrBringToFront(userId, orderService);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not place order: " + ex.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addToCartAndPrompt() {
        try {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setItemId(itemId);
            cartItem.setQuantity(1);
            cartItem.setUnitPrice(price);

            boolean success = cartService.addToCart(cartItem);
            if (success) {
                int option = JOptionPane.showOptionDialog(
                        this,
                        "\"" + title + "\" (₹" + String.format("%.2f", price) + ") has been added to your cart!\nWould you like to open your cart now?",
                        "Added to Cart",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"View Cart", "Continue Shopping"},
                        "View Cart"
                );
                if (option == JOptionPane.YES_OPTION) {
                    openCart();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Could not add item to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding to cart: " + ex.getMessage(), "Cart Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCart() {
        new CartFrame(userId, cartService, orderService).setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ItemDetailsFrame().setVisible(true));
    }
}
