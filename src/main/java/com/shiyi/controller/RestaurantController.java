package com.shiyi.controller;

import com.shiyi.domain.Restaurant;
import com.shiyi.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by Shiyi on 1/25/2017.
 */
@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @RequestMapping(value="/init",method = RequestMethod.POST)
    public Iterable<Restaurant> init(@RequestBody Map<String, String> json) {
        //System.out.println(json.get("username"));
        return restaurantService.init(json.get("username"), Double.parseDouble(json.get("lat")), Double.parseDouble(json.get("lon")));
    }

    @RequestMapping("/")
    public Iterable<Restaurant> list() {
        return restaurantService.list();
    }

    @RequestMapping("/{id}")
    public Restaurant read(@PathVariable(value="id") String business_id) {
        return restaurantService.read(business_id);
    }
}
