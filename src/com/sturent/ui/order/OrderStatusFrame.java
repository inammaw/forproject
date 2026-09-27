package com.sturent.ui.order;

import com.sturent.model.Order;
import com.sturent.service.OrderService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class OrderStatusFrame extends JFrame {
    private final int orderId;
    private final OrderService service;
    private final JLabel statusBadge = new JLabel();
    private final JLabel statusDesc = new JLabel();
    private final JPanel timelinePanel = new JPanel();

    public OrderStatusFrame(int orderId) { this(orderId, new OrderService()); }

    public OrderStatusFrame(int orderId, OrderService service) {
        this.orderId = orderId;
        this.service = service;

        setTitle("StuRent - Track Order #" + orderId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 480);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadStatus();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top Brand Header
        JButton refresh = StuRentTheme.createSecondaryButton("Refresh Status");
        refresh.addActionListener(e -> loadStatus());
        JPanel header = StuRentTheme.createHeader(refresh);
        add(header, BorderLayout.NORTH);

        // Center card
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        JLabel title = new JLabel("Order Tracking");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JLabel sub = new JLabel("Real-time progress for your campus marketplace transaction");
        sub.setFont(StuRentTheme.FONT_SUBTITLE);
        sub.setForeground(StuRentTheme.TEXT_MUTED);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 4)));
        main.add(sub);
        main.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JPanel rowTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rowTop.setOpaque(false);
        JLabel ref = new JLabel("Order #" + orderId);
        ref.setFont(StuRentTheme.FONT_CARD_TITLE);
        ref.setForeground(StuRentTheme.TEXT_DARK);
        rowTop.add(ref);
        rowTop.add(statusBadge);

        statusDesc.setFont(StuRentTheme.FONT_REGULAR);
        statusDesc.setForeground(StuRentTheme.TEXT_MUTED);

        timelinePanel.setLayout(new GridLayout(1, 4, 10, 0));
        timelinePanel.setOpaque(false);
        timelinePanel.setBorder(new EmptyBorder(20, 0, 10, 0));

        card.add(rowTop);
        card.add(Box.createRigidArea(new Dimension(0, 8)));
        card.add(statusDesc);
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(new JSeparator());
        card.add(timelinePanel);

        main.add(card);
        add(main, BorderLayout.CENTER);
    }

    private void loadStatus() {
        try {
            Order o = null;
            try {
                o = service.getOrder(orderId);
            } catch (Exception ignored) {
            }
            if (o == null) {
                o = OrderHistoryFrame.findFallbackOrder(orderId);
            }
            String status = (o == null || o.getStatus() == null) ? "CONFIRMED" : o.getStatus().toUpperCase();

            statusBadge.setText(" " + status + " ");
            statusBadge.setFont(StuRentTheme.FONT_BADGE);
            statusBadge.setOpaque(true);
            statusBadge.setBackground(status.contains("DELIVER") ? StuRentTheme.BADGE_GREEN_BG : StuRentTheme.BADGE_AMBER_BG);
            statusBadge.setForeground(status.contains("DELIVER") ? StuRentTheme.BADGE_GREEN_TEXT : StuRentTheme.BADGE_AMBER_TEXT);

            statusDesc.setText("Current Status: " + status + "  \u2022  Campus delivery in progress or fulfilled.");

            timelinePanel.removeAll();
            timelinePanel.add(createStep("1. Placed", true));
            timelinePanel.add(createStep("2. Confirmed", true));
            timelinePanel.add(createStep("3. In Transit", status.contains("TRANSIT") || status.contains("DELIVER")));
            timelinePanel.add(createStep("4. Delivered", status.contains("DELIVER")));

        } catch (Exception ex) {
            statusDesc.setText("Unable to fetch status: " + ex.getMessage());
        }
        timelinePanel.revalidate();
        timelinePanel.repaint();
    }

    private JPanel createStep(String labelText, boolean completed) {
        JPanel step = new JPanel();
        step.setLayout(new BoxLayout(step, BoxLayout.Y_AXIS));
        step.setOpaque(false);

        JLabel indicator = new JLabel(completed ? "\u2713" : "\u25CB", SwingConstants.CENTER);
        indicator.setFont(new Font("Segoe UI", Font.BOLD, 18));
        indicator.setForeground(completed ? StuRentTheme.PRIMARY_GREEN : StuRentTheme.TEXT_MUTED);
        indicator.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText, SwingConstants.CENTER);
        label.setFont(StuRentTheme.FONT_SMALL);
        label.setForeground(completed ? StuRentTheme.TEXT_DARK : StuRentTheme.TEXT_MUTED);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        step.add(indicator);
        step.add(Box.createRigidArea(new Dimension(0, 4)));
        step.add(label);
        return step;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new OrderStatusFrame(101).setVisible(true));
    }
}
