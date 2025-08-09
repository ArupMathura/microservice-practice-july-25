package com.example.revisionSpringBoot;

import com.example.revisionSpringBoot.entity.Rating;
import com.example.revisionSpringBoot.feignClientServices.RatingServiceFeignClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@SpringBootTest
class UserServiceApplicationTests {

    @Autowired
    private RatingServiceFeignClient ratingService;

	@Test
	void contextLoads() {
	}


    @Test
    void createRating() {
        Rating rating = Rating.builder()
                .ratingId("ratingID-test")
                .userId("userId-test")
                .hotelId("hotelId-test")
                .rating("10")
                .feedback("this is created using feign client")
                .build();

        ResponseEntity<Rating> saveRatings = ratingService.create(rating);
        System.out.println("----------new rating created------------");
    }

}
