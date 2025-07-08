package com.auction.bidding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
// @EnableFeignClients
public class BiddingApplication {
	
	// @Bean
    // public RestTemplate restTemplate() {
    //     return new RestTemplate();
    // }

	public static void main(String[] args) {
		SpringApplication.run(BiddingApplication.class, args);
	}

}
