package com.auction.auction.dto;

import com.auction.auction.model.Auction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuctionResponse {
    private String id;
    private String title;
    private String description;
    private Double startingPrice;
    private Double minimumIncrement;
    private Date startTime;
    private Date endTime;
    private Integer biddingIntervalMinutes;
    private String sellerId;
    private Auction.AuctionStatus status;
}