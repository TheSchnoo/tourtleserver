package com.tourtle.web.services;

import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.domain.POI;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

/**
 * Business logic for POIS
 */
@Service
public class PoiService {

    @Autowired
    PoiDao poiDao;

    public POI getPoiById(String poiId) {
        if (poiDao.checkPoiExists(poiId)) {
            return poiDao.getPoiByID(poiId);
        } else {
            throw new DataRetrievalFailureException("Couldn't find resource");
        }
    }

    public int createPoi(String poiId, String body) throws JSONException {
        return poiDao.createPoi(poiId, body);
    }
    
    public int postPoi(String poiId, String body) {
        return poiDao.postPoi(poiId, body);
    }

    public int deletePoi(String poiId) {
        if (poiDao.checkPoiExists(poiId)) {
            return poiDao.deletePoi(poiId);
        } else {
            throw new DataRetrievalFailureException("Couldn't find resource");
        }
    }
}
