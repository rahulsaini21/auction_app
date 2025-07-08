package com.auction.bidding.service;

import com.auction.bidding.exception.RateLimitException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RateLimitService {

    private static final String RATE_LIMIT_PREFIX = "rate_limit:";

    @Value("${bidding.rate-limit.interval:60}")
    private int rateLimitInterval;

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public RateLimitService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void checkRateLimit(String auctionId, String userId) {
        String key = RATE_LIMIT_PREFIX + auctionId + ":" + userId;
        Long count = redisTemplate.opsForValue().increment(key);

        if (count == null) {
            return;
        }

        if (count == 1) {
            redisTemplate.expire(key, rateLimitInterval, TimeUnit.SECONDS);
        } else if (count > 1) {
            throw new RateLimitException("You can only place one bid per " + rateLimitInterval + " seconds");
        }
    }
}