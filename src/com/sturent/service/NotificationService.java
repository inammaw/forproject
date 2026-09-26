package com.sturent.service;

import com.sturent.dao.NotificationDAO;
import com.sturent.model.Notification;

import java.sql.SQLException;
import java.util.List;

public class NotificationService {
    private final NotificationDAO notificationDAO;

    public NotificationService() { this(new NotificationDAO()); }
    public NotificationService(NotificationDAO notificationDAO) { this.notificationDAO = notificationDAO; }

    public boolean notifyUser(int userId, String title, String message) throws SQLException {
        if (userId <= 0 || title == null || title.isBlank() || message == null || message.isBlank())
            throw new IllegalArgumentException("Valid notification data is required.");
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title.trim());
        n.setMessage(message.trim());
        n.setRead(false);
        return notificationDAO.create(n);
    }

    public List<Notification> getNotifications(int userId) throws SQLException {
        return notificationDAO.findByUserId(userId);
    }

    public boolean markAsRead(int notificationId) throws SQLException {
        return notificationDAO.markAsRead(notificationId);
    }

    public int getUnreadCount(int userId) throws SQLException {
        return notificationDAO.countUnread(userId);
    }
}
