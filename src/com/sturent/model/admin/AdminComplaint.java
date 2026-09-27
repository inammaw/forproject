package com.sturent.model.admin;

import java.sql.Timestamp;

public class AdminComplaint {

    private int complaintId;
    private String userId;
    private int itemId;
    private String complaintType;
    private String description;
    private String status;
    private String adminResponse;
    private Timestamp createdAt;
    private Timestamp resolvedAt;

    public AdminComplaint(
            int complaintId,
            String userId,
            int itemId,
            String complaintType,
            String description,
            String status,
            String adminResponse,
            Timestamp createdAt,
            Timestamp resolvedAt
    ) {
        this.complaintId = complaintId;
        this.userId = userId;
        this.itemId = itemId;
        this.complaintType = complaintType;
        this.description = description;
        this.status = status;
        this.adminResponse = adminResponse;
        this.createdAt = createdAt;
        this.resolvedAt = resolvedAt;
    }

    public int getComplaintId() {
        return complaintId;
    }

    public String getUserId() {
        return userId;
    }

    public int getItemId() {
        return itemId;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public String getDescription() {
        return description;
    }

    public String getStatus() {
        return status;
    }

    public String getAdminResponse() {
        return adminResponse;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getResolvedAt() {
        return resolvedAt;
    }
}