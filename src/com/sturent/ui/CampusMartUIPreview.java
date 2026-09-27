package com.sturent.ui;

import com.sturent.dao.*;
import com.sturent.db.CampusMartDBTest;
import com.sturent.model.*;
import com.sturent.service.*;
import com.sturent.ui.cart.CartFrame;
import com.sturent.ui.cart.CheckoutFrame;
import com.sturent.ui.marketplace.DiscoverItemsFrame;
import com.sturent.ui.marketplace.ItemDetailsFrame;
import com.sturent.ui.messaging.ConversationFrame;
import com.sturent.ui.messaging.InboxFrame;
import com.sturent.ui.messaging.SendMessageDialog;
import com.sturent.ui.order.OrderDetailsFrame;
import com.sturent.ui.order.OrderHistoryFrame;
import com.sturent.ui.order.OrderStatusFrame;
import com.sturent.ui.review.ReviewFrame;
import com.sturent.ui.review.SellerReviewsFrame;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StuRent UI Master Preview Hub
 * Launches all screens styled with the official StuRent design system.
 */
public class CampusMartUIPreview extends JFrame {

    private final JRadioButton demoModeRadio = new JRadioButton("Demo / Mock Mode (Instantly preview with sample data)", true);
    private final JRadioButton liveModeRadio = new JRadioButton("Live Database Mode (Aiven MySQL)");

    private final JTextField userIdField = StuRentTheme.createTextField(4);
    private final JTextField orderIdField = StuRentTheme.createTextField(4);
    private final JTextField otherUserIdField = StuRentTheme.createTextField(4);

    public CampusMartUIPreview() {
        super("StuRent - UI Suite Preview");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(960, 720);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        userIdField.setText("1");
        orderIdField.setText("101");
        otherUserIdField.setText("2");

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // 1. Brand Header
        JLabel hubBadge = StuRentTheme.createBadge("Preview Hub Active", StuRentTheme.BADGE_GREEN_BG, StuRentTheme.BADGE_GREEN_TEXT);
        JPanel header = StuRentTheme.createHeader(hubBadge);
        add(header, BorderLayout.NORTH);

        // 2. Main Body
        JPanel main = new JPanel(new BorderLayout(16, 16));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(20, 28, 20, 28));

        // Subheader + Control Bar
        JPanel topBox = new JPanel();
        topBox.setLayout(new BoxLayout(topBox, BoxLayout.Y_AXIS));
        topBox.setOpaque(false);

        JLabel title = new JLabel("StuRent UI Design Suite");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JLabel sub = new JLabel("Preview all campus marketplace screens with consistent Forest Green branding and modern card layouts.");
        sub.setFont(StuRentTheme.FONT_SUBTITLE);
        sub.setForeground(StuRentTheme.TEXT_MUTED);

        topBox.add(title);
        topBox.add(Box.createRigidArea(new Dimension(0, 4)));
        topBox.add(sub);
        topBox.add(Box.createRigidArea(new Dimension(0, 14)));

        // Mode and Parameter Card
        JPanel ctrlCard = StuRentTheme.createCard();
        ctrlCard.setLayout(new FlowLayout(FlowLayout.LEFT, 14, 4));

        ButtonGroup group = new ButtonGroup();
        group.add(demoModeRadio);
        group.add(liveModeRadio);
        demoModeRadio.setOpaque(false);
        liveModeRadio.setOpaque(false);
        demoModeRadio.setFont(StuRentTheme.FONT_BOLD);
        demoModeRadio.setForeground(StuRentTheme.PRIMARY_GREEN);
        liveModeRadio.setFont(StuRentTheme.FONT_BOLD);

        ctrlCard.add(demoModeRadio);
        ctrlCard.add(liveModeRadio);
        ctrlCard.add(new JSeparator(SwingConstants.VERTICAL));
        ctrlCard.add(new JLabel("User ID:"));
        ctrlCard.add(userIdField);
        ctrlCard.add(new JLabel("Order ID:"));
        ctrlCard.add(orderIdField);
        ctrlCard.add(new JLabel("Target User/Seller ID:"));
        ctrlCard.add(otherUserIdField);

