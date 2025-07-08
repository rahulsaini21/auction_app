package com.auction.bidding.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "auctions")
public class Auction {
    @Id
    private String id;
    private String title;
    private String description;
    private Double startingPrice;
    private Double minimumIncrement;
    private Date startTime;
    private Date endTime;
    private Integer biddingIntervalMinutes;
    
    private String seller;
    private AuctionStatus status;

    public enum AuctionStatus {
        PENDING, ACTIVE, COMPLETED, CANCELLED
    }
}