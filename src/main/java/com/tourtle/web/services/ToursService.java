package com.tourtle.web.services;

import com.tourtle.web.Domain.POI;
import com.tourtle.web.Domain.Tour;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ToursService {

    public JSONObject createJsonBeaconObject(String uuid, double lat, double lon,
                                             String description, String imageUrl) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", uuid);
        obj.put("lat", lat);
        obj.put("lon", lon);
        obj.put("description", description);
        obj.put("imageUrl", imageUrl);
        return obj;
    }

    public List<Tour> filterPoisToToursList(List<POI> tours) {

        List<Tour> tourList = new ArrayList<>();

        String tourname = "";

        Tour tour = null;

        int count = 0;

        for (POI poi : tours) {
            if (poi.getTourname().equals(tourname) && tour != null) {
                // Already processing POIs for this tour
                tour.addPoi(poi);
            } else {
                // New tour
                if (!tourname.equals("")) {
                    // If we've just finished a tour, store it before moving on to the next one
                    tourList.add(tour);
                }
                tourname = poi.getTourname();
                List<POI> pois = new ArrayList<>();
                pois.add(poi);
                tour = new Tour();
                tour.setName(tourname);
                tour.setPois(pois);
            }
            count++;
            if (count == tours.size()) {
                tourList.add(tour);
            }
        }
        return tourList;
    }
}
