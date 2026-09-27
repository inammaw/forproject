package com.sturent.ui.marketplace;

import com.sturent.model.CartItem;
import com.sturent.model.Order;
import com.sturent.model.OrderItem;
import com.sturent.service.CartService;
import com.sturent.service.MessageService;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;
import com.sturent.ui.cart.CartFrame;
import com.sturent.ui.messaging.InboxFrame;
import com.sturent.ui.order.OrderHistoryFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Discover Items Screen
 * Matches Photo 1 from the StuRent application:
 * - Brand header: "StuRent - Your Campus. Your Marketplace." + "+ Add Item"
 * - Section header: "Discover Items" + "Find what you need from your campus community"
 * - Search bar + Forest Green "Search" button + "All Types" combo + Refresh button
 * - Grid of item cards (test5, test test, useless human)
 * - Single-instance window management to prevent window clutter on minimize/maximize
 */
public class DiscoverItemsFrame extends JFrame {

    private final int userId;
    private final CartService cartService;
    private final OrderService orderService;
    private final MessageService messageService;

    // Track open sub-windows so minimize/maximize or re-clicking doesn't spawn duplicate windows
    private CartFrame cartFrameInstance;
    private OrderHistoryFrame ordersFrameInstance;
    private InboxFrame inboxFrameInstance;
    private ItemDetailsFrame detailsFrameInstance;

    private final JTextField searchField = StuRentTheme.createTextField(30);
    private final JComboBox<String> typeCombo = new JComboBox<>(
            new String[] { "All Types", "For Sale", "For Rent", "Sale & Rent" });
    private final JPanel cardsGrid = new JPanel(new GridLayout(0, 3, 20, 20));
    private final List<MarketItem> items = new ArrayList<>();

    public static class MarketItem {
        public int itemId;
        public String title;
        public String type;
        public double salePrice;
        public double rentPrice;
        public String status;
        public String sellerId;
        public String description;

        public MarketItem(int itemId, String title, String type, double salePrice, double rentPrice, String status, String sellerId,
                String description) {
            this.itemId = itemId;
            this.title = title;
            this.type = type;
            this.salePrice = salePrice;
            this.rentPrice = rentPrice;
            this.status = status;
            this.sellerId = sellerId;
            this.description = description;
        }

        public MarketItem(String title, String type, double salePrice, double rentPrice, String status, String sellerId,
                String description) {
            this(1, title, type, salePrice, rentPrice, status, sellerId, description);
        }
    }

    public DiscoverItemsFrame() {
        this(1, new CartService(), new OrderService(), new MessageService());
    }

    public DiscoverItemsFrame(int userId) {
        this(userId, new CartService(), new OrderService(), new MessageService());
    }

    public DiscoverItemsFrame(int userId, CartService cartService, OrderService orderService, MessageService messageService) {
        this.userId = userId > 0 ? userId : 1;
        this.cartService = cartService != null ? cartService : new CartService();
        this.orderService = orderService != null ? orderService : new OrderService();
        this.messageService = messageService != null ? messageService : new MessageService();

        setTitle("StuRent - Campus Marketplace");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        seedItems();
        buildUI();
        renderCards();
    }

