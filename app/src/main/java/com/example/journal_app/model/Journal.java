package com.example.journal_app.model;

import com.google.firebase.Timestamp;

public class Journal
{
    private String title;
    private String thought;
    private String imageUri;
    private String userId;
    private Timestamp timestamp;
    private String username;

    public Journal(){

    }

    public Journal(String title, String thought, String imageUri, String userId, Timestamp timestamp, String username) {
        this.title = title;
        this.thought = thought;
        this.imageUri = imageUri;
        this.userId = userId;
        this.timestamp = timestamp;
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
