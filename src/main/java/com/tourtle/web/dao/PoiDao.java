package com.tourtle.web.dao;


import com.tourtle.web.domain.POI;
import org.springframework.stereotype.Repository;

/**
 * DAO for POI
 */
public interface PoiDao {

    boolean checkPoiExists(String poiId);

    POI getPoiByID(String poiId);

    int createPoi(String poiId, String body);

    int postPoi(String poiId, String body);

    int deletePoi(String poiId);
}
