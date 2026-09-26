package com.sturent.ui.messaging;

import com.sturent.service.MessageService;

import javax.swing.*;
import java.awt.*;

public class SendMessageDialog extends JDialog {
    public SendMessageDialog(Frame owner, int senderId, int receiverId) {
        this(owner, senderId, receiverId, new MessageService());
    }

    public SendMessageDialog(Frame owner, int senderId, int receiverId, MessageService service) {
        super(owner, "Send Message", true);
        setSize(450, 250);
        setLocationRelativeTo(owner);

        JTextArea text = new JTextArea(6, 30);
        JButton send = new JButton("Send");
        send.addActionListener(e -> {
            try {
                service.sendMessage(senderId, receiverId, text.getText());
                JOptionPane.showMessageDialog(this, "Message sent.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        setLayout(new BorderLayout(8, 8));
        add(new JScrollPane(text), BorderLayout.CENTER);
        add(send, BorderLayout.SOUTH);
    }
}
