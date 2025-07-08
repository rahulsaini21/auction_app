package com.auction.bidding.dto;

public class BidResponse {
    private String bidId;
    private String status;
    private String message;

    // Constructors, getters and setters
    public BidResponse(String bidId, String status, String message) {
        this.bidId = bidId;
        this.status = status;
        this.message = message;
    }

    public String getBidId() {
        return bidId;
    }

    public void setBidId(String bidId) {
        this.bidId = bidId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}