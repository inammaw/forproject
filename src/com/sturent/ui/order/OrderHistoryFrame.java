package com.sturent.ui.order;

import com.sturent.model.Order;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class OrderHistoryFrame extends JFrame {
    private static final List<OrderHistoryFrame> OPEN_INSTANCES = new ArrayList<>();
    private static final List<Order> FALLBACK_ORDERS = new ArrayList<>();
    static {
        Order o1 = new Order(); o1.setOrderId(101); o1.setUserId(1); o1.setStatus("DELIVERED"); o1.setTotalAmount(1489.00); o1.setDeliveryAddress("Room 304, Block B, Campus Hostel");
        Order o2 = new Order(); o2.setOrderId(102); o2.setUserId(1); o2.setStatus("IN_TRANSIT"); o2.setTotalAmount(599.50); o2.setDeliveryAddress("Campus Library Pickup Desk");
        Order o3 = new Order(); o3.setOrderId(103); o3.setUserId(1); o3.setStatus("CONFIRMED"); o3.setTotalAmount(250.00); o3.setDeliveryAddress("Hostel Block B, Room 304, Campus East");
        FALLBACK_ORDERS.add(o1);
        FALLBACK_ORDERS.add(o2);
        FALLBACK_ORDERS.add(o3);
    }

    public static synchronized void recordFallbackOrder(Order o) {
        FALLBACK_ORDERS.add(0, o);
    }

    public static synchronized Order findFallbackOrder(int orderId) {
        for (Order o : FALLBACK_ORDERS) {
            if (o.getOrderId() == orderId) return o;
        }
        return null;
    }

    public static void refreshAllOpenFrames(int userId) {
        SwingUtilities.invokeLater(() -> {
            synchronized (OPEN_INSTANCES) {
                for (OrderHistoryFrame frame : OPEN_INSTANCES) {
                    if (frame.userId == userId && frame.isDisplayable()) {
                        frame.loadOrders();
                    }
                }
            }
        });
    }

    public static OrderHistoryFrame openOrBringToFront(int userId, OrderService service) {
        synchronized (OPEN_INSTANCES) {
            for (OrderHistoryFrame frame : OPEN_INSTANCES) {
                if (frame.userId == userId && frame.isDisplayable()) {
                    frame.setVisible(true);
                    frame.setExtendedState(Frame.NORMAL);
                    frame.toFront();
                    frame.requestFocus();
                    frame.loadOrders();
                    return frame;
                }
            }
        }
        OrderHistoryFrame frame = new OrderHistoryFrame(userId, service);
        frame.setVisible(true);
        return frame;
    }

    private final int userId;
    private final OrderService service;
    private final JPanel ordersContainer = new JPanel();
    private final JLabel headerCount = new JLabel("All orders");

    public OrderHistoryFrame(int userId) { this(userId, new OrderService()); }

    public OrderHistoryFrame(int userId, OrderService service) {
        this.userId = userId;
        this.service = service != null ? service : new OrderService();

        setTitle("StuRent - Order History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 640);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        synchronized (OPEN_INSTANCES) {
            OPEN_INSTANCES.add(this);
        }
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                synchronized (OPEN_INSTANCES) {
                    OPEN_INSTANCES.remove(OrderHistoryFrame.this);
                }
            }
        });

        buildUI();
        loadOrders();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top Brand Header
        JButton refresh = StuRentTheme.createSecondaryButton("Refresh");
        refresh.addActionListener(e -> loadOrders());
        JPanel header = StuRentTheme.createHeader(refresh);
        add(header, BorderLayout.NORTH);

        // Main content
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Order History");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        headerCount.setFont(StuRentTheme.FONT_SUBTITLE);
        headerCount.setForeground(StuRentTheme.TEXT_MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(headerCount);
        main.add(titlePanel, BorderLayout.NORTH);

        // Scrollable container for order cards
        ordersContainer.setLayout(new BoxLayout(ordersContainer, BoxLayout.Y_AXIS));
        ordersContainer.setOpaque(false);

        JScrollPane scroll = new JScrollPane(ordersContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    public void loadOrders() {
        ordersContainer.removeAll();
        try {
            List<Order> orders = service.getOrdersForUser(userId);
            if (orders == null || orders.isEmpty()) {
                JPanel empty = StuRentTheme.createCard();
                empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
                JLabel l1 = new JLabel("No orders placed yet.");
                l1.setFont(StuRentTheme.FONT_CARD_TITLE);
                l1.setForeground(StuRentTheme.TEXT_MUTED);
                JLabel l2 = new JLabel("Browse campus listings to place your first order!");
                l2.setFont(StuRentTheme.FONT_SMALL);
                l2.setForeground(StuRentTheme.TEXT_MUTED);
                empty.add(l1);
                empty.add(Box.createRigidArea(new Dimension(0, 4)));
                empty.add(l2);
                ordersContainer.add(empty);
                headerCount.setText("0 orders recorded");
            } else {
                headerCount.setText(orders.size() + " orders recorded");
                for (Order o : orders) {
                    ordersContainer.add(createOrderCard(o));
                    ordersContainer.add(Box.createRigidArea(new Dimension(0, 12)));
                }
            }
        } catch (Exception ex) {
            JPanel noticeCard = StuRentTheme.createCard();
            noticeCard.setLayout(new BorderLayout());
            noticeCard.setBackground(new Color(254, 243, 199));
            noticeCard.setBorder(BorderFactory.createLineBorder(new Color(253, 230, 138), 1));
            JLabel noticeText = new JLabel("<html><b>Demo Preview Mode:</b> Displaying sample orders (Database: " + ex.getMessage() + ")</html>");
            noticeText.setFont(StuRentTheme.FONT_SMALL);
            noticeText.setForeground(new Color(180, 83, 9));
            noticeCard.add(noticeText, BorderLayout.CENTER);
            ordersContainer.add(noticeCard);
            ordersContainer.add(Box.createRigidArea(new Dimension(0, 10)));

            synchronized (FALLBACK_ORDERS) {
                for (Order o : FALLBACK_ORDERS) {
                    ordersContainer.add(createOrderCard(o));
                    ordersContainer.add(Box.createRigidArea(new Dimension(0, 12)));
                }
                headerCount.setText(FALLBACK_ORDERS.size() + " orders (Demo Preview)");
            }
        }
        ordersContainer.revalidate();
        ordersContainer.repaint();
    }

    private JPanel createOrderCard(Order o) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout(16, 12));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));

        // Left info
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        rowTop.setOpaque(false);

        JLabel orderIdLbl = new JLabel("Order #" + o.getOrderId());
        orderIdLbl.setFont(StuRentTheme.FONT_CARD_TITLE);
        orderIdLbl.setForeground(StuRentTheme.TEXT_DARK);

        JLabel badge;
        String status = o.getStatus() == null ? "PENDING" : o.getStatus().toUpperCase();
        if (status.contains("DELIVER")) {
            badge = StuRentTheme.createBadge(status, StuRentTheme.BADGE_GREEN_BG, StuRentTheme.BADGE_GREEN_TEXT);
        } else if (status.contains("CANCEL")) {
            badge = StuRentTheme.createBadge(status, StuRentTheme.DANGER_BG, StuRentTheme.DANGER_TEXT);
        } else {
            badge = StuRentTheme.createBadge(status, StuRentTheme.BADGE_AMBER_BG, StuRentTheme.BADGE_AMBER_TEXT);
        }

        rowTop.add(orderIdLbl);
        rowTop.add(badge);

        JLabel amountLbl = new JLabel("Total: ₹" + String.format("%.2f", o.getTotalAmount()));
        amountLbl.setFont(StuRentTheme.FONT_PRICE);
        amountLbl.setForeground(StuRentTheme.PRIMARY_GREEN);

        JLabel addrLbl = new JLabel(o.getDeliveryAddress() != null && !o.getDeliveryAddress().isBlank()
                ? "📍 " + o.getDeliveryAddress() : "📍 Campus Delivery");
        addrLbl.setFont(StuRentTheme.FONT_SMALL);
        addrLbl.setForeground(StuRentTheme.TEXT_MUTED);

        left.add(rowTop);
        left.add(Box.createRigidArea(new Dimension(0, 4)));
        left.add(amountLbl);
        left.add(Box.createRigidArea(new Dimension(0, 4)));
        left.add(addrLbl);

        // Right actions
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JButton viewBtn = StuRentTheme.createPrimaryButton("View Details");
        viewBtn.addActionListener(e -> new OrderDetailsFrame(o.getOrderId(), service).setVisible(true));

        JButton trackBtn = StuRentTheme.createSecondaryButton("Track");
        trackBtn.addActionListener(e -> new OrderStatusFrame(o.getOrderId(), service).setVisible(true));

        right.add(trackBtn);
        right.add(viewBtn);

        card.add(left, BorderLayout.WEST);
        card.add(right, BorderLayout.EAST);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderHistoryFrame(1).setVisible(true));
    }
}