        topBox.add(ctrlCard);
        main.add(topBox, BorderLayout.NORTH);

        // Grid of Category Cards
        JPanel grid = new JPanel(new GridLayout(0, 2, 16, 16));
        grid.setOpaque(false);

        grid.add(createCategoryCard("Authentication & Admin (R4 Module)", new JButton[]{
                createLauncherBtn("Sign In (LoginFrame)", () -> new com.sturent.gui.LoginFrame().setVisible(true)),
                createLauncherBtn("Create Account (RegisterFrame)", () -> new com.sturent.gui.RegisterFrame().setVisible(true)),
                createLauncherBtn("Admin Dashboard (AdminDashboardFrame)", () -> new com.sturent.gui.admin.AdminDashboardFrame().setVisible(true))
        }));

        grid.add(createCategoryCard("Marketplace & Items (Screenshots)", new JButton[]{
                createLauncherBtn("Discover Items (Marketplace Home)", this::openMarketplace),
                createLauncherBtn("Item Details View (Two-Column Preview)", this::openItemDetails)
        }));

        grid.add(createCategoryCard("Cart & Checkout", new JButton[]{
                createLauncherBtn("My Cart (CartFrame)", this::openCart),
                createLauncherBtn("Checkout & Delivery (CheckoutFrame)", this::openCheckout)
        }));

        grid.add(createCategoryCard("Order Management", new JButton[]{
                createLauncherBtn("Order History (OrderHistoryFrame)", this::openOrderHistory),
                createLauncherBtn("Order Details (OrderDetailsFrame)", this::openOrderDetails),
                createLauncherBtn("Order Status & Tracking (OrderStatusFrame)", this::openOrderStatus)
        }));

        grid.add(createCategoryCard("Messaging & Feedback", new JButton[]{
                createLauncherBtn("Inbox & Messages (InboxFrame)", this::openInbox),
                createLauncherBtn("Chat Conversation (ConversationFrame)", this::openConversation),
                createLauncherBtn("Send Message Dialog", this::openSendMessageDialog),
                createLauncherBtn("Write Review (ReviewFrame)", this::openReview),
                createLauncherBtn("Seller Reviews (SellerReviewsFrame)", this::openSellerReviews)
        }));

        JScrollPane scroll = new JScrollPane(grid);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        main.add(scroll, BorderLayout.CENTER);

        // Bottom Bar
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        JButton dbBtn = StuRentTheme.createSecondaryButton("🔌 Aiven MySQL DB Connection Tester");
        dbBtn.addActionListener(e -> new CampusMartDBTest().setVisible(true));
        bottom.add(dbBtn, BorderLayout.EAST);
        main.add(bottom, BorderLayout.SOUTH);

