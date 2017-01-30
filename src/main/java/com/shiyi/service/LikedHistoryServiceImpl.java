package com.shiyi.service;

import com.shiyi.domain.LikedHistory;
import com.shiyi.domain.Restaurant;
import com.shiyi.repository.LikedHistoryRepository;
import com.shiyi.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shiyi on 1/26/2017.
 */
@Service
public class LikedHistoryServiceImpl implements LikedHistoryService {

    private LikedHistoryRepository likedHistoryRepository;
    private RestaurantRepository restaurantRepository;
    @Autowired
    public LikedHistoryServiceImpl(LikedHistoryRepository likedHistoryRepository, RestaurantRepository restaurantRepository) {
        this.likedHistoryRepository = likedHistoryRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /*
    public void setLikedRestaurants(String clientName, List<String> businessIds) {
        for(String businessId: businessIds) {
            likedHistoryRepository.save(new LikedHistory(clientName, businessId));
        }
    }
    */

    @Override
    public LikedHistory create(LikedHistory likedHistory) {
        /*
        List<LikedHistory> list = likedHistoryRepository.findByusername(likedHistory.getusername());
        boolean alreadyExist = false;
        for(LikedHistory item: list) {
            if (item.getBusinessId().equals(likedHistory.getBusinessId()))
                alreadyExist = true;
        }
        if (!alreadyExist)
            return likedHistoryRepository.save(likedHistory);
        else
            return null;
            */
        return likedHistoryRepository.save(likedHistory);
    }

    @Override
    public void delete(Long id) {
        likedHistoryRepository.delete(id);
    }

    @Override
    public String delete(LikedHistory likedHistory) {
        List<LikedHistory> list = likedHistoryRepository.findByusername(likedHistory.getUsername());
        for(LikedHistory item: list) {
            if (item.getBusinessId().equals(likedHistory.getBusinessId())) {
                likedHistoryRepository.delete(item.getUsernameBusinessId());
                break;
            }
        }
        return "deleted";
    }
    @Override
    public List<Restaurant> listByusername(String username) {
        List<LikedHistory> historyList = likedHistoryRepository.findByusername(username);
        List<Restaurant> restList = new ArrayList<>();
        for(LikedHistory item : historyList) {
            Restaurant rest = restaurantRepository.findByBusinessId(item.getBusinessId());
            rest.setFavoriteStatus("true");
            restList.add(restaurantRepository.findByBusinessId(item.getBusinessId()));
        }
        return restList;
    }

}
