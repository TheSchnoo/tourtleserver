package com.tourtle.web.controllers;

import com.tourtle.web.services.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.tourtle.web.services.ToursService;

@RestController
@RequestMapping(value = "/tours")
public class ToursController {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ToursService toursService;

    @RequestMapping(method = RequestMethod.GET)
    ResponseEntity<Object> getAllTours() {
        System.out.println("Base tours endpoint hit");

        return new ResponseEntity<>(databaseService.getAllTours().toString(), HttpStatus.OK);
    }
}
