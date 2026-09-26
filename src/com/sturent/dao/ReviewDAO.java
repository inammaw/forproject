package com.sturent.dao;

import com.sturent.config.DBConnection;
import com.sturent.model.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public boolean add(Review review) throws SQLException {
        String sql = "INSERT INTO reviews (reviewer_id, seller_id, item_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, review.getReviewerId());
            ps.setInt(2, review.getSellerId());
            ps.setInt(3, review.getItemId());
            ps.setInt(4, review.getRating());
            ps.setString(5, review.getComment());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Review> findBySellerId(int sellerId) throws SQLException {
        String sql = "SELECT review_id, reviewer_id, seller_id, item_id, rating, comment, created_at " +
                     "FROM reviews WHERE seller_id = ? ORDER BY created_at DESC";
        return query(sql, sellerId);
    }

    public List<Review> findByItemId(int itemId) throws SQLException {
        String sql = "SELECT review_id, reviewer_id, seller_id, item_id, rating, comment, created_at " +
                     "FROM reviews WHERE item_id = ? ORDER BY created_at DESC";
        return query(sql, itemId);
    }

    public boolean delete(int reviewId) throws SQLException {
        String sql = "DELETE FROM reviews WHERE review_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, reviewId);
            return ps.executeUpdate() > 0;
        }
    }

    private List<Review> query(String sql, int id) throws SQLException {
        List<Review> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp timestamp = rs.getTimestamp("created_at");
                    result.add(new Review(
                        rs.getInt("review_id"),
                        rs.getInt("reviewer_id"),
                        rs.getInt("seller_id"),
                        rs.getInt("item_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        timestamp == null ? null : timestamp.toLocalDateTime()
                    ));
                }
            }
        }
        return result;
    }
}
