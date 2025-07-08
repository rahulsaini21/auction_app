package com.auction.bidding.dto;


public class LeaderboardEntry {
    private String userId;
    private String username;
    private double highestBid;
    private int position;

    // Constructors, getters and setters
    public LeaderboardEntry(String userId, String username, double highestBid, int position) {
        this.userId = userId;
        this.username = username;
        this.highestBid = highestBid;
        this.position = position;
    }

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getHighestBid() {
        return highestBid;
    }

    public void setHighestBid(double highestBid) {
        this.highestBid = highestBid;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}