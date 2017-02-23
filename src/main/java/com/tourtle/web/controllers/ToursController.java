package com.tourtle.web.controllers;

import com.tourtle.TourtleServerApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;
import com.tourtle.web.services.ToursService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/tours")
public class ToursController {

    @Autowired
    ToursService toursService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Object> getAllTours() {
        System.out.println("Base tours endpoint hit");

        String sql = "SELECT tour.tourname, poi.* " +
                "FROM tour, poi, tours_pois " +
                "WHERE tour.tourid=tours_pois.tourid " +
                "AND poi.beaconid=tours_pois.beaconid";
        List tours = jdbcTemplate.queryForList(sql);

        Map<String, List<JSONObject>> toursMap = convertToJsonArray(tours);

        JSONArray retArray = new JSONArray();

        for (Map.Entry<String, List<JSONObject>> entry : toursMap.entrySet()) {
            JSONObject tourObject = new JSONObject();
            tourObject.put("name", entry.getKey());
            JSONArray toursArray = new JSONArray(entry.getValue());
            tourObject.put("beacons", toursArray);
            retArray.put(tourObject);
        }

        JSONObject tourObject1 = new JSONObject();

        // Create tour JSONObject
        tourObject1.put("name", "Afternoon Delight");
        JSONArray afternoonBeaconArray = new JSONArray();
        afternoonBeaconArray.put(toursService.createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B57FE6D",
                49.270622, -123.13474100000002,
                "Oyama - Delicious Meats", "imageLink"));
        afternoonBeaconArray.put(toursService.createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B5FIONA",
                49.270622, -123.13474100000002,
                "Terra Breads - Artisan Breads", "imageLink"));
        tourObject1.put("beacons", afternoonBeaconArray);


        JSONObject tourObject2 = new JSONObject();
        tourObject2.put("name", "Island Breakfast");
        JSONArray breakfastBeaconArray = new JSONArray();
        breakfastBeaconArray.put(toursService.createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B57FE6A",
                49.270622, -123.13474100000002,
                "JJ Bean - Fresh Coffee", "imageLink"));
        afternoonBeaconArray.put(toursService.createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B5FIONA",
                49.270622, -123.13474100000002,
                "Terra Breads - Artisan Breads", "imageLink"));
        tourObject2.put("beacons", breakfastBeaconArray);

        return new ResponseEntity(retArray.toString(), HttpStatus.OK);
    }

    private Map<String, List<JSONObject>> convertToJsonArray(List tours) {
        // Creating a map with key tourname, values List<JSONObject> where the list is a list of POIs
        Map toursMap = new HashMap<String, List<JSONObject>>();

        for (int i=0; i < tours.size(); i++) {

            String tourname = (String) ((Map) tours.get(i)).get("tourname");
            ((Map) tours.get(i)).remove("tourname");
            if (toursMap.containsKey(tourname)) {
                ((LinkedList<JSONObject>) toursMap.get(tourname)).add(new JSONObject((Map) tours.get(i)));
            } else {
                List poiList = new LinkedList<JSONObject>();
                poiList.add(new JSONObject((Map) tours.get(i)));
                toursMap.put(tourname, poiList);
            }
        }
        return toursMap;
    }
}
