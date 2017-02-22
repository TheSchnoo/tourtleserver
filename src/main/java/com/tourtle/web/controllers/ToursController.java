package com.tourtle.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.json.JSONArray;
import org.json.JSONObject;
import com.tourtle.web.services.ToursService;

@RestController
@RequestMapping(value = "/tours")
public class ToursController {

    @Autowired
    ToursService toursService;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Object> getAllTours() {
        System.out.println("Base tours endpoint hit");
        JSONArray toursArray = new JSONArray();

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

        toursArray.put(tourObject1);
        toursArray.put(tourObject2);

        return new ResponseEntity(toursArray.toString(), HttpStatus.OK);
    }
}
