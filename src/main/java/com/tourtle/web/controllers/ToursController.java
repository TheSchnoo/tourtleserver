package com.tourtle.web.controllers;

import com.tourtle.web.domain.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.tourtle.web.services.TourService;

import java.util.List;

@RestController
@RequestMapping(value = "/tours")
public class ToursController {

    @Autowired
    TourService tourService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Object> getTours(@RequestParam(value = "tourName", required=false) String tourName) {
        System.out.println("Base tours endpoint hit");
        List response;
        try {
            if (tourName != null) {
                Tour tour = tourService.getTourByName(tourName);
                return new ResponseEntity<>(tour, HttpStatus.OK);
            } else {
                List<Tour> tours = tourService.getAllTours();
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
            Tour tour = tourService.getTourById(tourId);
            return new ResponseEntity<>(tour, HttpStatus.OK);
        } catch (DataRetrievalFailureException e) {
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.PUT)
    ResponseEntity<Object> createTour(@PathVariable("tourId") String tourId, @RequestBody String body) {
        System.out.println("Base tours id endpoint hit");
        try {
            int rowsAffected = tourService.createTour(tourId, body);
            return new ResponseEntity<>(rowsAffected, HttpStatus.CREATED);
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>("A tour already exists with that tourid", HttpStatus.IM_USED);
        }
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.POST)
    ResponseEntity<Object> postTour(@PathVariable("tourId") String tourId, @RequestBody String body) {
        System.out.println("POST tours endpoint hit");
        int rowsAffected = tourService.postTour(tourId, body);
        if (rowsAffected == 0) {
            return new ResponseEntity<>("Rows Affected: " + rowsAffected, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Rows Affected: " + rowsAffected, HttpStatus.ACCEPTED);
    }

    @RequestMapping(value="/{tourId}", method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteTour(@PathVariable("tourId") String tourId,
                                      @RequestBody(required = false) String body) {
        System.out.println("DELETE tours endpoint hit");
        int rowsAffected = 0;
        if (body != null) {
            rowsAffected = tourService.deleteFromTours(tourId, body);
        } else {
            rowsAffected = tourService.deleteTour(tourId);
        }

        if (rowsAffected == 0) {
            return new ResponseEntity<>("Rows Affected: " + rowsAffected, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>("Rows Affected: " + rowsAffected, HttpStatus.OK);
    }
}
