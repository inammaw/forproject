package com.sturent.ui.review;

import com.sturent.model.Review;
import com.sturent.service.ReviewService;

import javax.swing.*;
import java.awt.*;

public class SellerReviewsFrame extends JFrame {
    private final int sellerId;
    private final ReviewService service;
    private final DefaultListModel<String> model = new DefaultListModel<>();

    public SellerReviewsFrame(int sellerId) { this(sellerId, new ReviewService()); }

    public SellerReviewsFrame(int sellerId, ReviewService service) {
        this.sellerId = sellerId; this.service = service;
        setTitle("Seller Reviews");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500); setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(new JScrollPane(new JList<>(model)), BorderLayout.CENTER);
        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadReviews());
        add(refresh, BorderLayout.SOUTH);
        loadReviews();
    }

    private void loadReviews() {
        model.clear();
        try {
            for (Review r : service.getSellerReviews(sellerId))
                model.addElement("Rating: " + r.getRating() + "/5 | " + r.getComment());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
