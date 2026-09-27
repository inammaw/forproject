package com.sturent.ui.messaging;

import com.sturent.model.Message;
import com.sturent.service.MessageService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class InboxFrame extends JFrame {
    private final int userId;
    private final MessageService service;
    private final JPanel messagesContainer = new JPanel();
    private final JLabel countLabel = new JLabel("Messages");

    public InboxFrame(int userId) { this(userId, new MessageService()); }

    public InboxFrame(int userId, MessageService service) {
        this.userId = userId;
        this.service = service;

        setTitle("StuRent - Inbox");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(860, 640);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadInbox();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top Brand Header
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        actions.setOpaque(false);

        JButton compose = StuRentTheme.createPrimaryButton("+ New Message");
        compose.addActionListener(e -> new SendMessageDialog(this, userId, 2).setVisible(true));

        JButton refresh = StuRentTheme.createSecondaryButton("Refresh");
        refresh.addActionListener(e -> loadInbox());

        actions.add(compose);
        actions.add(refresh);

        JPanel header = StuRentTheme.createHeader(actions);
        add(header, BorderLayout.NORTH);

        // Center Panel
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Inbox & Messages");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        countLabel.setFont(StuRentTheme.FONT_SUBTITLE);
        countLabel.setForeground(StuRentTheme.TEXT_MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(countLabel);
        main.add(titlePanel, BorderLayout.NORTH);

        // Scrollable cards
        messagesContainer.setLayout(new BoxLayout(messagesContainer, BoxLayout.Y_AXIS));
        messagesContainer.setOpaque(false);

        JScrollPane scroll = new JScrollPane(messagesContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    private void loadInbox() {
        messagesContainer.removeAll();
        try {
            List<Message> msgs = service.getInbox(userId);
            if (msgs == null || msgs.isEmpty()) {
                JPanel empty = StuRentTheme.createCard();
                empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
                JLabel l1 = new JLabel("No messages in your inbox.");
                l1.setFont(StuRentTheme.FONT_CARD_TITLE);
                l1.setForeground(StuRentTheme.TEXT_MUTED);
                JLabel l2 = new JLabel("Conversations with sellers and buyers will show up here.");
                l2.setFont(StuRentTheme.FONT_SMALL);
                l2.setForeground(StuRentTheme.TEXT_MUTED);
                empty.add(l1);
                empty.add(Box.createRigidArea(new Dimension(0, 4)));
                empty.add(l2);
                messagesContainer.add(empty);
                countLabel.setText("0 conversations");
            } else {
                countLabel.setText(msgs.size() + " message(s)");
                for (Message m : msgs) {
                    messagesContainer.add(createMessageCard(m));
                    messagesContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (Exception ex) {
            JPanel noticeCard = StuRentTheme.createCard();
            noticeCard.setLayout(new BorderLayout());
            noticeCard.setBackground(new Color(254, 243, 199));
            noticeCard.setBorder(BorderFactory.createLineBorder(new Color(253, 230, 138), 1));
            JLabel noticeText = new JLabel("<html><b>Demo Preview Mode:</b> Displaying sample messages (Database: " + ex.getMessage() + ")</html>");
            noticeText.setFont(StuRentTheme.FONT_SMALL);
            noticeText.setForeground(new Color(180, 83, 9));
            noticeCard.add(noticeText, BorderLayout.CENTER);
            messagesContainer.add(noticeCard);
            messagesContainer.add(Box.createRigidArea(new Dimension(0, 10)));

            messagesContainer.add(createMessageCard(new Message(1, 2, userId, "Hi! Is the dumbbell set still available for sale?", false, null)));
            messagesContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            messagesContainer.add(createMessageCard(new Message(2, 3, userId, "Can we meet at the library today at 4 PM?", true, null)));
            messagesContainer.add(Box.createRigidArea(new Dimension(0, 10)));
            messagesContainer.add(createMessageCard(new Message(3, 4, userId, "Thanks for returning the calculator on time!", true, null)));

            countLabel.setText("3 messages (Demo Preview)");
        }
        messagesContainer.revalidate();
        messagesContainer.repaint();
    }

    private JPanel createMessageCard(Message m) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout(16, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 85));

        // Left Avatar
        JPanel avatar = new JPanel(new GridBagLayout());
        avatar.setPreferredSize(new Dimension(50, 50));
        avatar.setBackground(new Color(224, 242, 254));
        avatar.setBorder(BorderFactory.createLineBorder(new Color(186, 230, 253), 1));
        JLabel avText = new JLabel("U" + m.getSenderId());
        avText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        avText.setForeground(new Color(3, 105, 161));
        avatar.add(avText);

        // Center Text
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel sender = new JLabel("User #" + m.getSenderId());
        sender.setFont(StuRentTheme.FONT_CARD_TITLE);
        sender.setForeground(StuRentTheme.TEXT_DARK);

        JLabel content = new JLabel(m.getContent());
        content.setFont(StuRentTheme.FONT_REGULAR);
        content.setForeground(StuRentTheme.TEXT_MUTED);

        center.add(sender);
        center.add(Box.createRigidArea(new Dimension(0, 4)));
        center.add(content);

        // Right button
        JButton chatBtn = StuRentTheme.createPrimaryButton("Open Chat");
        chatBtn.addActionListener(e -> new ConversationFrame(userId, m.getSenderId()).setVisible(true));

        card.add(avatar, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        card.add(chatBtn, BorderLayout.EAST);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new InboxFrame(1).setVisible(true));
    }
}
