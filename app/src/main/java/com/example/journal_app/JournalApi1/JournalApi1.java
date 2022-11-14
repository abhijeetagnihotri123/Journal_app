package com.example.journal_app.JournalApi1;

import android.app.Application;

public class JournalApi1 extends Application
{
    private String username;
    private String userId;
    private static JournalApi1 instance;

    JournalApi1(){

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public static JournalApi1 getInstance()
    {
        if(instance == null)
        {
            instance = new JournalApi1();
        }
        return instance;
    }
}
