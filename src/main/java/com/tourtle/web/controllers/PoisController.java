package com.tourtle.web.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.POI;
import com.tourtle.web.domain.domainviews.Views;
import com.tourtle.web.services.PoiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles all calls to the /pois endpoint
 */
@RestController
@RequestMapping(value = "/pois")
public class PoisController {

    @Autowired
    PoiService poiService;

    @JsonView(Views.ResponseData.class)
    @RequestMapping(value="/{poiId}", method = RequestMethod.GET)
    public ResponseEntity<POI> getPoiById(@PathVariable("poiId") String poiId) {
        try {
            POI poi = poiService.getPoiById(poiId);
            return new ResponseEntity(poi, HttpStatus.OK);
        } catch (DataRetrievalFailureException e) {
            return new ResponseEntity("Resource not found", HttpStatus.NOT_FOUND);
        }
    }

}
