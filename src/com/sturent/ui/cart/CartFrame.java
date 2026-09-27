package com.sturent.ui.cart;

import com.sturent.model.CartItem;
import com.sturent.service.CartService;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CartFrame extends JFrame {
    private final CartService cartService;
    private final OrderService orderService;
    private final int userId;
    private final JPanel itemsContainer = new JPanel();
    private final JLabel totalLabel = new JLabel("₹0.00");
    private final JLabel itemCountLabel = new JLabel("0 items in cart");

    public CartFrame(int userId) {
        this(userId, new CartService(), new OrderService());
    }

    public CartFrame(int userId, CartService cartService) {
        this(userId, cartService, new OrderService());
    }

    public CartFrame(int userId, CartService cartService, OrderService orderService) {
        this.userId = userId;
        this.cartService = cartService != null ? cartService : new CartService();
        this.orderService = orderService != null ? orderService : new OrderService();

        setTitle("StuRent - My Cart");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(960, 680);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadCart();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // 1. Top Brand Header
        JButton refreshBtn = StuRentTheme.createSecondaryButton("Refresh");
        refreshBtn.addActionListener(e -> loadCart());
        JPanel header = StuRentTheme.createHeader(refreshBtn);
        add(header, BorderLayout.NORTH);

        // 2. Main Content
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Title section
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel pageTitle = new JLabel("My Cart");
        pageTitle.setFont(StuRentTheme.FONT_TITLE);
        pageTitle.setForeground(StuRentTheme.TEXT_DARK);

        itemCountLabel.setFont(StuRentTheme.FONT_SUBTITLE);
        itemCountLabel.setForeground(StuRentTheme.TEXT_MUTED);

        titlePanel.add(pageTitle);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(itemCountLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // Center: Scrollable list of item cards
        itemsContainer.setLayout(new BoxLayout(itemsContainer, BoxLayout.Y_AXIS));
        itemsContainer.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(itemsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Right side: Order Summary Card
        JPanel rightSummary = createSummaryCard();
        mainPanel.add(rightSummary, BorderLayout.EAST);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JPanel createSummaryCard() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.setPreferredSize(new Dimension(300, 350));

        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Order Summary");
        title.setFont(StuRentTheme.FONT_CARD_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JSeparator sep = new JSeparator();
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setForeground(StuRentTheme.CARD_BORDER);

        JPanel rowTotal = new JPanel(new BorderLayout());
        rowTotal.setOpaque(false);
        JLabel lblTotal = new JLabel("Total Amount:");
        lblTotal.setFont(StuRentTheme.FONT_BOLD);
        lblTotal.setForeground(StuRentTheme.TEXT_DARK);

        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        totalLabel.setForeground(StuRentTheme.PRIMARY_GREEN);
        rowTotal.add(lblTotal, BorderLayout.WEST);
        rowTotal.add(totalLabel, BorderLayout.EAST);

        JLabel campusDelivery = new JLabel("\u2022 Campus Pickup / Delivery included");
        campusDelivery.setFont(StuRentTheme.FONT_SMALL);
        campusDelivery.setForeground(StuRentTheme.TEXT_MUTED);

        JButton checkoutBtn = StuRentTheme.createPrimaryButton("Proceed to Checkout");
        checkoutBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        checkoutBtn.addActionListener(e -> new CheckoutFrame(userId, cartService, orderService).setVisible(true));

        card.add(title);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(rowTotal);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(campusDelivery);
        card.add(Box.createVerticalGlue());
        card.add(checkoutBtn);

        wrapper.add(card, BorderLayout.NORTH);
        return wrapper;
    }

    public void loadCart() {
        itemsContainer.removeAll();
        double sum = 0.0;
        int count = 0;

        try {
            List<CartItem> items = cartService.getCart(userId);
            if (items == null || items.isEmpty()) {
                JPanel emptyCard = StuRentTheme.createCard();
                emptyCard.setLayout(new BoxLayout(emptyCard, BoxLayout.Y_AXIS));
                JLabel emptyLbl = new JLabel("Your cart is currently empty.");
                emptyLbl.setFont(StuRentTheme.FONT_CARD_TITLE);
                emptyLbl.setForeground(StuRentTheme.TEXT_MUTED);
                JLabel emptySub = new JLabel("Browse campus listings to find books, gear, and supplies!");
                emptySub.setFont(StuRentTheme.FONT_SMALL);
                emptySub.setForeground(StuRentTheme.TEXT_MUTED);
                emptyCard.add(emptyLbl);
                emptyCard.add(Box.createRigidArea(new Dimension(0, 6)));
                emptyCard.add(emptySub);
                itemsContainer.add(emptyCard);
            } else {
                for (CartItem item : items) {
                    sum += item.getSubtotal();
                    count += item.getQuantity();
                    itemsContainer.add(createCartItemCard(item));
                    itemsContainer.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }
            totalLabel.setText("₹" + String.format("%.2f", sum));
            itemCountLabel.setText(count + " items in your cart");
        } catch (Exception ex) {
            // Graceful UI Preview Fallback if DB is offline or access denied
            JPanel noticeCard = StuRentTheme.createCard();
            noticeCard.setLayout(new BorderLayout());
            noticeCard.setBackground(new Color(254, 243, 199));
            noticeCard.setBorder(BorderFactory.createLineBorder(new Color(253, 230, 138), 1));
            JLabel noticeText = new JLabel("<html><b>Demo Preview Mode:</b> Displaying sample items (Database: " + ex.getMessage() + ")</html>");
            noticeText.setFont(StuRentTheme.FONT_SMALL);
            noticeText.setForeground(new Color(180, 83, 9));
            noticeCard.add(noticeText, BorderLayout.CENTER);
            itemsContainer.add(noticeCard);
            itemsContainer.add(Box.createRigidArea(new Dimension(0, 10)));

            // Sample items for preview
            CartItem c1 = new CartItem(); c1.setCartItemId(1); c1.setItemId(101); c1.setQuantity(1); c1.setUnitPrice(450.0);
            CartItem c2 = new CartItem(); c2.setCartItemId(2); c2.setItemId(102); c2.setQuantity(2); c2.setUnitPrice(120.0);
            CartItem c3 = new CartItem(); c3.setCartItemId(3); c3.setItemId(103); c3.setQuantity(1); c3.setUnitPrice(799.0);

            itemsContainer.add(createCartItemCard(c1));
            itemsContainer.add(Box.createRigidArea(new Dimension(0, 12)));
            itemsContainer.add(createCartItemCard(c2));
            itemsContainer.add(Box.createRigidArea(new Dimension(0, 12)));
            itemsContainer.add(createCartItemCard(c3));

            totalLabel.setText("₹1489.00");
            itemCountLabel.setText("4 items in your cart (Demo Preview)");
        }

        itemsContainer.revalidate();
        itemsContainer.repaint();
    }

    private JPanel createCartItemCard(CartItem item) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout(16, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 95));

        // Left: Thumbnail Placeholder Box
        JPanel thumb = new JPanel(new GridBagLayout());
        thumb.setPreferredSize(new Dimension(75, 65));
        thumb.setBackground(new Color(241, 245, 249));
        thumb.setBorder(BorderFactory.createLineBorder(StuRentTheme.CARD_BORDER, 1));
        JLabel thumbText = new JLabel("Item #" + item.getItemId());
        thumbText.setFont(new Font("Segoe UI", Font.BOLD, 10));
        thumbText.setForeground(StuRentTheme.TEXT_MUTED);
        thumb.add(thumbText);

        // Center: Details
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        rowTop.setOpaque(false);
        JLabel name = new JLabel(getItemDisplayName(item.getItemId()));
        name.setFont(StuRentTheme.FONT_CARD_TITLE);
        name.setForeground(StuRentTheme.TEXT_DARK);
        JLabel badge = StuRentTheme.createBadge("SALE", StuRentTheme.BADGE_BLUE_BG, StuRentTheme.BADGE_BLUE_TEXT);
        rowTop.add(name);
        rowTop.add(badge);

        JLabel price = new JLabel("Unit Price: ₹" + String.format("%.2f", item.getUnitPrice()) + "  \u2022  Qty: " + item.getQuantity());
        price.setFont(StuRentTheme.FONT_SMALL);
        price.setForeground(StuRentTheme.TEXT_MUTED);

        JLabel subtotal = new JLabel("Subtotal: ₹" + String.format("%.2f", item.getSubtotal()));
        subtotal.setFont(StuRentTheme.FONT_PRICE);
        subtotal.setForeground(StuRentTheme.PRIMARY_GREEN);

        center.add(rowTop);
        center.add(Box.createRigidArea(new Dimension(0, 4)));
        center.add(price);
        center.add(Box.createRigidArea(new Dimension(0, 4)));
        center.add(subtotal);

        // Right: Remove Button
        JButton removeBtn = StuRentTheme.createDangerButton("Remove");
        removeBtn.addActionListener(e -> {
            try {
                cartService.removeFromCart(item.getCartItemId());
                loadCart();
            } catch (Exception ex) {
                showError(ex);
            }
        });

        card.add(thumb, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(removeBtn, BorderLayout.EAST);

        return card;
    }

    private String getItemDisplayName(int itemId) {
        switch (itemId) {
            case 1: return "test5 (Dumbbell Set 4x)";
            case 2: return "test test (Campus Electronics)";
            case 3: return "useless human (Desk Bobblehead)";
            case 4: return "Operating System Concepts (10th Ed)";
            case 5: return "Scientific Calculator FX-991EX";
            case 6: return "Ergonomic Study Desk Lamp";
            case 101: return "Database Engineering Textbook";
            case 102: return "Scientific Calculator FX-991ES";
            case 103: return "Dumbbell Set (4x 2.5kg)";
            default: return "Campus Listing #" + itemId;
        }
    }

    private void showError(Exception ex) {
        JOptionPane.showMessageDialog(this, ex.getMessage(), "Cart Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CartFrame(1).setVisible(true));
    }
}
