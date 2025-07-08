package com.auction.bidding.service;

import com.auction.bidding.model.Auction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuctionServiceClient {

    private final RestTemplate restTemplate;

    @Value("${auction.service.base-url}")
    private String auctionServiceBaseUrl;

    public AuctionServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Auction getAuctionById(String auctionId) {
        return restTemplate.getForObject(
                auctionServiceBaseUrl + "/" + auctionId,
                Auction.class);
    }
}