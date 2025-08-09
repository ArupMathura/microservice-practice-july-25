package com.example.revisionSpringBoot.feignClientServices;

import com.example.revisionSpringBoot.entity.Rating;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Service
@FeignClient(name = "RATING-SERVICE")
public interface RatingServiceFeignClient {


    @PostMapping("/api/ratings")
    public ResponseEntity<Rating> create(@RequestBody Rating rating);


    @PutMapping("/api/ratings/{ratingId}")
    public Rating updateRating(@PathVariable String ratingId, Rating rating);

    @DeleteMapping("/api/ratings/{ratingId}")
    public void deleteRating(@PathVariable String ratingId);
}
