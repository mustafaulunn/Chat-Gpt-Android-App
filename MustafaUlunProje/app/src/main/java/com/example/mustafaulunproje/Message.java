package com.example.mustafaulunproje;

import java.util.List;

public class Message {
    public static String SENT_BY_ME="Ben";
    public static String SENT_BY_BOT="Bot";
    String message;
    String sentBy;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;

    }

    public void setSentBy(String sentBy) {
        this.sentBy = sentBy;
    }

    public String getSentBy() {
        return sentBy;
    }

    public Message(String message, String sentBy) {
        this.message = message;
        this.sentBy=sentBy;
    }
}
