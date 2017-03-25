package com.tourtle.web.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface WebProfileDao {
    boolean checkWebProfileExists(String username);

    void createWebProfile(String username, String password);
}
