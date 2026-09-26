package com.sturent.model;

import java.time.LocalDateTime;

public class Review {
    private int reviewId;
    private int reviewerId;
    private int sellerId;
    private int itemId;
    private int rating;
    private String comment;
    private LocalDateTime createdAt;

    public Review() {}

    public Review(int reviewId, int reviewerId, int sellerId, int itemId,
                  int rating, String comment, LocalDateTime createdAt) {
        this.reviewId = reviewId;
        this.reviewerId = reviewerId;
        this.sellerId = sellerId;
        this.itemId = itemId;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getReviewId() { return reviewId; }
    public void setReviewId(int reviewId) { this.reviewId = reviewId; }
    public int getReviewerId() { return reviewerId; }
    public void setReviewerId(int reviewerId) { this.reviewerId = reviewerId; }
    public int getSellerId() { return sellerId; }
    public void setSellerId(int sellerId) { this.sellerId = sellerId; }
    public int getItemId() { return itemId; }
    public void setItemId(int itemId) { this.itemId = itemId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
