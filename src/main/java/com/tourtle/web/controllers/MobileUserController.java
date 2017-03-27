package com.tourtle.web.controllers;

import com.tourtle.web.domain.MobileProfile;
import com.tourtle.web.services.MobileProfileService;
import com.tourtle.web.services.PoiService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Responsible for handling connections to the mobileuser endpoint
 */
@RestController
@RequestMapping(value = "/mobileuser")
public class MobileUserController {

    @Autowired MobileProfileService mobileProfileService;
    @Autowired PoiService poiService;

    @RequestMapping(value="/{id}/poivisited", method = RequestMethod.POST)
    public ResponseEntity<MobileProfile> addNewPOIToVisited(@PathVariable("id") String mProfileName, @RequestBody VisitedBody input) {
        boolean profileExists = mobileProfileService.checkMobileProfileExists(mProfileName);
        boolean allPOIsExist = poiService.checkAllPOIExist(input.getUuids());
        if (profileExists && allPOIsExist) {
            MobileProfile profile = mobileProfileService.updateVisitedPOIs(mProfileName, input.getUuids());
            return new ResponseEntity(profile, HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Resource does not exist", HttpStatus.NOT_FOUND);
        }
    }

    @Data
    private static class VisitedBody {        // static class required to work properly for jackson
        private String[] uuids;
    }
}
