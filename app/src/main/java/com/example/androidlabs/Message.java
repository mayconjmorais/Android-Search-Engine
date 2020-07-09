package com.example.androidlabs;

public class Message {
    long id;
    String message;
    boolean side;

    public Message(String message, boolean side, long id) {
        this.id = id;
        this.message = message;
        this.side = side;
    }

    public long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSide() {
        return side;
    }

    public void setSide(boolean side) {
        this.side = side;
    }
}