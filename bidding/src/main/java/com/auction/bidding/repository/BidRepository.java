package com.auction.bidding.repository;

import com.auction.bidding.model.Bid;
import com.auction.bidding.model.BidStatus;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends MongoRepository<Bid, String> {
    List<Bid> findByAuctionIdOrderByAmountDesc(String auctionId);
    List<Bid> findByAuctionIdAndUserIdOrderByBidTimeDesc(String auctionId, String userId);
    List<Bid> findByAuctionIdAndStatus(String auctionId, BidStatus status);
}