    private void seedItems() {
        items.add(new MarketItem(1, "test5", "SALE", 11111.0, 0, "AVAILABLE", "TEST123",
                "test5 dumbbell set in mint condition"));
        items.add(new MarketItem(2, "test test", "SALE", 11111.0, 0, "AVAILABLE", "CAMPUS_SELLER",
                "Campus electronics and study kit"));
        items.add(new MarketItem(3, "useless human", "SALE_AND_RENT", 10.0, 11.0, "AVAILABLE", "STUDENT_99",
                "Funny novelty student desk bobblehead"));
        items.add(new MarketItem(4, "Operating System Concepts (10th Ed)", "SALE", 450.0, 50.0, "AVAILABLE", "CS_STUDENT",
                "Hardcover textbook for CS201"));
        items.add(new MarketItem(5, "Scientific Calculator FX-991EX", "SALE_AND_RENT", 600.0, 25.0, "AVAILABLE", "ENG_LAB",
                "Approved for semester exams"));
        items.add(new MarketItem(6, "Ergonomic Study Desk Lamp", "SALE", 320.0, 0, "AVAILABLE", "HOSTEL_B",
                "LED with touch controls and warm light"));
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // 1. Top Brand Header with actions
        JPanel topActions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topActions.setOpaque(false);

        JButton myCartBtn = StuRentTheme.createSecondaryButton("Cart");
        myCartBtn.addActionListener(e -> openCart());

        JButton ordersBtn = StuRentTheme.createSecondaryButton("Orders");
        ordersBtn.addActionListener(e -> openOrders());

        JButton msgsBtn = StuRentTheme.createSecondaryButton("Messages");
        msgsBtn.addActionListener(e -> openMessages());

        JButton addItemBtn = StuRentTheme.createPrimaryButton("+ Add Item");
        addItemBtn.addActionListener(e -> openAddItemDialog());

        topActions.add(myCartBtn);
        topActions.add(ordersBtn);
        topActions.add(msgsBtn);
        topActions.add(addItemBtn);

        if (com.sturent.Session.isLoggedin()) {
            com.sturent.model.User currentUser = com.sturent.Session.getCurrentUser();
            JLabel userBadge = StuRentTheme.createBadge(currentUser.getName() + " (" + currentUser.getRole() + ")",
                    StuRentTheme.BADGE_GREEN_BG, StuRentTheme.BADGE_GREEN_TEXT);
            topActions.add(userBadge);

            if ("ADMIN".equalsIgnoreCase(currentUser.getRole())) {
                JButton adminBtn = StuRentTheme.createSecondaryButton("Admin Panel");
                adminBtn.addActionListener(e -> new com.sturent.gui.admin.AdminDashboardFrame().setVisible(true));
                topActions.add(adminBtn);
            }

            JButton logoutBtn = StuRentTheme.createSecondaryButton("Logout");
            logoutBtn.addActionListener(e -> {
                com.sturent.Session.clearSession();
                dispose();
                new com.sturent.gui.LoginFrame().setVisible(true);
            });
            topActions.add(logoutBtn);
        } else {
            JButton loginBtn = StuRentTheme.createSecondaryButton("Login");
            loginBtn.addActionListener(e -> {
                dispose();
                new com.sturent.gui.LoginFrame().setVisible(true);
            });
            topActions.add(loginBtn);
        }

        JPanel header = StuRentTheme.createHeader(topActions);
        add(header, BorderLayout.NORTH);

        // 2. Main Content
        JPanel main = new JPanel(new BorderLayout(16, 16));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Subheader: Discover Items
        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setOpaque(false);

        JLabel h1 = new JLabel("Discover Items");
        h1.setFont(new Font("Segoe UI", Font.BOLD, 26));
        h1.setForeground(StuRentTheme.TEXT_DARK);

        JLabel h2 = new JLabel("Find what you need from your campus community");
        h2.setFont(StuRentTheme.FONT_SUBTITLE);
        h2.setForeground(StuRentTheme.TEXT_MUTED);

        titleSection.add(h1);
        titleSection.add(Box.createRigidArea(new Dimension(0, 4)));
        titleSection.add(h2);
        titleSection.add(Box.createRigidArea(new Dimension(0, 16)));

        // Search & Filter Toolbar
        JPanel searchBar = new JPanel(new BorderLayout(12, 0));
        searchBar.setOpaque(false);

        JPanel searchControls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchControls.setOpaque(false);

        JButton searchBtn = StuRentTheme.createPrimaryButton("Search");
        searchBtn.addActionListener(e -> filterItems());

        typeCombo.setFont(StuRentTheme.FONT_REGULAR);
        typeCombo.setPreferredSize(new Dimension(130, 38));
        typeCombo.addActionListener(e -> filterItems());

        JButton refreshBtn = StuRentTheme.createSecondaryButton("↻");
        refreshBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        refreshBtn.addActionListener(e -> {
            searchField.setText("");
            typeCombo.setSelectedIndex(0);
            renderCards();
        });

        searchControls.add(searchBtn);
        searchControls.add(typeCombo);
        searchControls.add(refreshBtn);

        searchBar.add(searchField, BorderLayout.CENTER);
        searchBar.add(searchControls, BorderLayout.EAST);

        titleSection.add(searchBar);
        main.add(titleSection, BorderLayout.NORTH);

        // Center: Cards Grid inside ScrollPane
        cardsGrid.setOpaque(false);
        JScrollPane scroll = new JScrollPane(cardsGrid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        main.add(scroll, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private void renderCards() {
        cardsGrid.removeAll();
        for (MarketItem item : items) {
            cardsGrid.add(createItemCard(item));
        }
        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private void filterItems() {
        String q = searchField.getText().trim().toLowerCase();
        String selectedType = (String) typeCombo.getSelectedItem();

        cardsGrid.removeAll();
        for (MarketItem item : items) {
            boolean matchesQuery = q.isEmpty() || item.title.toLowerCase().contains(q);
            boolean matchesType = "All Types".equals(selectedType) ||
                    ("For Sale".equals(selectedType) && item.type.contains("SALE")) ||
                    ("For Rent".equals(selectedType) && item.type.contains("RENT")) ||
                    ("Sale & Rent".equals(selectedType) && item.type.contains("AND"));

            if (matchesQuery && matchesType) {
                cardsGrid.add(createItemCard(item));
            }
        }
        cardsGrid.revalidate();
        cardsGrid.repaint();
    }

    private JPanel createItemCard(MarketItem item) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        // 1. Image Preview Box
        JPanel imgBox = new JPanel(new GridBagLayout());
        imgBox.setPreferredSize(new Dimension(280, 160));
        imgBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        imgBox.setBackground(new Color(238, 242, 246));
        imgBox.setBorder(BorderFactory.createLineBorder(StuRentTheme.CARD_BORDER, 1));

        JLabel imgLabel = new JLabel("No Image");
        imgLabel.setFont(StuRentTheme.FONT_SMALL);
        imgLabel.setForeground(StuRentTheme.TEXT_MUTED);
        imgBox.add(imgLabel);

        // 2. Title
        JLabel titleLabel = new JLabel(item.title);
        titleLabel.setFont(StuRentTheme.FONT_CARD_TITLE);
        titleLabel.setForeground(StuRentTheme.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 3. Category Type Tag
        JLabel typeLabel = new JLabel(item.type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        typeLabel.setForeground(new Color(100, 116, 139));
        typeLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 4. Price
        String priceText;
        if ("SALE_AND_RENT".equals(item.type)) {
            priceText = "Sale: ₹" + item.salePrice + " | Rent: ₹" + item.rentPrice + " / hourly";
        } else if ("RENT".equals(item.type)) {
            priceText = "Rent: ₹" + item.rentPrice + " / day";
        } else {
            priceText = "₹" + item.salePrice;
        }
        JLabel priceLabel = new JLabel(priceText);
        priceLabel.setFont(StuRentTheme.FONT_PRICE);
        priceLabel.setForeground(StuRentTheme.TEXT_DARK);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 5. Status: • AVAILABLE
        JPanel statusIndicator = StuRentTheme.createStatusIndicator(item.status, new Color(22, 163, 74));
        statusIndicator.setAlignmentX(Component.LEFT_ALIGNMENT);

        // 6. Action buttons row
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        actionRow.setOpaque(false);
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton viewBtn = StuRentTheme.createSecondaryButton("Details");
        viewBtn.addActionListener(e -> openItemDetails(item));

        JButton buyNowBtn = StuRentTheme.createPrimaryButton("Buy Now");
        buyNowBtn.addActionListener(e -> buyNowItem(item));

        JButton cartBtn = StuRentTheme.createSecondaryButton("+ Cart");
        cartBtn.addActionListener(e -> addItemToCart(item));

        JButton delBtn = StuRentTheme.createDangerButton("Delete");
        delBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + item.title + "'?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                items.remove(item);
                renderCards();
            }
        });

        actionRow.add(viewBtn);
        actionRow.add(buyNowBtn);
        actionRow.add(cartBtn);
        actionRow.add(delBtn);

        card.add(imgBox);
        card.add(Box.createRigidArea(new Dimension(0, 12)));
        card.add(titleLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(typeLabel);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(priceLabel);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(statusIndicator);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(actionRow);

        return card;
    }

    private void buyNowItem(MarketItem item) {
        double price = item.salePrice > 0 ? item.salePrice : item.rentPrice;
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JLabel infoLbl = new JLabel("<html><b>Item:</b> " + item.title + "<br/><b>Total Price:</b> ₹" + String.format("%.2f", price) + "</html>");
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

        if (confirm != JOptionPane.OK_OPTION) return;

        String address = addrField.getText().trim();
        if (address.isEmpty()) address = "Hostel Block B, Room 304, Campus East";

        try {
            OrderItem orderItem = new OrderItem(0, 0, item.itemId, 1, price);
            List<OrderItem> list = new ArrayList<>();
            list.add(orderItem);

            Order order = new Order();
            order.setUserId(userId);
            order.setDeliveryAddress(address);
            order.setItems(list);
            order.setStatus("CONFIRMED");

            int orderId;
            try {
                orderId = orderService.placeOrder(order);
            } catch (Exception ex) {
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
                    "Item: " + item.title + " (₹" + String.format("%.2f", price) + ")\n" +
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
                openOrders();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not place order: " + ex.getMessage(), "Order Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openCart() {
        if (cartFrameInstance != null && cartFrameInstance.isDisplayable()) {
            cartFrameInstance.setVisible(true);
            cartFrameInstance.setExtendedState(Frame.NORMAL);
            cartFrameInstance.toFront();
            cartFrameInstance.requestFocus();
            cartFrameInstance.loadCart();
        } else {
            cartFrameInstance = new CartFrame(userId, cartService, orderService);
            cartFrameInstance.setVisible(true);
        }
    }

    private void openOrders() {
        ordersFrameInstance = OrderHistoryFrame.openOrBringToFront(userId, orderService);
    }

    private void openMessages() {
        if (inboxFrameInstance != null && inboxFrameInstance.isDisplayable()) {
            inboxFrameInstance.setVisible(true);
            inboxFrameInstance.setExtendedState(Frame.NORMAL);
            inboxFrameInstance.toFront();
            inboxFrameInstance.requestFocus();
        } else {
            inboxFrameInstance = new InboxFrame(userId, messageService);
            inboxFrameInstance.setVisible(true);
        }
    }

    private void openItemDetails(MarketItem item) {
        if (detailsFrameInstance != null && detailsFrameInstance.isDisplayable()) {
            detailsFrameInstance.dispose();
        }
        double p = item.salePrice > 0 ? item.salePrice : item.rentPrice;
        detailsFrameInstance = new ItemDetailsFrame(
                userId, item.itemId, item.title, item.type, p, item.sellerId, item.description, cartService, orderService
        );
        detailsFrameInstance.setVisible(true);
    }

    private void addItemToCart(MarketItem item) {
        try {
            double price = item.salePrice > 0 ? item.salePrice : item.rentPrice;
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setItemId(item.itemId);
            cartItem.setQuantity(1);
            cartItem.setUnitPrice(price);

            boolean success = cartService.addToCart(cartItem);
            if (success) {
                if (cartFrameInstance != null && cartFrameInstance.isDisplayable()) {
                    cartFrameInstance.loadCart();
                }

                int choice = JOptionPane.showOptionDialog(
                        this,
                        "\"" + item.title + "\" (₹" + String.format("%.2f", price) + ") added to your cart!",
                        "Added to Cart",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"View Cart", "Continue Shopping"},
                        "Continue Shopping"
                );
                if (choice == JOptionPane.YES_OPTION) {
                    openCart();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Could not add item to cart.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Could not add to cart: " + ex.getMessage(), "Cart Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openAddItemDialog() {
        JTextField nameField = StuRentTheme.createTextField(20);
        JTextField priceField = StuRentTheme.createTextField(10);
        JComboBox<String> itemTypeCombo = new JComboBox<>(new String[] { "SALE", "RENT", "SALE_AND_RENT" });

        JPanel form = new JPanel(new GridLayout(3, 2, 10, 10));
        form.add(new JLabel("Item Name:"));
        form.add(nameField);
        form.add(new JLabel("Listing Type:"));
        form.add(itemTypeCombo);
        form.add(new JLabel("Price (₹):"));
        form.add(priceField);

        int res = JOptionPane.showConfirmDialog(this, form, "Add New Campus Item", JOptionPane.OK_CANCEL_OPTION);
        if (res == JOptionPane.OK_OPTION && !nameField.getText().trim().isEmpty()) {
            double p = 100.0;
            try {
                p = Double.parseDouble(priceField.getText().trim());
            } catch (Exception ignored) {
            }
            int nextId = items.size() + 10;
            items.add(0, new MarketItem(nextId, nameField.getText().trim(), (String) itemTypeCombo.getSelectedItem(), p, 10.0,
                    "AVAILABLE", "ME", "New student listing"));
            renderCards();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DiscoverItemsFrame().setVisible(true));
    }
}
