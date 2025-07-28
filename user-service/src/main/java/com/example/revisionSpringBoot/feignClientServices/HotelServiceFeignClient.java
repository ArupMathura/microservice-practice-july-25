package com.example.revisionSpringBoot.feignClientServices;

import com.example.revisionSpringBoot.entity.Hotel;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "HOTEL-SERVICE")
public interface HotelServiceFeignClient {

    @GetMapping("/api/hotels/{hotelId}")
    Hotel getHotelById(@PathVariable("hotelId") String hotelId);
}
