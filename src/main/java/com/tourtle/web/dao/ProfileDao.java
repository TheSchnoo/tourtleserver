package com.tourtle.web.dao;

import com.tourtle.web.domain.Profile;

/**
 * DAO for Profile
 */
public interface ProfileDao {

    Profile getProfileByUsername(String poiId);

    boolean checkProfileExists(String profileId);
}
