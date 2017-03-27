package com.tourtle.web.dao;


import com.tourtle.web.domain.POI;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * DAO for POI
 */
public interface PoiDao {

    boolean checkPoiExists(String poiId);

    POI getPoiByID(String poiId);

    int createPoi(String poiId, String body);

    int postPoi(String poiId, String body);

    int deletePoi(String poiId);

    int addPoiToMobileUser(String profileName, String uuid);

    List<String> getCompletedPOIByMobileUser(String username);

    List<String> getCompletedPOIIdForTourByMobileUser(String username, int tourId);

    List<String> getPOIIdForTour(int tourId);

    List<String> getOwnedPOIByWebUser(String username);
}
