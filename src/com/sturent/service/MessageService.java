package com.sturent.service;

import com.sturent.dao.MessageDAO;
import com.sturent.model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    private final MessageDAO messageDAO;

    public MessageService() { this(new MessageDAO()); }
    public MessageService(MessageDAO messageDAO) { this.messageDAO = messageDAO; }

    public boolean sendMessage(int senderId, int receiverId, String content) throws SQLException {
        if (senderId <= 0 || receiverId <= 0 || content == null || content.isBlank())
            throw new IllegalArgumentException("Valid sender, receiver and message are required.");
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content.trim());
        message.setRead(false);
        return messageDAO.send(message);
    }

    public List<Message> getConversation(int userA, int userB) throws SQLException {
        return messageDAO.findConversation(userA, userB);
    }

    public List<Message> getInbox(int userId) throws SQLException {
        return messageDAO.findInbox(userId);
    }

    public boolean markAsRead(int messageId) throws SQLException {
        return messageDAO.markAsRead(messageId);
    }
}
