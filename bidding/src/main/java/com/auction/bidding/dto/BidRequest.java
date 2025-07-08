package com.auction.bidding.dto;


public class BidRequest {
    private String auctionId;
    
    private double amount;

    // Getters and setters
    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}