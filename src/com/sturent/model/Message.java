package com.sturent.model;

import java.time.LocalDateTime;

public class Message {
    private int messageId;
    private int senderId;
    private int receiverId;
    private String content;
    private boolean read;
    private LocalDateTime sentAt;

    public Message() {}

    public Message(int messageId, int senderId, int receiverId, String content,
                   boolean read, LocalDateTime sentAt) {
        this.messageId = messageId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.read = read;
        this.sentAt = sentAt;
    }

    public int getMessageId() { return messageId; }
    public void setMessageId(int messageId) { this.messageId = messageId; }
    public int getSenderId() { return senderId; }
    public void setSenderId(int senderId) { this.senderId = senderId; }
    public int getReceiverId() { return receiverId; }
    public void setReceiverId(int receiverId) { this.receiverId = receiverId; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public boolean isRead() { return read; }
    public void setRead(boolean read) { this.read = read; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }
}
