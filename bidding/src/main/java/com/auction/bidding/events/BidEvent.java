package com.auction.bidding.events;

import com.auction.bidding.model.Bid;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidEvent {
    private String eventId;
    private String eventType;  // "BID_PLACED", "BID_REJECTED", "AUCTION_EXTENDED"
    private Bid bid;
    private String reason;     // Rejection reason (if any)
}