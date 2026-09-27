package com.sturent.ui.messaging;

import com.sturent.service.MessageService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class SendMessageDialog extends JDialog {
    public SendMessageDialog(Frame owner, int senderId, int receiverId) {
        this(owner, senderId, receiverId, new MessageService());
    }

    public SendMessageDialog(Frame owner, int senderId, int receiverId, MessageService service) {
        super(owner, "StuRent - Send Message", true);
        setSize(520, 360);
        setLocationRelativeTo(owner);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel title = new JLabel("Send Message to User #" + receiverId);
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JLabel sub = new JLabel("Inquire about an item or negotiate campus meetup details");
        sub.setFont(StuRentTheme.FONT_SUBTITLE);
        sub.setForeground(StuRentTheme.TEXT_MUTED);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 4)));
        main.add(sub);
        main.add(Box.createRigidArea(new Dimension(0, 14)));

        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout(10, 10));

        JTextArea text = new JTextArea(6, 30);
        text.setFont(StuRentTheme.FONT_REGULAR);
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        text.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StuRentTheme.CARD_BORDER, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRow.setOpaque(false);

        JButton cancel = StuRentTheme.createSecondaryButton("Cancel");
        cancel.addActionListener(e -> dispose());

        JButton send = StuRentTheme.createPrimaryButton("Send Message");
        send.addActionListener(e -> {
            try {
                String content = text.getText().trim();
                if (content.isEmpty()) return;
                service.sendMessage(senderId, receiverId, content);
                JOptionPane.showMessageDialog(this, "Message sent successfully!", "Sent", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnRow.add(cancel);
        btnRow.add(send);

        card.add(new JScrollPane(text), BorderLayout.CENTER);
        card.add(btnRow, BorderLayout.SOUTH);

        main.add(card);
        setContentPane(main);
    }
}
