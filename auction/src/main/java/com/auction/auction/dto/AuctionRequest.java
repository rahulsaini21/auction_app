package com.auction.auction.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.Date;

@Data
public class AuctionRequest {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 500)
    private String description;

    @NotNull
    @Positive
    private Double startingPrice;

    @NotNull
    @Positive
    private Double minimumIncrement;

    @NotNull
    @Future
    private Date startTime;

    @NotNull
    @Future
    private Date endTime;

    @NotNull
    @Min(1)
    private Integer biddingIntervalMinutes;
}