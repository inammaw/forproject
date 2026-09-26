package com.sturent.ui.messaging;

import com.sturent.model.Message;
import com.sturent.service.MessageService;

import javax.swing.*;
import java.awt.*;

public class ConversationFrame extends JFrame {
    private final int userId;
    private final int otherUserId;
    private final MessageService service;
    private final JTextArea conversation = new JTextArea();
    private final JTextField input = new JTextField();

    public ConversationFrame(int userId, int otherUserId) {
        this(userId, otherUserId, new MessageService());
    }

    public ConversationFrame(int userId, int otherUserId, MessageService service) {
        this.userId = userId; this.otherUserId = otherUserId; this.service = service;
        setTitle("Conversation");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); setLocationRelativeTo(null);
        buildUI(); loadConversation();
    }

    private void buildUI() {
        setLayout(new BorderLayout(5, 5));
        conversation.setEditable(false);
        add(new JScrollPane(conversation), BorderLayout.CENTER);

        JPanel send = new JPanel(new BorderLayout(5, 5));
        JButton button = new JButton("Send");
        send.add(input, BorderLayout.CENTER);
        send.add(button, BorderLayout.EAST);
        add(send, BorderLayout.SOUTH);
        button.addActionListener(e -> send());
    }

    private void loadConversation() {
        try {
            StringBuilder sb = new StringBuilder();
            for (Message m : service.getConversation(userId, otherUserId))
                sb.append(m.getSenderId()).append(": ").append(m.getContent()).append("\n");
            conversation.setText(sb.toString());
        } catch (Exception ex) { conversation.setText(ex.getMessage()); }
    }

    private void send() {
        try {
            service.sendMessage(userId, otherUserId, input.getText());
            input.setText("");
            loadConversation();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
