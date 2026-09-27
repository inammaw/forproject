package com.sturent.ui.messaging;

import com.sturent.model.Message;
import com.sturent.service.MessageService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ConversationFrame extends JFrame {
    private final int userId;
    private final int otherUserId;
    private final MessageService service;
    private final JPanel chatBox = new JPanel();
    private final JTextField input = StuRentTheme.createTextField(30);

    public ConversationFrame(int userId, int otherUserId) {
        this(userId, otherUserId, new MessageService());
    }

    public ConversationFrame(int userId, int otherUserId, MessageService service) {
        this.userId = userId;
        this.otherUserId = otherUserId;
        this.service = service;

        setTitle("StuRent - Chat with User #" + otherUserId);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 640);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadConversation();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Top Brand Header
        JButton close = StuRentTheme.createSecondaryButton("Close");
        close.addActionListener(e -> dispose());
        JPanel header = StuRentTheme.createHeader(close);
        add(header, BorderLayout.NORTH);

        // Main Panel
        JPanel main = new JPanel(new BorderLayout(12, 12));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(16, 24, 20, 24));

        // Subheader
        JPanel userBanner = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        userBanner.setOpaque(false);
        JLabel userTitle = new JLabel("Conversation with User #" + otherUserId);
        userTitle.setFont(StuRentTheme.FONT_TITLE);
        userTitle.setForeground(StuRentTheme.TEXT_DARK);
        JLabel status = StuRentTheme.createBadge("CAMPUS MEMBER", StuRentTheme.BADGE_GREEN_BG, StuRentTheme.BADGE_GREEN_TEXT);
        userBanner.add(userTitle);
        userBanner.add(status);
        main.add(userBanner, BorderLayout.NORTH);

        // Center: Chat History Card
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout());

        chatBox.setLayout(new BoxLayout(chatBox, BoxLayout.Y_AXIS));
        chatBox.setOpaque(false);

        JScrollPane scroll = new JScrollPane(chatBox);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        card.add(scroll, BorderLayout.CENTER);

        // Bottom send row
        JPanel sendRow = new JPanel(new BorderLayout(10, 0));
        sendRow.setOpaque(false);
        sendRow.setBorder(new EmptyBorder(12, 0, 0, 0));

        JButton sendBtn = StuRentTheme.createPrimaryButton("Send Message");
        sendBtn.addActionListener(e -> send());
        input.addActionListener(e -> send());

        sendRow.add(input, BorderLayout.CENTER);
        sendRow.add(sendBtn, BorderLayout.EAST);
        card.add(sendRow, BorderLayout.SOUTH);

        main.add(card, BorderLayout.CENTER);
        add(main, BorderLayout.CENTER);
    }

    private void loadConversation() {
        chatBox.removeAll();
        try {
            List<Message> msgs = service.getConversation(userId, otherUserId);
            if (msgs == null || msgs.isEmpty()) {
                JLabel empty = new JLabel("No messages yet. Say hello to start the conversation!", SwingConstants.CENTER);
                empty.setFont(StuRentTheme.FONT_REGULAR);
                empty.setForeground(StuRentTheme.TEXT_MUTED);
                empty.setAlignmentX(Component.CENTER_ALIGNMENT);
                chatBox.add(Box.createVerticalGlue());
                chatBox.add(empty);
                chatBox.add(Box.createVerticalGlue());
            } else {
                for (Message m : msgs) {
                    boolean isMe = m.getSenderId() == userId;
                    chatBox.add(createMessageBubble(m.getContent(), isMe));
                    chatBox.add(Box.createRigidArea(new Dimension(0, 8)));
                }
            }
        } catch (Exception ex) {
            JLabel err = new JLabel("Error: " + ex.getMessage());
            chatBox.add(err);
        }
        chatBox.revalidate();
        chatBox.repaint();
    }

    private JPanel createMessageBubble(String content, boolean isMe) {
        JPanel row = new JPanel(new FlowLayout(isMe ? FlowLayout.RIGHT : FlowLayout.LEFT, 4, 0));
        row.setOpaque(false);

        JPanel bubble = new JPanel(new BorderLayout());
        bubble.setBackground(isMe ? StuRentTheme.PRIMARY_GREEN : new Color(241, 245, 249));
        bubble.setBorder(new EmptyBorder(10, 14, 10, 14));

        JLabel text = new JLabel("<html><p style=\"width: 320px;\">" + content + "</p></html>");
        text.setFont(StuRentTheme.FONT_REGULAR);
        text.setForeground(isMe ? Color.WHITE : StuRentTheme.TEXT_DARK);

        bubble.add(text, BorderLayout.CENTER);
        row.add(bubble);
        return row;
    }

    private void send() {
        String txt = input.getText().trim();
        if (txt.isEmpty()) return;
        try {
            service.sendMessage(userId, otherUserId, txt);
            input.setText("");
            loadConversation();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Message Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ConversationFrame(1, 2).setVisible(true));
    }
}
