package com.tourtle.web.services;

import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.dao.ProfileDao;
import com.tourtle.web.dao.TourDao;
import com.tourtle.web.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for profiles
 */
@Service
public class ProfileService {

    @Autowired ProfileDao profileDao;
    @Autowired TourDao tourDao;
    @Autowired PoiDao poiDao;

    /**
     * Get a mobile profile given a username.  This method should only be called if a mobile profile exists, using
     * the checkMobileProfileExists method.
     * @param username username of the profile to fetch
     * @return Profile object with completed tours and visited POI.
     */
    public Profile getProfileByUsername(String username) {
        if (profileDao.checkMobileProfileExists(username)) {

            Profile p = new Profile();
            p.setUsername(username);

            List<String> tours = tourDao.getCompletedToursByMobileUser(username);
            List<String> pois = poiDao.getCompletedPOIByMobileUser(username);

            p.setToursCompleted(tours);
            p.setPoiVisited(pois);

            return p;
        } else {
            throw new DataRetrievalFailureException("Profile does not exist");
        }
    }

    /**
     * Check if a mobile profile exists, given a username
     * @param username username to check
     * @return true if found
     */
    public boolean checkMobileProfileExists(String username) {
        return profileDao.checkMobileProfileExists(username);
    }

    public Profile createMobileProfile(String username, String password) {
        profileDao.createMobileProfile(username, password);
        return getProfileByUsername(username);
    }
}
