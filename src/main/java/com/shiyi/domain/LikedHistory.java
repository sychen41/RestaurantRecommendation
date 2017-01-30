package com.shiyi.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by Shiyi on 1/25/2017.
 */
@Entity
public class LikedHistory {
    @Id
    @GeneratedValue
    private Long usernameBusinessId;
    private String username;
    private String businessId;

    public LikedHistory() {}

    public LikedHistory(String username, String businessId) {
        this.username = username;
        this.businessId = businessId;
    }

    public Long getUsernameBusinessId() {
        return usernameBusinessId;
    }

    public void setUsernameBusinessId(Long usernameBusinessId) {
        this.usernameBusinessId = usernameBusinessId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }
}
