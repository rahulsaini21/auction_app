package com.auction.auction.controller;

import com.auction.auction.dto.AuctionRequest;
import com.auction.auction.dto.AuctionResponse;
import com.auction.auction.model.User;
import com.auction.auction.security.AuthServiceClient;
import com.auction.auction.service.AuctionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auctions")
@RequiredArgsConstructor
public class AuctionController {

    private static final Logger logger = LoggerFactory.getLogger(AuctionController.class);
    private final AuctionService auctionService;
    private final AuthServiceClient authServiceClient;

    @PostMapping
    public ResponseEntity<AuctionResponse> createAuction(
            @RequestBody @Valid AuctionRequest auctionRequest,
            @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        logger.info("token: {}",token);
        User user = authServiceClient.getUserFromToken(token); // implement this
        logger.info("Creating auction for user: {}", user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(auctionService.createAuction(auctionRequest, user.getId(), token));
    }

    @GetMapping
    public ResponseEntity<List<AuctionResponse>> getAllAuctions() {
        logger.debug("Fetching all auctions");
        return ResponseEntity.ok(auctionService.getAllAuctions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuctionResponse> getAuctionById(@PathVariable String id) {
        logger.debug("Fetching auction by ID: {}", id);
        return ResponseEntity.ok(auctionService.getAuctionById(id));
    }
}