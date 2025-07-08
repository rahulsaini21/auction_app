package com.auction.bidding.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "bids")
public class Bid {
    @Id
    private String id;
    private String auctionId;
    private String userId;
    private double amount;
    private Date bidTime;
    private BidStatus status;

    // Constructors, getters and setters
    public Bid() {
    }

    public Bid(String auctionId, String userId, double amount, Date bidTime, BidStatus status) {
        this.auctionId = auctionId;
        this.userId = userId;
        this.amount = amount;
        this.bidTime = bidTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getBidTime() {
        return bidTime;
    }

    public void setBidTime(Date bidTime) {
        this.bidTime = bidTime;
    }

    public BidStatus getStatus() {
        return status;
    }

    public void setStatus(BidStatus status) {
        this.status = status;
    }
}