        add(main, BorderLayout.CENTER);
    }

    private JPanel createCategoryCard(String title, JButton[] buttons) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(StuRentTheme.FONT_CARD_TITLE);
        titleLbl.setForeground(StuRentTheme.TEXT_DARK);

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(new JSeparator());
        card.add(Box.createRigidArea(new Dimension(0, 10)));

        for (JButton btn : buttons) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            card.add(btn);
            card.add(Box.createRigidArea(new Dimension(0, 6)));
        }
        return card;
    }

    private JButton createLauncherBtn(String text, Runnable action) {
        JButton btn = StuRentTheme.createSecondaryButton(text);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.addActionListener(e -> action.run());
        return btn;
    }

    private int getUserId() {
        try { return Integer.parseInt(userIdField.getText().trim()); } catch (Exception e) { return 1; }
    }

    private int getOrderId() {
        try { return Integer.parseInt(orderIdField.getText().trim()); } catch (Exception e) { return 101; }
    }

    private int getOtherUserId() {
        try { return Integer.parseInt(otherUserIdField.getText().trim()); } catch (Exception e) { return 2; }
    }

    private boolean isDemo() {
        return demoModeRadio.isSelected();
    }

    private final CartService demoCartService = new CartService(new MockCartDAO());
    private final OrderService demoOrderService = new OrderService(new MockOrderDAO());
    private final MessageService demoMessageService = new MessageService(new MockMessageDAO());
    private final ReviewService demoReviewService = new ReviewService(new MockReviewDAO());

    private void openMarketplace() {
        if (isDemo()) {
            new DiscoverItemsFrame(getUserId(), demoCartService, demoOrderService, demoMessageService).setVisible(true);
        } else {
            new DiscoverItemsFrame(getUserId()).setVisible(true);
        }
    }

    private void openItemDetails() {
        if (isDemo()) {
            new ItemDetailsFrame(getUserId(), 1, "test5", "SALE", 11111.00, "TEST123", "test5 dumbbell set in mint condition", demoCartService, demoOrderService).setVisible(true);
        } else {
            new ItemDetailsFrame(getUserId(), 1, "test5", "SALE", 11111.00, "TEST123", "test5 dumbbell set in mint condition", new CartService(), new OrderService()).setVisible(true);
        }
    }

    private void openCart() {
        if (isDemo()) {
            new CartFrame(getUserId(), demoCartService, demoOrderService).setVisible(true);
        } else {
            new CartFrame(getUserId(), new CartService(), new OrderService()).setVisible(true);
        }
    }

    private void openCheckout() {
        if (isDemo()) {
            new CheckoutFrame(getUserId(), demoCartService, demoOrderService).setVisible(true);
        } else {
            new CheckoutFrame(getUserId(), new CartService(), new OrderService()).setVisible(true);
        }
    }

    private void openOrderHistory() {
        if (isDemo()) {
            OrderHistoryFrame.openOrBringToFront(getUserId(), demoOrderService);
        } else {
            OrderHistoryFrame.openOrBringToFront(getUserId(), new OrderService());
        }
    }

    private void openOrderDetails() {
        if (isDemo()) {
            new OrderDetailsFrame(getOrderId(), demoOrderService).setVisible(true);
        } else {
            new OrderDetailsFrame(getOrderId(), new OrderService()).setVisible(true);
        }
    }

    private void openOrderStatus() {
        if (isDemo()) {
            new OrderStatusFrame(getOrderId(), demoOrderService).setVisible(true);
        } else {
            new OrderStatusFrame(getOrderId(), new OrderService()).setVisible(true);
        }
    }

    private void openInbox() {
        if (isDemo()) {
            new InboxFrame(getUserId(), demoMessageService).setVisible(true);
        } else {
            new InboxFrame(getUserId()).setVisible(true);
        }
    }

    private void openConversation() {
        if (isDemo()) {
            new ConversationFrame(getUserId(), getOtherUserId(), demoMessageService).setVisible(true);
        } else {
            new ConversationFrame(getUserId(), getOtherUserId()).setVisible(true);
        }
    }

    private void openSendMessageDialog() {
        if (isDemo()) {
            new SendMessageDialog(this, getUserId(), getOtherUserId(), demoMessageService).setVisible(true);
        } else {
            new SendMessageDialog(this, getUserId(), getOtherUserId()).setVisible(true);
        }
    }

    private void openReview() {
        if (isDemo()) {
            new ReviewFrame(getUserId(), getOtherUserId(), 10, demoReviewService).setVisible(true);
        } else {
            new ReviewFrame(getUserId(), getOtherUserId(), 10).setVisible(true);
        }
    }

    private void openSellerReviews() {
        if (isDemo()) {
            new SellerReviewsFrame(getOtherUserId(), demoReviewService).setVisible(true);
        } else {
            new SellerReviewsFrame(getOtherUserId()).setVisible(true);
        }
    }

    // --- Mock DAOs ---
    private static class MockCartDAO extends CartDAO {
        private final List<CartItem> items = new ArrayList<>();
        public MockCartDAO() {
            CartItem c1 = new CartItem();
            c1.setCartItemId(1); c1.setUserId(1); c1.setItemId(101); c1.setQuantity(1); c1.setUnitPrice(450.0);
            CartItem c2 = new CartItem();
            c2.setCartItemId(2); c2.setUserId(1); c2.setItemId(102); c2.setQuantity(2); c2.setUnitPrice(120.0);
            CartItem c3 = new CartItem();
            c3.setCartItemId(3); c3.setUserId(1); c3.setItemId(103); c3.setQuantity(1); c3.setUnitPrice(799.0);
            items.add(c1); items.add(c2); items.add(c3);
        }
        @Override
        public boolean add(CartItem item) {
            for (CartItem existing : items) {
                if (existing.getUserId() == item.getUserId() && existing.getItemId() == item.getItemId()) {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    return true;
                }
            }
            int newId = items.size() + 1;
            item.setCartItemId(newId);
            items.add(item);
            return true;
        }
        @Override public List<CartItem> findByUserId(int userId) { return new ArrayList<>(items); }
        @Override public boolean remove(int id) { items.removeIf(i -> i.getCartItemId() == id); return true; }
        @Override public boolean clearUserCart(int u) { items.clear(); return true; }
    }

    private static class MockOrderDAO extends OrderDAO {
        private final List<Order> orders = new ArrayList<>();
        private int nextOrderId = 104;

        public MockOrderDAO() {
            Order o1 = new Order();
            o1.setOrderId(101);
            o1.setUserId(1);
            o1.setStatus("DELIVERED");
            o1.setTotalAmount(1489.00);
            o1.setDeliveryAddress("Room 304, Block B, Campus Hostel");

            Order o2 = new Order();
            o2.setOrderId(102);
            o2.setUserId(1);
            o2.setStatus("IN_TRANSIT");
            o2.setTotalAmount(599.50);
            o2.setDeliveryAddress("Campus Library Pickup Desk");

            orders.add(o1);
            orders.add(o2);
        }

        @Override
        public synchronized List<Order> findByUserId(int userId) {
            List<Order> list = new ArrayList<>();
            for (Order o : orders) {
                if (o.getUserId() == userId) {
                    list.add(o);
                }
            }
            return list;
        }

        @Override
        public synchronized Order findById(int id) {
            for (Order o : orders) {
                if (o.getOrderId() == id) return o;
            }
            return null;
        }

        @Override
        public synchronized int createOrder(Order o) {
            int newId = nextOrderId++;
            o.setOrderId(newId);
            orders.add(0, o);
            return newId;
        }
    }

    private static class MockMessageDAO extends MessageDAO {
        private final List<Message> msgs = new ArrayList<>();
        public MockMessageDAO() {
            msgs.add(new Message(1, 2, 1, "Hi! Is the Database Engineering textbook still available for rent?", false, null));
            msgs.add(new Message(2, 1, 2, "Yes, it is! In great condition, no missing pages.", true, null));
            msgs.add(new Message(3, 2, 1, "Awesome, can we meet at the campus library today at 4 PM?", false, null));
        }
        @Override public List<Message> findInbox(int r) { return new ArrayList<>(msgs); }
        @Override public List<Message> findConversation(int u1, int u2) { return new ArrayList<>(msgs); }
        @Override public boolean send(Message m) { msgs.add(m); return true; }
    }

    private static class MockReviewDAO extends ReviewDAO {
        @Override public List<Review> findBySellerId(int s) {
            List<Review> list = new ArrayList<>();
            list.add(new Review(1, 3, s, 10, 5, "Item was exactly as described, prompt meetup at hostel. Highly recommended!", null));
            list.add(new Review(2, 4, s, 10, 4, "Good condition lab equipment. Smooth communication.", null));
            return list;
        }
        @Override public boolean add(Review r) { return true; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CampusMartUIPreview().setVisible(true));
    }
}
