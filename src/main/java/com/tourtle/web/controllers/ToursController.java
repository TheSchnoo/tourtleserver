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

        JSONObject tourObject = new JSONObject();

        // Create tour JSONObject
        tourObject.put("name", "Adult");
        JSONArray beaconsArray = new JSONArray();
        beaconsArray.put(toursService.createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B57FE6D",
                49.270622, -123.13474100000002));
        beaconsArray.put(toursService.createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B5FIONA",
                49.270622, -123.13474100000002));
        tourObject.put("beacons", beaconsArray);

        toursArray.put(tourObject);

        return new ResponseEntity(toursArray.toString(), HttpStatus.OK);
    }
}
