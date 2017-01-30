package com.shiyi.controller;

import com.shiyi.domain.LikedHistory;
import com.shiyi.domain.Restaurant;
import com.shiyi.service.LikedHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Shiyi on 1/26/2017.
 */
@RestController
@RequestMapping("/history")
public class LikeHistoryController {
    @Autowired
    private LikedHistoryService likedHistoryService;

    @RequestMapping(value="/{username}", method= RequestMethod.GET)
    public Iterable<Restaurant> listByClientName(@PathVariable(value="username") String username) {
        return likedHistoryService.listByusername(username);
    }

    @RequestMapping(value="", method= RequestMethod.POST)
    public LikedHistory create(@RequestBody LikedHistory likedHistory) {
        return likedHistoryService.create(likedHistory);
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") Long id) {
        likedHistoryService.delete(id);
    }

    @RequestMapping(value="", method= RequestMethod.DELETE)
    public String delete(@RequestBody LikedHistory likedHistory) {
        return likedHistoryService.delete(likedHistory);
    }
}
