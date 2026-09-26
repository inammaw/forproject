package com.sturent.dao;

import com.sturent.config.DBConnection;
import com.sturent.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    public boolean create(Notification notification) throws SQLException {
        String sql = "INSERT INTO notifications (user_id, title, message, is_read) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, notification.getUserId());
            ps.setString(2, notification.getTitle());
            ps.setString(3, notification.getMessage());
            ps.setBoolean(4, notification.isRead());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Notification> findByUserId(int userId) throws SQLException {
        String sql = "SELECT notification_id, user_id, title, message, is_read, created_at " +
                     "FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        List<Notification> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(map(rs));
            }
        }
        return result;
    }

    public boolean markAsRead(int notificationId) throws SQLException {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notification_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, notificationId);
            return ps.executeUpdate() > 0;
        }
    }

    public int countUnread(int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private Notification map(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("created_at");
        return new Notification(
            rs.getInt("notification_id"),
            rs.getInt("user_id"),
            rs.getString("title"),
            rs.getString("message"),
            rs.getBoolean("is_read"),
            timestamp == null ? null : timestamp.toLocalDateTime()
        );
    }
}
