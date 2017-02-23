package com.tourtle.web.services;

import com.tourtle.web.Domain.POI;
import com.tourtle.web.Domain.Tour;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
class TourtleConversionService {

    JSONArray convertToursListToJsonArray(List tours) {

        for (Object tour : tours) {
            String tourname = (String) ((Map) tour).get("tourname");
            ((Map) tour).remove("tourname");

            Tour tourObj = new Tour(tourname, new LinkedList<>());
            POI poi = (POI) tour;
            tourObj.addPoi(poi);
            }


        // Creating a map with key tourname, values List<JSONObject> where the list is a list of POIs
        Map<String, List<JSONObject>> toursMap = convertListResponseToMap(tours);

        // Convert to JSONArray of tour objects
        return convertToursMapToJsonArray(toursMap);
    }

    private JSONArray convertToursMapToJsonArray(Map<String, List<JSONObject>> toursMap) {
        JSONArray retArray = new JSONArray();

        for (Map.Entry<String, List<JSONObject>> entry : toursMap.entrySet()) {
            JSONObject tourObject = new JSONObject();
            tourObject.put("name", entry.getKey());
            JSONArray toursArray = new JSONArray(entry.getValue());
            tourObject.put("beacons", toursArray);
            retArray.put(tourObject);
        }
        return retArray;
    }

    private Map<String, List<JSONObject>> convertListResponseToMap(List tours) {
        Map<String, List<JSONObject>> toursMap = new HashMap<>();

        for (Object tour : tours) {
            String tourname = (String) ((Map) tour).get("tourname");
            ((Map) tour).remove("tourname");

            if (toursMap.containsKey(tourname)) {
                (toursMap.get(tourname)).add(new JSONObject((Map) tour));
            } else {
                List poiList = new LinkedList<JSONObject>();
                poiList.add(new JSONObject((Map) tour));
                toursMap.put(tourname, poiList);
            }
        }
        return toursMap;
    }
}
