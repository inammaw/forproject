package com.sturent.ui;

import com.sturent.model.Notification;
import com.sturent.service.NotificationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class NotificationFrame extends JFrame {
    private final int userId;
    private final NotificationService service;
    private final JPanel notificationsContainer = new JPanel();
    private final JLabel countLabel = new JLabel("All notifications");

    public NotificationFrame(int userId) {
        this(userId, new NotificationService());
    }

    public NotificationFrame(int userId, NotificationService service) {
        this.userId = userId;
        this.service = service;

        setTitle("StuRent - Notifications");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadNotifications();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        JButton refresh = StuRentTheme.createSecondaryButton("Refresh");
        refresh.addActionListener(e -> loadNotifications());
        JPanel header = StuRentTheme.createHeader(refresh);
        add(header, BorderLayout.NORTH);

        // Center Panel
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Campus Notifications");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        countLabel.setFont(StuRentTheme.FONT_SUBTITLE);
        countLabel.setForeground(StuRentTheme.TEXT_MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(countLabel);
        main.add(titlePanel, BorderLayout.NORTH);

        // Scrollable cards
        notificationsContainer.setLayout(new BoxLayout(notificationsContainer, BoxLayout.Y_AXIS));
        notificationsContainer.setOpaque(false);

        JScrollPane scroll = new JScrollPane(notificationsContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    private void loadNotifications() {
        notificationsContainer.removeAll();
        try {
            List<Notification> list = service.getNotifications(userId);
            if (list == null || list.isEmpty()) {
                JPanel empty = StuRentTheme.createCard();
                empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
                JLabel l1 = new JLabel("No new notifications.");
                l1.setFont(StuRentTheme.FONT_CARD_TITLE);
                l1.setForeground(StuRentTheme.TEXT_MUTED);
                JLabel l2 = new JLabel("Campus updates, order confirmations, and chats will notify you here.");
                l2.setFont(StuRentTheme.FONT_SMALL);
                l2.setForeground(StuRentTheme.TEXT_MUTED);
                empty.add(l1);
                empty.add(Box.createRigidArea(new Dimension(0, 4)));
                empty.add(l2);
                notificationsContainer.add(empty);
                countLabel.setText("0 notifications");
            } else {
                countLabel.setText(list.size() + " notification(s)");
                for (Notification n : list) {
                    notificationsContainer.add(createNotificationCard(n));
                    notificationsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (Exception ex) {
            // Demo fallback if offline
            JPanel noticeCard = StuRentTheme.createCard();
            noticeCard.setBackground(new Color(254, 243, 199));
            noticeCard.setBorder(BorderFactory.createLineBorder(new Color(253, 230, 138), 1));
            JLabel noticeText = new JLabel("<html><b>Demo Mode:</b> Displaying sample notifications (" + ex.getMessage() + ")</html>");
            noticeText.setFont(StuRentTheme.FONT_SMALL);
            noticeText.setForeground(new Color(180, 83, 9));
            noticeCard.add(noticeText);
            notificationsContainer.add(noticeCard);
            notificationsContainer.add(Box.createRigidArea(new Dimension(0, 10)));

            Notification n1 = new Notification();
            n1.setNotificationId(1);
            n1.setTitle("Order #101 Confirmed");
            n1.setMessage("Your rental of the dumbbell set has been confirmed by seller TEST123.");
            Notification n2 = new Notification();
            n2.setNotificationId(2);
            n2.setTitle("New Message from User #2");
            n2.setMessage("Awesome, can we meet at the campus library today at 4 PM?");

            notificationsContainer.add(createNotificationCard(n1));
            notificationsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            notificationsContainer.add(createNotificationCard(n2));
            countLabel.setText("2 notifications (Demo Preview)");
        }
        notificationsContainer.revalidate();
        notificationsContainer.repaint();
    }

    private JPanel createNotificationCard(Notification n) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout(14, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        // Icon badge
        JPanel iconBadge = new JPanel(new GridBagLayout());
        iconBadge.setPreferredSize(new Dimension(46, 46));
        iconBadge.setBackground(n.isRead() ? new Color(241, 245, 249) : StuRentTheme.BADGE_GREEN_BG);
        iconBadge.setBorder(BorderFactory.createLineBorder(n.isRead() ? StuRentTheme.CARD_BORDER : StuRentTheme.PRIMARY_GREEN_LIGHT, 1));
        JLabel iconLbl = new JLabel(n.isRead() ? "\u2713" : "!");
        iconLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        iconLbl.setForeground(n.isRead() ? StuRentTheme.TEXT_MUTED : StuRentTheme.PRIMARY_GREEN);
        iconBadge.add(iconLbl);

        // Center content
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel title = new JLabel(n.getTitle());
        title.setFont(StuRentTheme.FONT_CARD_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JLabel message = new JLabel(n.getMessage());
        message.setFont(StuRentTheme.FONT_REGULAR);
        message.setForeground(StuRentTheme.TEXT_MUTED);

        center.add(title);
        center.add(Box.createRigidArea(new Dimension(0, 4)));
        center.add(message);

        // Right action
        JButton markBtn = StuRentTheme.createSecondaryButton(n.isRead() ? "Read" : "Mark Read");
        markBtn.setEnabled(!n.isRead());
        markBtn.addActionListener(e -> {
            try {
                service.markAsRead(n.getNotificationId());
                loadNotifications();
            } catch (Exception ignored) {}
        });

        card.add(iconBadge, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(markBtn, BorderLayout.EAST);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new NotificationFrame(1).setVisible(true));
    }
}
