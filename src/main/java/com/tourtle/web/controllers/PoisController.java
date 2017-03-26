package com.tourtle.web.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.POI;
import com.tourtle.web.domain.domainviews.Views;
import com.tourtle.web.services.PoiService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value="/{poiId}", method = RequestMethod.PUT)
    public ResponseEntity putPoi(@PathVariable("poiId") String poiId,
                                           @RequestBody(required = false) String body) {
        try {
            int rowsAffected = poiService.createPoi(poiId, body);
            return new ResponseEntity<>("Rows Affected: " + rowsAffected, HttpStatus.OK);
        } catch (JSONException e) {
            return new ResponseEntity<>("Malformed POI data", HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value="/{poiId}", method = RequestMethod.POST)
    public ResponseEntity<Integer> postPoi(@PathVariable("poiId") String poiId,
                                       @RequestBody(required = false) String body) {
        int rowsAffected = poiService.postPoi(poiId, body);
        return new ResponseEntity("Rows Affected: " + rowsAffected, HttpStatus.OK);
    }

    @RequestMapping(value="/{poiId}", method = RequestMethod.DELETE)
    public ResponseEntity deletePoi(@PathVariable("poiId") String poiId) {
        try {
            return new ResponseEntity<>("Rows Affected: " + poiService.deletePoi(poiId), HttpStatus.OK);
        } catch (DataRetrievalFailureException e) {
            return new ResponseEntity<>("Resource not found", HttpStatus.NOT_FOUND);
        }
    }

}
