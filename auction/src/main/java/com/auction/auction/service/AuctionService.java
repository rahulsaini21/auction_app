package com.auction.auction.service;

import com.auction.auction.dto.AuctionRequest;
import com.auction.auction.dto.AuctionResponse;
import com.auction.auction.exception.ResourceNotFoundException;
import com.auction.auction.model.Auction;
import com.auction.auction.model.User;
import com.auction.auction.repository.AuctionRepository;
import com.auction.auction.security.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private static final Logger logger = LoggerFactory.getLogger(AuctionService.class);
    private final AuctionRepository auctionRepository;
    private final AuthServiceClient authServiceClient;

    public List<AuctionResponse> getAllAuctions() {
        return auctionRepository.findAll().stream()
                .map(this::mapToAuctionResponse)
                .collect(Collectors.toList());
    }

    public AuctionResponse getAuctionById(String auctionId) {
    Auction auction = auctionRepository.findById(auctionId)
            .orElseThrow(() -> new ResourceNotFoundException("Auction", "id", auctionId));
    
    return mapToAuctionResponse(auction);
}

    public AuctionResponse createAuction(AuctionRequest auctionRequest, String userId, String token) {
        try {
            User seller = authServiceClient.getUserById(userId, token);
            if (seller == null) {
                throw new ResourceNotFoundException("User", "id", userId);
            }

            Auction auction = Auction.builder()
                    .title(auctionRequest.getTitle())
                    .description(auctionRequest.getDescription())
                    .startingPrice(auctionRequest.getStartingPrice())
                    .minimumIncrement(auctionRequest.getMinimumIncrement())
                    .startTime(auctionRequest.getStartTime())
                    .endTime(auctionRequest.getEndTime())
                    .biddingIntervalMinutes(auctionRequest.getBiddingIntervalMinutes())
                    .seller(seller.getId())
                    .status(Auction.AuctionStatus.PENDING)
                    .build();

            Auction savedAuction = auctionRepository.save(auction);
            logger.info("Created auction with ID: {}", savedAuction.getId());

            return mapToAuctionResponse(savedAuction);
        } catch (Exception e) {
            logger.error("Error creating auction", e);
            throw e;
        }
    }

    @Scheduled(fixedRate = 60000)
    public void completeExpiredAuctions() {
        try {
            Date now = new Date();
            List<Auction> expiredAuctions = auctionRepository.findByEndTimeBeforeAndStatus(
                now, Auction.AuctionStatus.ACTIVE);

            expiredAuctions.forEach(auction -> {
                auction.setStatus(Auction.AuctionStatus.COMPLETED);
                auctionRepository.save(auction);
                logger.info("Completed auction ID: {}", auction.getId());
            });
        } catch (Exception e) {
            logger.error("Error completing expired auctions", e);
        }
    }

    private AuctionResponse mapToAuctionResponse(Auction auction) {
        logger.info("auctions: {}", auction);
        return AuctionResponse.builder()
                .id(auction.getId())
                .title(auction.getTitle())
                .description(auction.getDescription())
                .startingPrice(auction.getStartingPrice())
                .minimumIncrement(auction.getMinimumIncrement())
                .startTime(auction.getStartTime())
                .endTime(auction.getEndTime())
                .biddingIntervalMinutes(auction.getBiddingIntervalMinutes())
                .sellerId(auction.getSeller())
                .status(auction.getStatus())
                .build();
    }
}