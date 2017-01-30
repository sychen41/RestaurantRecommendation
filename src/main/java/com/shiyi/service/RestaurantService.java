package com.shiyi.service;


import com.shiyi.domain.Restaurant;

/**
 * Created by Shiyi on 1/25/2017.
 */
public interface RestaurantService {
    Iterable<Restaurant> init(String username, double lat, double lon);
    Iterable<Restaurant> list();
    Restaurant read(String business_id);
    Iterable<Restaurant> recommendation(String username);
}
