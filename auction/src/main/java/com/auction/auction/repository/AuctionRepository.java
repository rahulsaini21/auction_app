package com.auction.auction.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.auction.auction.model.Auction;
import com.auction.auction.model.Auction.AuctionStatus;

import java.util.Date;
import java.util.List;

public interface AuctionRepository extends MongoRepository<Auction, String> {
    List<Auction> findByStatus(AuctionStatus status);
    List<Auction> findBySeller(String seller);
    List<Auction> findByStartTimeBeforeAndEndTimeAfter(Date currentTime1, Date currentTime2);
    List<Auction> findByEndTimeBeforeAndStatus(Date currentTime, AuctionStatus status);
}