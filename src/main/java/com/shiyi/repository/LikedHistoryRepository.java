package com.shiyi.repository;

import com.shiyi.domain.LikedHistory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Shiyi on 1/26/2017.
 */
@Repository
public interface LikedHistoryRepository extends CrudRepository<LikedHistory, Long> {
    List<LikedHistory> findByusername(String username);
}
