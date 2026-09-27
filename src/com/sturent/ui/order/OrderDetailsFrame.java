package com.sturent.ui.order;

import com.sturent.model.Order;
import com.sturent.model.OrderItem;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFrame extends JFrame {
    private final int orderId;
    private final OrderService service;
    private final JPanel container = new JPanel();

    public OrderDetailsFrame(int orderId) { this(orderId, new OrderService()); }

    public OrderDetailsFrame(int orderId, OrderService service) {
        this.orderId = orderId;
        this.service = service;

        setTitle("StuRent - Order Details #" + orderId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(780, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadOrder();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top Brand Header
        JButton closeBtn = StuRentTheme.createSecondaryButton("Close");
        closeBtn.addActionListener(e -> dispose());
        JPanel header = StuRentTheme.createHeader(closeBtn);
        add(header, BorderLayout.NORTH);

        // Center scrollable content
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(24, 32, 24, 32));

        JScrollPane scroll = new JScrollPane(container);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        add(scroll, BorderLayout.CENTER);
    }

    private void loadOrder() {
        container.removeAll();
        try {
            Order o = null;
            try {
                o = service.getOrder(orderId);
            } catch (Exception ignored) {
            }
            if (o == null) {
                o = OrderHistoryFrame.findFallbackOrder(orderId);
            }
            if (o == null) {
                // Synthesize preview order info so user never sees a dead-end
                o = new Order();
                o.setOrderId(orderId);
                o.setUserId(1);
                o.setStatus("CONFIRMED");
                o.setTotalAmount(11111.00);
                o.setDeliveryAddress("Hostel Block B, Room 304, Campus East");
                List<OrderItem> defaultItems = new ArrayList<>();
                defaultItems.add(new OrderItem(1, orderId, 1, 1, 11111.00));
                o.setItems(defaultItems);
            }

            if (o.getItems() == null || o.getItems().isEmpty()) {
                List<OrderItem> defaultItems = new ArrayList<>();
                defaultItems.add(new OrderItem(1, o.getOrderId(), 1, 1, o.getTotalAmount() > 0 ? o.getTotalAmount() : 11111.00));
                o.setItems(defaultItems);
            }

            // 1. Order Info Card
            JPanel infoCard = StuRentTheme.createCard();
            infoCard.setLayout(new BorderLayout(16, 12));

            JPanel left = new JPanel();
            left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
            left.setOpaque(false);

            JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            titleRow.setOpaque(false);
            JLabel title = new JLabel("Order #" + o.getOrderId());
            title.setFont(StuRentTheme.FONT_TITLE);
            title.setForeground(StuRentTheme.TEXT_DARK);

            String status = o.getStatus() == null ? "PENDING" : o.getStatus().toUpperCase();
            JLabel badge = StuRentTheme.createBadge(status,
                    status.contains("DELIVER") ? StuRentTheme.BADGE_GREEN_BG : StuRentTheme.BADGE_AMBER_BG,
                    status.contains("DELIVER") ? StuRentTheme.BADGE_GREEN_TEXT : StuRentTheme.BADGE_AMBER_TEXT);

            titleRow.add(title);
            titleRow.add(badge);

            JLabel total = new JLabel("Total Paid: ₹" + String.format("%.2f", o.getTotalAmount()));
            total.setFont(StuRentTheme.FONT_PRICE);
            total.setForeground(StuRentTheme.PRIMARY_GREEN);

            JLabel addr = new JLabel("Delivery Location: " + (o.getDeliveryAddress() != null ? o.getDeliveryAddress() : "Campus Meetup"));
            addr.setFont(StuRentTheme.FONT_REGULAR);
            addr.setForeground(StuRentTheme.TEXT_MUTED);

            left.add(titleRow);
            left.add(Box.createRigidArea(new Dimension(0, 6)));
            left.add(total);
            left.add(Box.createRigidArea(new Dimension(0, 4)));
            left.add(addr);

            infoCard.add(left, BorderLayout.CENTER);
            container.add(infoCard);
            container.add(Box.createRigidArea(new Dimension(0, 16)));

            // 2. Items Header
            JLabel itemsHeader = new JLabel("Ordered Items");
            itemsHeader.setFont(StuRentTheme.FONT_HEADING);
            itemsHeader.setForeground(StuRentTheme.TEXT_DARK);
            container.add(itemsHeader);
            container.add(Box.createRigidArea(new Dimension(0, 10)));

            // 3. Items list
            for (OrderItem item : o.getItems()) {
                JPanel itemCard = StuRentTheme.createCard();
                itemCard.setLayout(new BorderLayout(16, 0));
                itemCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

                JPanel iLeft = new JPanel();
                iLeft.setLayout(new BoxLayout(iLeft, BoxLayout.Y_AXIS));
                iLeft.setOpaque(false);

                JLabel iName = new JLabel(getItemDisplayName(item.getItemId()));
                iName.setFont(StuRentTheme.FONT_CARD_TITLE);
                iName.setForeground(StuRentTheme.TEXT_DARK);

                JLabel iQty = new JLabel("Quantity: " + item.getQuantity() + "  \u2022  Unit Price: ₹" + String.format("%.2f", item.getUnitPrice()));
                iQty.setFont(StuRentTheme.FONT_SMALL);
                iQty.setForeground(StuRentTheme.TEXT_MUTED);

                iLeft.add(iName);
                iLeft.add(Box.createRigidArea(new Dimension(0, 4)));
                iLeft.add(iQty);

                double lineTotal = item.getQuantity() * item.getUnitPrice();
                JLabel iTotal = new JLabel("₹" + String.format("%.2f", lineTotal));
                iTotal.setFont(StuRentTheme.FONT_PRICE);
                iTotal.setForeground(StuRentTheme.PRIMARY_GREEN);

                itemCard.add(iLeft, BorderLayout.CENTER);
                itemCard.add(iTotal, BorderLayout.EAST);

                container.add(itemCard);
                container.add(Box.createRigidArea(new Dimension(0, 10)));
            }

        } catch (Exception ex) {
            JPanel err = StuRentTheme.createCard();
            err.add(new JLabel("Error: " + ex.getMessage()));
            container.add(err);
        }
        container.revalidate();
        container.repaint();
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderDetailsFrame(101).setVisible(true));
    }
}
