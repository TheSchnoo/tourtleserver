package com.tourtle.web.dao;

import com.tourtle.web.domain.Profile;

/**
 * DAO for Profile
 */
public interface ProfileDao {

    boolean checkMobileProfileExists(String profileId);

    void createMobileProfile(String username, String password);

}
