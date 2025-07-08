package com.auction.bidding.service;

import com.auction.bidding.model.Auction;
import com.auction.bidding.dto.BidRequest;
import com.auction.bidding.dto.BidResponse;
import com.auction.bidding.dto.LeaderboardEntry;
import com.auction.bidding.dto.UserPositionResponse;
import com.auction.bidding.exception.BidException;
import com.auction.bidding.exception.InvalidAuctionException;
import com.auction.bidding.model.Bid;
import com.auction.bidding.model.BidStatus;
import com.auction.bidding.repository.BidRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class BidService {

    private static final Logger logger = LoggerFactory.getLogger(BidService.class);

    private final BidRepository bidRepository;
    private final AuctionServiceClient auctionServiceClient;
    private final LeaderboardService leaderboardService;

    @Autowired
    public BidService(BidRepository bidRepository, 
                     AuctionServiceClient auctionServiceClient,
                     LeaderboardService leaderboardService) {
        this.bidRepository = bidRepository;
        this.auctionServiceClient = auctionServiceClient;
        this.leaderboardService = leaderboardService;
    }

    public BidResponse processBid(BidRequest bidRequest, String userId) {
        // Validate auction
        Auction auction = auctionServiceClient.getAuctionById(bidRequest.getAuctionId());
        if (auction == null) {
            throw new InvalidAuctionException("Auction not found");
        }

        // Check auction status
        if (auction.getStatus() == Auction.AuctionStatus.CANCELLED) {
            throw new BidException("Auction is not active");
        }

        // Check auction time window
        Date now = new Date();
        if (now.before(auction.getStartTime()) || now.after(auction.getEndTime())) {
            throw new BidException("Bid can only be placed during auction window");
        }

        // Check bid amount
        // double currentHighestBid = leaderboardService.getHighestBidForAuction(bidRequest.getAuctionId())
        //         .orElse(auction.getStartingPrice());

        // double minimumBid = currentHighestBid.add(auction.getMinimumIncrement());
        // if (bidRequest.getAmount().compareTo(minimumBid) < 0) {
        //     throw new BidException("Bid amount must be at least " + minimumBid);
        // }
        double currentHighestBid = leaderboardService.getHighestBidForAuction(bidRequest.getAuctionId())
                .orElse(auction.getStartingPrice());

        double minimumBid = currentHighestBid + auction.getMinimumIncrement();

        if (bidRequest.getAmount() < minimumBid) {
            throw new BidException("Bid amount must be at least " + minimumBid);
        }

        // Create and save bid
        Bid bid = new Bid();
        bid.setAuctionId(bidRequest.getAuctionId());
        bid.setUserId(userId);
        bid.setAmount(bidRequest.getAmount());
        bid.setBidTime(now);
        bid.setStatus(BidStatus.PENDING);

        Bid savedBid = bidRepository.save(bid);

        return new BidResponse(savedBid.getId(), "PENDING", "Bid is being processed");
    }

    public void acceptBid(Bid bid) {
        bid.setStatus(BidStatus.ACCEPTED);
        bidRepository.save(bid);
        
        // Update leaderboard
        leaderboardService.updateLeaderboard(bid.getAuctionId(), bid.getUserId(), bid.getAmount());
        
        // Mark lower bids from same user as OUTBID
        List<Bid> userBids = bidRepository.findByAuctionIdAndUserIdOrderByBidTimeDesc(
                bid.getAuctionId(), bid.getUserId());
        
        userBids.stream()
            .filter(b -> b.getAmount() < bid.getAmount())
            .forEach(b -> {
                b.setStatus(BidStatus.OUTBID);
                bidRepository.save(b);
            });
    }

    public void rejectBid(Bid bid, String reason) {
        bid.setStatus(BidStatus.REJECTED);
        bidRepository.save(bid);
    }

    public List<Bid> getBidsForAuction(String auctionId) {
        return bidRepository.findByAuctionIdOrderByAmountDesc(auctionId);
    }

    public List<LeaderboardEntry> getTopBidders(String auctionId, int limit) {
        return leaderboardService.getTopBidders(auctionId, limit);
    }

    public UserPositionResponse getUserPosition(String auctionId, String userId) {
        return leaderboardService.getUserPosition(auctionId, userId);
    }

}