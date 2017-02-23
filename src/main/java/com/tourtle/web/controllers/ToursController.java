package com.tourtle.web.controllers;

import com.tourtle.web.services.DatabaseService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tourtle.web.services.ToursService;

import java.util.List;

@RestController
@RequestMapping(value = "/tours")
public class ToursController {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ToursService toursService;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Object> getTours(@RequestParam(value = "tourName", required=false) String tourName) {
        System.out.println("Base tours endpoint hit");

        List response;

        if (tourName != null) {
            response = databaseService.getTourByName(tourName);
        } else {
            response = databaseService.getAllTours();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.GET)
    ResponseEntity<Object> getToursById(@PathVariable("tourId") String tourId) {
        System.out.println("Base tours id endpoint hit");

        JSONArray response = databaseService.getTourById(tourId);

        if(response.length() == 0) {
            return new ResponseEntity<>("No tour with tourId = " + tourId, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.PUT)
    ResponseEntity<Object> createTour(@PathVariable("tourId") String tourId, @RequestBody String body) {
        System.out.println("ToursId PUT endpoint hit");

        int[] batchRowsEffected = databaseService.createTour(tourId, new JSONObject(body));

        return new ResponseEntity<>(batchRowsEffected, HttpStatus.OK);
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteTour(@PathVariable("tourId") String tourId, @RequestBody String body) {
        System.out.println("ToursId DELETE endpoint hit");

        int[] batchRowsEffected = databaseService.deleteTour(tourId, new JSONObject(body));

        return new ResponseEntity<>(batchRowsEffected, HttpStatus.OK);
    }
}
