package com.tourtle.web.controllers;

import com.tourtle.web.domain.Profile;

import com.tourtle.web.domain.WebProfile;
import com.tourtle.web.services.LoginService;
import com.tourtle.web.services.ProfileService;
import com.tourtle.web.services.WebProfileService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Handles login for the Tourtle mobile app, and the tourtle web app.
 */

@RestController
@RequestMapping(value = "/login")
public class LoginController {

    @Autowired LoginService loginService;
    @Autowired ProfileService profileService;
    @Autowired WebProfileService webProfileService;


    //TODO Handle usernames that are too long/funny characters


    @RequestMapping(value="/mobile", method = RequestMethod.PUT)
    public ResponseEntity<Profile> loginMobile(@RequestBody LoginBodyInput input) {
        boolean authenticated = loginService.loginMobile(input.user, input.password);
        if (authenticated) {
            Profile p = profileService.getProfileByUsername(input.user);
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value="/mobile", method = RequestMethod.POST)
    public ResponseEntity<Profile> makeMobileAccount(@RequestBody LoginBodyInput input) {
        boolean exists = profileService.checkMobileProfileExists(input.user);
        if (!exists) {
                Profile p = profileService.createMobileProfile(input.user, input.password);
                return new ResponseEntity(p, HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Username is already taken", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value="/web", method = RequestMethod.PUT)
    public ResponseEntity<Profile> loginWeb(@RequestBody LoginBodyInput input) {
        boolean authenticated = loginService.loginWeb(input.user, input.password);
        if (authenticated) {
            WebProfile wp = webProfileService.getProfileByUserName(input.user);
            return new ResponseEntity(wp, HttpStatus.OK);
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value="/web", method = RequestMethod.POST)
    public ResponseEntity<Profile> makeWebAccount(@RequestBody LoginBodyInput input) {
        boolean exists = webProfileService.checkWebProfileExists(input.user);
        if (!exists) {
            WebProfile wp = webProfileService.createWebProfile(input.user, input.password);
            return new ResponseEntity(wp, HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Username is already taken", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value="/mobile", method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteMobileProfile(@RequestBody LoginBodyInput input) {
        int rowsAffected = 0;
        rowsAffected = profileService.deleteMobileProfile(input.user, input.password);
        if (rowsAffected == 0) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(rowsAffected, HttpStatus.OK);
        }
    }

    @Data
    private static class LoginBodyInput {        // static class required to work properly for jackson
        private String user;
        private String password;
    }
}


