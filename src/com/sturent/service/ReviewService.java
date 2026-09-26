package com.sturent.service;

import com.sturent.dao.ReviewDAO;
import com.sturent.model.Review;

import java.sql.SQLException;
import java.util.List;

public class ReviewService {
    private final ReviewDAO reviewDAO;

    public ReviewService() { this(new ReviewDAO()); }
    public ReviewService(ReviewDAO reviewDAO) { this.reviewDAO = reviewDAO; }

    public boolean addReview(Review review) throws SQLException {
        if (review == null || review.getReviewerId() <= 0 || review.getSellerId() <= 0
                || review.getItemId() <= 0 || review.getRating() < 1 || review.getRating() > 5)
            throw new IllegalArgumentException("Invalid review.");
        return reviewDAO.add(review);
    }

    public List<Review> getSellerReviews(int sellerId) throws SQLException {
        return reviewDAO.findBySellerId(sellerId);
    }

    public List<Review> getItemReviews(int itemId) throws SQLException {
        return reviewDAO.findByItemId(itemId);
    }

    public boolean deleteReview(int reviewId) throws SQLException {
        return reviewDAO.delete(reviewId);
    }
}
