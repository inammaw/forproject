package com.sturent.model.admin;

import java.sql.Timestamp;

public class AdminReview {

    private int reviewId;
    private int reviewerId;
    private int sellerId;
    private int itemId;
    private int rating;
    private String comment;
    private Timestamp createdAt;

    public AdminReview(
            int reviewId,
            int reviewerId,
            int sellerId,
            int itemId,
            int rating,
            String comment,
            Timestamp createdAt
    ) {
        this.reviewId = reviewId;
        this.reviewerId = reviewerId;
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() {
        return reviewId;
    }

    public int getReviewerId() {
        return reviewerId;
    }

    public int getSellerId() {
        return sellerId;
    }

    public int getItemId() {
        return itemId;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }
}