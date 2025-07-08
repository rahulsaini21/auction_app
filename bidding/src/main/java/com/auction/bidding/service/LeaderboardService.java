package com.auction.bidding.service;

import com.auction.bidding.dto.LeaderboardEntry;
import com.auction.bidding.dto.UserPositionResponse;
import com.auction.bidding.dto.UserProfile;
import com.auction.bidding.model.Bid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeaderboardService {

    private static final String LEADERBOARD_PREFIX = "leaderboard:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthServiceClient authServiceClient;

    @Autowired
    public LeaderboardService(RedisTemplate<String, Object> redisTemplate, 
                            AuthServiceClient authServiceClient) {
        this.redisTemplate = redisTemplate;
        this.authServiceClient = authServiceClient;
    }

    public void updateLeaderboard(String auctionId, String userId, double amount) {
        String key = LEADERBOARD_PREFIX + auctionId;
        redisTemplate.opsForZSet().add(key, userId, amount);
    }

    public List<LeaderboardEntry> getTopBidders(String auctionId, int limit) {
        String key = LEADERBOARD_PREFIX + auctionId;
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, 0, limit - 1);

        if (tuples == null || tuples.isEmpty()) {
            return Collections.emptyList();
        }

        // return tuples.stream()
        //         .map(tuple -> {
        //             String userId = (String) tuple.getValue();
        //             double score = double.valueOf(tuple.getScore());
        //             UserProfile user = authServiceClient.getUserById(userId);
        //             return new LeaderboardEntry(
        //                     userId,
        //                     user != null ? user.getUsername() : "Unknown",
        //                     score,
        //                     redisTemplate.opsForZSet().reverseRank(key, userId).intValue() + 1
        //             );
        //         })
        //         .collect(Collectors.toList());
        return tuples.stream()
            .map(tuple -> {
                String userId = (String) tuple.getValue();
                double score = tuple.getScore(); // No need to parse
                UserProfile user = authServiceClient.getUserById(userId);
                Long rank = redisTemplate.opsForZSet().reverseRank(key, userId);
                return new LeaderboardEntry(
                    userId,
                    user != null ? user.getUsername() : "Unknown",
                    score,
                    rank != null ? rank.intValue() + 1 : -1  // handle null rank safely
                );
            })
            .collect(Collectors.toList());
    }

    public Optional<Double> getHighestBidForAuction(String auctionId) {
        String key = LEADERBOARD_PREFIX + auctionId;
        Set<ZSetOperations.TypedTuple<Object>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(key, 0, 0);

        if (tuples == null || tuples.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(tuples.iterator().next().getScore());
    }


    public UserPositionResponse getUserPosition(String auctionId, String userId) {
        String key = LEADERBOARD_PREFIX + auctionId;
        Double score = redisTemplate.opsForZSet().score(key, userId);
        Long rank = redisTemplate.opsForZSet().reverseRank(key, userId);
        Long total = redisTemplate.opsForZSet().zCard(key);

        UserProfile user = authServiceClient.getUserById(userId);

        return new UserPositionResponse(
            userId,
            user != null ? user.getUsername() : "Unknown",
            score != null ? score : 0.0,
            rank != null ? rank.intValue() + 1 : -1,
            total != null ? total.intValue() : 0
        );
    }

}