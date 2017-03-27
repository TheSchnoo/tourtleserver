package com.tourtle.web.services;

import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.dao.MobileProfileDao;
import com.tourtle.web.dao.TourDao;
import com.tourtle.web.domain.MobileProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Business logic for profiles
 */
@Service
public class MobileProfileService {

    @Autowired MobileProfileDao mobileProfileDao;
    @Autowired TourDao tourDao;
    @Autowired PoiDao poiDao;

    /**
     * Get a mobile profile given a username.  This method should only be called if a mobile profile exists, using
     * the checkMobileProfileExists method.
     * @param username username of the profile to fetch
     * @return Profile object with completed tours and visited POI.
     */
    public MobileProfile getMobileProfileByUsername(String username) {
        if (mobileProfileDao.checkMobileProfileExists(username)) {

            MobileProfile p = new MobileProfile();
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
        return mobileProfileDao.checkMobileProfileExists(username);
    }

    public MobileProfile createMobileProfile(String username, String password) {
        mobileProfileDao.createMobileProfile(username, password);
        return getMobileProfileByUsername(username);
    }

    public int deleteMobileProfile(String username, String password) {
        return mobileProfileDao.deleteMobileProfile(username, password);
    }

    /**
     * Marks all the uuids as being visited by the specified mobile profile.  If any of the uuids happen
     * to correspond to a POI that result in a tour being completed, then the tour will also be marked
     * as completed.
     * @param profileName the profile name to update with the new poi
     * @param uuids the poi to add
     * @return MobileProfile  The user's updated mobile profile.
     */
    public MobileProfile updateVisitedPOIs(String profileName, String[] uuids) {

        for (String uuid : uuids) {
            poiDao.addPoiToMobileUser(profileName, uuid);

            List<Integer> tourIds = tourDao.getAllTourIdsUsingPOIId(uuid);

            for (int tourId : tourIds) {
                List<String> userVisitedPoiOnTour = poiDao.getCompletedPOIIdForTourByMobileUser(profileName, tourId);
                List<String> allPoiIdForTour = poiDao.getPOIIdForTour(tourId);

                if (userVisitedPoiOnTour.equals(allPoiIdForTour)) {
                    tourDao.addCompletedTourToMobileUser(tourId, profileName);
                }
            }
        }
        return getMobileProfileByUsername(profileName);
    }

}
