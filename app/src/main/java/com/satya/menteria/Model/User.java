package com.satya.menteria.Model;

import java.util.ArrayList;

public class User {
    private String username;
    private String codeforcesHandle;
    private String codeforcesRating;
    private String levelPool;
    private String imageUrl;
    private String mentor = "NO_MENTOR_ASSIGNED";
    private ArrayList<String> mentees;


    public User(){}

    public User(String username, String codeforcesHandle, String codeforcesRating, String levelPool, String imageUrl) {
        this.username = username;
        this.codeforcesHandle = codeforcesHandle;
        this.codeforcesRating = codeforcesRating;
        this.levelPool = levelPool;
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodeforcesHandle() {
        return codeforcesHandle;
    }

    public void setCodeforcesHandle(String codeforcesHandle) {
        this.codeforcesHandle = codeforcesHandle;
    }

    public String getCodeforcesRating() {
        return codeforcesRating;
    }

    public void setCodeforcesRating(String codeforcesRating) {
        this.codeforcesRating = codeforcesRating;
    }

    public String getLevelPool() {
        return levelPool;
    }

    public void setLevelPool(String levelPool) {
        this.levelPool = levelPool;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMentor() {
        return mentor;
    }

    public void setMentor(String mentor) {
        this.mentor = mentor;
    }

    public ArrayList<String> getMentees() {
        return mentees;
    }

    public void setMentees(ArrayList<String> mentees) {
        this.mentees = mentees;
    }
}
