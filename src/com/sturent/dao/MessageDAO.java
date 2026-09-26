package com.sturent.dao;

import com.sturent.config.DBConnection;
import com.sturent.model.Message;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public boolean send(Message message) throws SQLException {
        String sql = "INSERT INTO messages (sender_id, receiver_id, content, is_read) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, message.getSenderId());
            ps.setInt(2, message.getReceiverId());
            ps.setString(3, message.getContent());
            ps.setBoolean(4, message.isRead());
            return ps.executeUpdate() > 0;
        }
    }

    public List<Message> findConversation(int userA, int userB) throws SQLException {
        String sql = "SELECT message_id, sender_id, receiver_id, content, is_read, sent_at " +
                     "FROM messages WHERE (sender_id = ? AND receiver_id = ?) " +
                     "OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_at ASC";
        List<Message> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userA);
            ps.setInt(2, userB);
            ps.setInt(3, userB);
            ps.setInt(4, userA);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(map(rs));
            }
        }
        return result;
    }

    public List<Message> findInbox(int receiverId) throws SQLException {
        String sql = "SELECT message_id, sender_id, receiver_id, content, is_read, sent_at " +
                     "FROM messages WHERE receiver_id = ? ORDER BY sent_at DESC";
        List<Message> result = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, receiverId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(map(rs));
            }
        }
        return result;
    }

    public boolean markAsRead(int messageId) throws SQLException {
        String sql = "UPDATE messages SET is_read = TRUE WHERE message_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, messageId);
            return ps.executeUpdate() > 0;
        }
    }

    private Message map(ResultSet rs) throws SQLException {
        Timestamp timestamp = rs.getTimestamp("sent_at");
        return new Message(
            rs.getInt("message_id"),
            rs.getInt("sender_id"),
            rs.getInt("receiver_id"),
            rs.getString("content"),
            rs.getBoolean("is_read"),
            timestamp == null ? null : timestamp.toLocalDateTime()
        );
    }
}
