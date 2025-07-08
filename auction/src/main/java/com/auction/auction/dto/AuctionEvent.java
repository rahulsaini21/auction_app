package com.auction.auction.dto;

import java.util.Date;

import com.auction.auction.model.Auction;

import lombok.Data;

@Data
public class AuctionEvent {
    private String auctionId;
    private String title;
    private Auction.AuctionStatus status;
    private String sellerId;
    private String sellerUsername;
    private Date startTime;
    private Date endTime;
    private AuctionEventType eventType;
    
    public AuctionEvent(Auction auction, AuctionEventType eventType) {
        this.auctionId = auction.getId();
        this.title = auction.getTitle();
        this.status = auction.getStatus();
        this.sellerId = auction.getSeller();
        this.sellerUsername = auction.getSeller();
        this.startTime = auction.getStartTime();
        this.endTime = auction.getEndTime();
        this.eventType = eventType;
    }
    
    public enum AuctionEventType {
        CREATED, STARTED, ENDED, CANCELLED
    }
}