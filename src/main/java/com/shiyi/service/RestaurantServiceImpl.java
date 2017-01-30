package com.shiyi.service;

import com.shiyi.domain.LikedHistory;
import com.shiyi.domain.Restaurant;
import com.shiyi.repository.LikedHistoryRepository;
import com.shiyi.repository.RestaurantRepository;
import com.shiyi.yelp.YelpAPI;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Shiyi on 1/25/2017.
 */
@Service
public class RestaurantServiceImpl implements RestaurantService {

    private static final int MAX_RECOMMENDED_RESTAURANTS = 10;

    RestaurantRepository restaurantRepository;
    LikedHistoryRepository likedHistoryRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository, LikedHistoryRepository likedHistoryRepository) {
        this.restaurantRepository = restaurantRepository;
        this.likedHistoryRepository = likedHistoryRepository;
    }

    @Override
    public Iterable<Restaurant> init(String username, double lat, double lon) {
        YelpAPI api = new YelpAPI();
        //double lat = 40.7393257;
        //double lon = -74.009508;
        JSONObject response = new JSONObject(
                api.searchForBusinessesByLocation(lat, lon));
        JSONArray array = (JSONArray) response.get("businesses");
        //List<JSONObject> list = new ArrayList<JSONObject>();
        List<Restaurant> list = new ArrayList<>();
        //Set<String> visited = getVisitedRestaurants(userId);
        List<LikedHistory> likedHistories = likedHistoryRepository.findByusername(username);
        Set<String> favorites = new HashSet<>();
        for(LikedHistory likedHistory: likedHistories)
            favorites.add(likedHistory.getBusinessId());

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            //System.out.println("object " + i + " " + object.toString());
            Restaurant restaurant = new Restaurant(object);
            if(favorites.contains(restaurant.getBusinessId()))
                restaurant.setFavoriteStatus("true");
            list.add(restaurant);
            restaurantRepository.save(restaurant);
        }
        return list;
    }

    @Override
    public Iterable<Restaurant> list() {
        return restaurantRepository.findAll();
    }
    @Override
    public Restaurant read(String business_id) {
        return restaurantRepository.findOne(business_id);
    }

    @Override
    public Iterable<Restaurant> recommendation(String username) {
        List<Restaurant> favorites = getFavoriteRestaurants(username);
        Set<String> allCategories = new HashSet<>();
        Set<String> idsOffavorites = new HashSet<>();
        for(Restaurant rest: favorites) {
            idsOffavorites.add(rest.getBusinessId());
            for(String category: rest.getCategories().split(","))
                allCategories.add(category);
        }
        Set<Restaurant> allRecoms = new HashSet<>();
        for (String category: allCategories) {
            allRecoms.addAll(getBusinessIdByCategory(category));
            //System.out.println(restaurantsOfCategory);
            //System.out.println("-------------");
        }
        Set<Restaurant> allRecomsMinusFavorites = new HashSet<>();
        int count = 0;
        for(Restaurant rest: allRecoms) {
            if(!idsOffavorites.contains(rest.getBusinessId())) {
                rest.setFavoriteStatus("false");
                allRecomsMinusFavorites.add(rest);
                count++;
                if(count==MAX_RECOMMENDED_RESTAURANTS)
                    break;
            }
        }
        return allRecomsMinusFavorites;
    }

    private Set<Restaurant> getBusinessIdByCategory(String category) {
        Set<Restaurant> set = new HashSet<>();
        for(Restaurant rest: restaurantRepository.findByCategoriesContaining(category))
            set.add(rest);
        return set;
    }
    private List<Restaurant> getFavoriteRestaurants(String username) {
        List<LikedHistory> historyList = likedHistoryRepository.findByusername(username);
        List<Restaurant> restList = new ArrayList<>();
        for(LikedHistory item : historyList) {
            Restaurant rest = restaurantRepository.findByBusinessId(item.getBusinessId());
            rest.setFavoriteStatus("true");
            restList.add(rest);
        }
        return restList;
    }
}
