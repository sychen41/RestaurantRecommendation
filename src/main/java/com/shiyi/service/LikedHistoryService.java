package com.shiyi.service;


import com.shiyi.domain.LikedHistory;
import com.shiyi.domain.Restaurant;

import java.util.List;

/**
 * Created by Shiyi on 1/26/2017.
 */
public interface LikedHistoryService {
    LikedHistory create(LikedHistory likedHistory);

    void delete(Long id);

    String delete(LikedHistory likedHistory);

    List<Restaurant> listByusername(String username);

}
