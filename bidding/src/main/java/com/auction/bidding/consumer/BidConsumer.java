package com.auction.bidding.consumer;

import com.auction.bidding.model.Bid;
import com.auction.bidding.service.BidService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class BidConsumer {

    private static final Logger logger = LoggerFactory.getLogger(BidConsumer.class);

    private final BidService bidService;

    public BidConsumer(BidService bidService) {
        this.bidService = bidService;
    }

    // @KafkaListener(topics = "${kafka.topic.bids}", groupId = "bid-processing-group")
    public void consumeBid(Bid bid) {
        logger.info("Received bid for processing: {}", bid);
        try {
            bidService.acceptBid(bid);
            logger.info("Bid processed successfully: {}", bid.getId());
        } catch (Exception e) {
            logger.error("Error processing bid: {}", bid.getId(), e);
            bidService.rejectBid(bid, e.getMessage());
        }
    }
}