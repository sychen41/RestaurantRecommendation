package com.shiyi.repository;

import com.shiyi.domain.Restaurant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shiyi on 1/25/2017.
 */
@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, String> {
    Restaurant findByBusinessId(String businessId);
    Iterable<Restaurant> findByCategoriesContaining(String category);
}
