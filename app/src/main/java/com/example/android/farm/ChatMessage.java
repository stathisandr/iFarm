package com.example.android.farm;

import java.util.Date;

/**
 * Created by Tasos on 03-Jan-18.
 */

public class ChatMessage {
    private String messageText;
    private String messageUser;
    private long messageTime;
    private String  problemid;


    public ChatMessage(String messageText, String messageUser, String problemid) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.problemid= problemid;

        messageTime = new Date().getTime();
    }

    public ChatMessage() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getProblemid() {
        return problemid;
    }

    public void setProblemid(String problemid) {
        this.problemid = problemid;
    }
}
