package com.tourtle.web.services;

import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.dao.TourDao;
import com.tourtle.web.dao.WebProfileDao;
import com.tourtle.web.domain.WebProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Business logic associated with managing web profiles
 */
@Service
public class WebProfileService {

    @Autowired WebProfileDao webProfileDao;
    @Autowired TourDao tourDao;
    @Autowired PoiDao poiDao;

    /**
     * Retrieve a profile.  This method should only be called if the profile has been confirmed to exist using
     * the checkWebProfileExists method.
     * @param user username
     * @return WebProfile with owned tours and pois.
     */
    public WebProfile getProfileByUserName(String user) {
            WebProfile wp = new WebProfile();
            wp.setUsername(user);

            List<String> toursOwned = tourDao.getOwnedToursByWebUser(user);
            List<String> poiOwned = poiDao.getOwnedPOIByWebUser(user);

            wp.setToursOwned(toursOwned);
            wp.setPoiOwned(poiOwned);

            return wp;
    }

    /**
     * Check if a webprofile exists, given a username
     * @param username username the web profile to check
     * @return true if exists
     */
    public boolean checkWebProfileExists(String username) {
        return webProfileDao.checkWebProfileExists(username);
    }

    public WebProfile createWebProfile(String username, String password) {
        webProfileDao.createWebProfile(username, password);

        return getProfileByUserName(username);
    }
}
