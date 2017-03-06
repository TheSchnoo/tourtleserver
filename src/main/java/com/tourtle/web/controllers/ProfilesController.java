package com.tourtle.web.controllers;

import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.Profile;
import com.tourtle.web.domain.domainviews.Views;
import com.tourtle.web.services.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles all calls to the /profiles endpoint
 */
@RestController
@RequestMapping(value = "/profiles")
public class ProfilesController {

    @Autowired
    ProfileService profileService;

    @JsonView(Views.ResponseData.class)
    @RequestMapping(value="/{username}", method = RequestMethod.GET)
    public ResponseEntity<Profile> getProfileByUsername(@PathVariable("username") String username) {
        try {
            Profile profile = profileService.getProfileByUsername(username);
            return new ResponseEntity(profile, HttpStatus.OK);
        } catch (DataRetrievalFailureException e) {
            return new ResponseEntity("Profile does not exist", HttpStatus.NOT_FOUND);
        }
    }
}
