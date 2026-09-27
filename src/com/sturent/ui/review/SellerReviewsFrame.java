package com.sturent.ui.review;

import com.sturent.model.Review;
import com.sturent.service.ReviewService;
import com.sturent.ui.StuRentTheme;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class SellerReviewsFrame extends JFrame {
    private final int sellerId;
    private final ReviewService service;
    private final JPanel reviewsContainer = new JPanel();
    private final JLabel countLabel = new JLabel("All reviews");

    public SellerReviewsFrame(int sellerId) { this(sellerId, new ReviewService()); }

    public SellerReviewsFrame(int sellerId, ReviewService service) {
        this.sellerId = sellerId;
        this.service = service;

        setTitle("StuRent - Seller Reviews (User #" + sellerId + ")");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 640);
        setLocationRelativeTo(null);
        getContentPane().setBackground(StuRentTheme.BG_CANVAS);

        buildUI();
        loadReviews();
    }

    private void buildUI() {
        setLayout(new BorderLayout());

        // Header
        JButton refresh = StuRentTheme.createSecondaryButton("Refresh");
        refresh.addActionListener(e -> loadReviews());
        JPanel header = StuRentTheme.createHeader(refresh);
        add(header, BorderLayout.NORTH);

        // Center Panel
        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setOpaque(false);
        main.setBorder(new EmptyBorder(24, 32, 24, 32));

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("Seller Reviews & Reputation");
        title.setFont(StuRentTheme.FONT_TITLE);
        title.setForeground(StuRentTheme.TEXT_DARK);

        countLabel.setFont(StuRentTheme.FONT_SUBTITLE);
        countLabel.setForeground(StuRentTheme.TEXT_MUTED);

        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 4)));
        titlePanel.add(countLabel);
        main.add(titlePanel, BorderLayout.NORTH);

        // Scrollable reviews
        reviewsContainer.setLayout(new BoxLayout(reviewsContainer, BoxLayout.Y_AXIS));
        reviewsContainer.setOpaque(false);

        JScrollPane scroll = new JScrollPane(reviewsContainer);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        main.add(scroll, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    private void loadReviews() {
        reviewsContainer.removeAll();
        try {
            List<Review> list = service.getSellerReviews(sellerId);
            if (list == null || list.isEmpty()) {
                JPanel empty = StuRentTheme.createCard();
                empty.setLayout(new BoxLayout(empty, BoxLayout.Y_AXIS));
                JLabel l1 = new JLabel("No reviews received yet for this seller.");
                l1.setFont(StuRentTheme.FONT_CARD_TITLE);
                l1.setForeground(StuRentTheme.TEXT_MUTED);
                JLabel l2 = new JLabel("Completed sales and rentals will generate campus feedback here.");
                l2.setFont(StuRentTheme.FONT_SMALL);
                l2.setForeground(StuRentTheme.TEXT_MUTED);
                empty.add(l1);
                empty.add(Box.createRigidArea(new Dimension(0, 4)));
                empty.add(l2);
                reviewsContainer.add(empty);
                countLabel.setText("0 reviews recorded");
            } else {
                countLabel.setText(list.size() + " review(s) for Seller #" + sellerId);
                for (Review r : list) {
                    reviewsContainer.add(createReviewCard(r));
                    reviewsContainer.add(Box.createRigidArea(new Dimension(0, 10)));
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Review Error", JOptionPane.ERROR_MESSAGE);
        }
        reviewsContainer.revalidate();
        reviewsContainer.repaint();
    }

    private JPanel createReviewCard(Review r) {
        JPanel card = StuRentTheme.createCard();
        card.setLayout(new BorderLayout(14, 0));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

        // Rating Badge
        JPanel left = new JPanel(new GridBagLayout());
        left.setPreferredSize(new Dimension(80, 55));
        left.setBackground(new Color(254, 243, 199));
        left.setBorder(BorderFactory.createLineBorder(new Color(253, 230, 138), 1));
        JLabel starText = new JLabel(r.getRating() + " ★");
        starText.setFont(new Font("Segoe UI", Font.BOLD, 16));
        starText.setForeground(new Color(180, 83, 9));
        left.add(starText);

        // Center comment
        JPanel center = new JPanel();
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        center.setOpaque(false);

        JLabel author = new JLabel("Review by Student #" + r.getReviewerId());
        author.setFont(StuRentTheme.FONT_CARD_TITLE);
        author.setForeground(StuRentTheme.TEXT_DARK);

        JLabel comment = new JLabel("<html><p style=\"width: 480px;\">\"" + r.getComment() + "\"</p></html>");
        comment.setFont(StuRentTheme.FONT_REGULAR);
        comment.setForeground(StuRentTheme.TEXT_MUTED);

        center.add(author);
        center.add(Box.createRigidArea(new Dimension(0, 4)));
        center.add(comment);

        card.add(left, BorderLayout.WEST);
        card.add(center, BorderLayout.CENTER);
        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SellerReviewsFrame(2).setVisible(true));
    }
}
