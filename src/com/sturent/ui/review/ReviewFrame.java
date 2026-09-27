package com.sturent.ui.review;

import com.sturent.model.Review;
import com.sturent.service.ReviewService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ReviewFrame extends JFrame {
    private final int reviewerId;
    private final int sellerId;
    private final int itemId;
    private final ReviewService service;
    private final JComboBox<String> ratingCombo = new JComboBox<>(new String[]{
            "5 Stars - Excellent (★★★★★)",
            "4 Stars - Very Good (★★★★☆)",
            "3 Stars - Average (★★★☆☆)",
            "2 Stars - Poor (★★☆☆☆)",
            "1 Star - Terrible (★☆☆☆☆)"
    });
    private final JTextArea comment = new JTextArea(5, 30);

    public ReviewFrame(int reviewerId, int sellerId, int itemId) {
        this(reviewerId, sellerId, itemId, new ReviewService());
    }

    public ReviewFrame(int reviewerId, int sellerId, int itemId, ReviewService service) {
        this.reviewerId = reviewerId;
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.service = service;

        setTitle("StuRent - Write Review");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(650, 520);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        JButton close = StuRentTheme.createSecondaryButton("Close");
        close.addActionListener(e -> dispose());
        JPanel header = StuRentTheme.createHeader(close);
        add(header, BorderLayout.NORTH);

        // Center Panel
        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        JLabel title = new JLabel("Write a Review");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        JLabel sub = new JLabel("Rate your campus transaction for Item #" + itemId + " with Seller #" + sellerId);
        sub.setFont(StuRentTheme.FONT_SUBTITLE);
        sub.setForeground(StuRentTheme.TEXT_MUTED);

        main.add(title);
        main.add(Box.createRigidArea(new Dimension(0, 4)));
        main.add(sub);
        main.add(Box.createRigidArea(new Dimension(0, 16)));

        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));

        JLabel ratingLbl = new JLabel("Overall Rating:");
        ratingLbl.setFont(StuRentTheme.FONT_BOLD);
        ratingLbl.setForeground(StuRentTheme.TEXT_DARK);

        ratingCombo.setFont(StuRentTheme.FONT_REGULAR);
        ratingCombo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));

        JLabel commentLbl = new JLabel("Your Feedback & Experience:");
        commentLbl.setFont(StuRentTheme.FONT_BOLD);
        commentLbl.setForeground(StuRentTheme.TEXT_DARK);

        comment.setFont(StuRentTheme.FONT_REGULAR);
        comment.setLineWrap(true);
        comment.setWrapStyleWord(true);
        comment.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(StuRentTheme.CARD_BORDER, 1),
                new EmptyBorder(10, 12, 10, 12)
        ));

        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionRow.setOpaque(false);

        JButton cancelBtn = StuRentTheme.createSecondaryButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        JButton submitBtn = StuRentTheme.createPrimaryButton("Submit Review");
        submitBtn.addActionListener(e -> submit());

        actionRow.add(cancelBtn);
        actionRow.add(submitBtn);

        card.add(ratingLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(ratingCombo);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(commentLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(new JScrollPane(comment));
        card.add(Box.createRigidArea(new Dimension(0, 16)));
        card.add(actionRow);

        main.add(card);
        add(main, BorderLayout.CENTER);
    }

    private void submit() {
        try {
            int stars = 5 - ratingCombo.getSelectedIndex();
            Review review = new Review();
            review.setReviewerId(reviewerId);
            review.setSellerId(sellerId);
            review.setItemId(itemId);
            review.setRating(stars);
            review.setComment(comment.getText().trim());

            service.addReview(review);
            JOptionPane.showMessageDialog(this, "Thank you! Your review has been submitted.", "Review Submitted", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ReviewFrame(1, 2, 10).setVisible(true));
    }
}
