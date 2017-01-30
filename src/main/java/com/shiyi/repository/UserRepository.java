package com.shiyi.repository;

import com.shiyi.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Shiyi on 1/26/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
