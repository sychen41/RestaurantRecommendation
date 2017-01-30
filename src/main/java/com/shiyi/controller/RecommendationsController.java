package com.shiyi.controller;

import com.shiyi.domain.Restaurant;
import com.shiyi.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Shiyi on 1/25/2017.
 */
@RestController
@RequestMapping("/recommendations")
public class RecommendationsController {

    private RestaurantService restaurantService;
    //private UserService userService;

    @Autowired
    public RecommendationsController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @RequestMapping("/{username}")
    public Iterable<Restaurant> read(@PathVariable(value="username") String username) {
        return restaurantService.recommendation(username);
    }
}
