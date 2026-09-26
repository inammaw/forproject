package com.sturent.ui.messaging;

import com.sturent.model.Message;
import com.sturent.service.MessageService;

import javax.swing.*;
import java.awt.*;

public class InboxFrame extends JFrame {
    private final int userId;
    private final MessageService service;
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public InboxFrame(int userId) { this(userId, new MessageService()); }

    public InboxFrame(int userId, MessageService service) {
        this.userId = userId; this.service = service;
        setTitle("Inbox");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(750, 500); setLocationRelativeTo(null);
        buildUI(); loadInbox();
    }

    private void buildUI() {
        setLayout(new BorderLayout());
        add(new JScrollPane(new JList<>(model)), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadInbox());
        add(refresh, BorderLayout.SOUTH);
    }

    private void loadInbox() {
        model.clear();
        try {
            for (Message m : service.getInbox(userId))
                model.addElement("From #" + m.getSenderId() + " | " + m.getContent());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
