package com.tourtle.web.dao;

/**
 * DAO for Profile
 */
public interface MobileProfileDao {

    boolean checkMobileProfileExists(String profileId);

    void createMobileProfile(String username, String password);

    int deleteMobileProfile(String username, String password);

}
