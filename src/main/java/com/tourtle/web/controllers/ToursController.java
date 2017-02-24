package com.tourtle.web.controllers;

import com.tourtle.web.domain.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tourtle.web.services.ToursService;

import java.util.List;

@RestController
@RequestMapping(value = "/tours")
public class ToursController {

    @Autowired
    ToursService toursService;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Object> getTours(@RequestParam(value = "tourName", required=false) String tourName) {
        System.out.println("Base tours endpoint hit");
        List response;
        try {
            if (tourName != null) {
                Tour tour = toursService.getTourByName(tourName);
                return new ResponseEntity<>(tour, HttpStatus.OK);
            } else {
                List<Tour> tours = toursService.getAllTours();
                return new ResponseEntity<>(tours, HttpStatus.OK);
            }
        } catch (DataRetrievalFailureException e) {
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.GET)
    ResponseEntity<Object> getToursById(@PathVariable("tourId") String tourId) {
        System.out.println("Base tours id endpoint hit");

        try {
            Tour tour = toursService.getTourById(tourId);
            return new ResponseEntity<>(tour, HttpStatus.OK);
        } catch (DataRetrievalFailureException e) {
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
        }
    }
}
