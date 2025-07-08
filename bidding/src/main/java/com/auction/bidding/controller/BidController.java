package com.auction.bidding.controller;

import com.auction.bidding.dto.BidRequest;
import com.auction.bidding.dto.BidResponse;
import com.auction.bidding.dto.LeaderboardEntry;
import com.auction.bidding.dto.UserPositionResponse;
import com.auction.bidding.dto.UserProfile;
import com.auction.bidding.exception.BidException;
import com.auction.bidding.exception.InvalidAuctionException;
import com.auction.bidding.exception.RateLimitException;
import com.auction.bidding.model.Bid;
import com.auction.bidding.model.BidStatus;
import com.auction.bidding.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/bids")
public class BidController {

    private static final Logger logger = LoggerFactory.getLogger(BidController.class);

    private final BidService bidService;
    private final AuthServiceClient authServiceClient;
    private final RateLimitService rateLimitService;
    private final KafkaProducerService kafkaProducerService;

    public BidController(BidService bidService, 
                        AuthServiceClient authServiceClient,
                        RateLimitService rateLimitService,
                        KafkaProducerService kafkaProducerService) {
        this.bidService = bidService;
        this.authServiceClient = authServiceClient;
        this.rateLimitService = rateLimitService;
        this.kafkaProducerService = kafkaProducerService;
    }

    @PostMapping
    public ResponseEntity<?> placeBid(@RequestBody BidRequest bidRequest,
                                     @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserProfile user = authServiceClient.getUserFromToken(token);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            // Check rate limit
            rateLimitService.checkRateLimit(bidRequest.getAuctionId(), user.getId());

            // Process bid
            BidResponse response = bidService.processBid(bidRequest, user.getId());

            // Create bid object for Kafka
            Bid bid = new Bid();
            bid.setId(response.getBidId());
            bid.setAuctionId(bidRequest.getAuctionId());
            bid.setUserId(user.getId());
            bid.setAmount(bidRequest.getAmount());
            bid.setBidTime(new Date());
            bid.setStatus(BidStatus.PENDING);

            // Send to Kafka
            kafkaProducerService.sendBid(bid);

            return ResponseEntity.ok(response);
        } catch (RateLimitException e) {
            logger.warn("Rate limit exceeded: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(e.getMessage());
        } catch (InvalidAuctionException e) {
            logger.warn("Invalid auction: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (BidException e) {
            logger.warn("Bid error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            logger.error("Error placing bid", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/leaderboard/{auctionId}")
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard(
            @PathVariable String auctionId,
            @RequestParam(defaultValue = "10") int limit) {
        try {
            List<LeaderboardEntry> leaderboard = bidService.getTopBidders(auctionId, limit);
            return ResponseEntity.ok(leaderboard);
        } catch (Exception e) {
            logger.error("Error getting leaderboard", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/position/{auctionId}")
    public ResponseEntity<UserPositionResponse> getUserPosition(
            @PathVariable String auctionId,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            UserProfile user = authServiceClient.getUserFromToken(token);
            
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            UserPositionResponse position = bidService.getUserPosition(auctionId, user.getId());
            return ResponseEntity.ok(position);
        } catch (Exception e) {
            logger.error("Error getting user position", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}