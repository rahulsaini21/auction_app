package com.auction.bidding.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuctionEvent {
    private String auctionId;
    private String eventType;  // "AUCTION_STARTED", "AUCTION_ENDED", "AUCTION_EXTENDED"
    private Long newEndTime;   // For extensions
}