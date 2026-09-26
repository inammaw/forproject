package com.sturent.ui.review;

import com.sturent.model.Review;
import com.sturent.service.ReviewService;

import javax.swing.*;
import java.awt.*;

public class ReviewFrame extends JFrame {
    private final int reviewerId;
    private final int sellerId;
    private final int itemId;
    private final ReviewService service;
    private final JSpinner rating = new JSpinner(new SpinnerNumberModel(5, 1, 5, 1));
    private final JTextArea comment = new JTextArea(6, 35);

    public ReviewFrame(int reviewerId, int sellerId, int itemId) {
        this(reviewerId, sellerId, itemId, new ReviewService());
    }

    public ReviewFrame(int reviewerId, int sellerId, int itemId, ReviewService service) {
        this.reviewerId = reviewerId; this.sellerId = sellerId; this.itemId = itemId; this.service = service;
        setTitle("Write Review");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 350); setLocationRelativeTo(null);
        buildUI();
    }

    private void buildUI() {
        setLayout(new BorderLayout(8, 8));
        JPanel top = new JPanel();
        top.add(new JLabel("Rating:"));
        top.add(rating);
        add(top, BorderLayout.NORTH);
        add(new JScrollPane(comment), BorderLayout.CENTER);
        JButton submit = new JButton("Submit Review");
        submit.addActionListener(e -> submit());
        add(submit, BorderLayout.SOUTH);
    }

    private void submit() {
        try {
            Review review = new Review();
            review.setReviewerId(reviewerId);
            review.setSellerId(sellerId);
            review.setItemId(itemId);
            review.setRating((Integer) rating.getValue());
            review.setComment(comment.getText().trim());
            service.addReview(review);
            JOptionPane.showMessageDialog(this, "Review submitted.");
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
