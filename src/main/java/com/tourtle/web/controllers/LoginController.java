package com.tourtle.web.controllers;

import com.tourtle.web.domain.MobileProfile;

import com.tourtle.web.domain.WebProfile;
import com.tourtle.web.services.LoginService;
import com.tourtle.web.services.MobileProfileService;
import com.tourtle.web.services.WebProfileService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired MobileProfileService mobileProfileService;
    @Autowired WebProfileService webProfileService;

    public LoginController (LoginService loginService, MobileProfileService mobileMobileProfileService,
                            WebProfileService webProfileService) {
        this.loginService = loginService;
        this.mobileProfileService = mobileMobileProfileService;
        this.webProfileService = webProfileService;
    }


    //TODO Handle usernames that are too long/funny characters


    @RequestMapping(value="/mobileuser", method = RequestMethod.PUT)
    public ResponseEntity<MobileProfile> loginMobile(@RequestBody LoginBodyInput input) {
        boolean authenticated = loginService.loginMobile(input.user, input.password);
        if (authenticated) {
            MobileProfile p = mobileProfileService.getMobileProfileByUsername(input.user);
            return new ResponseEntity(p, HttpStatus.OK);
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }


    @RequestMapping(value="/mobileuser", method = RequestMethod.POST)
    public ResponseEntity<MobileProfile> makeMobileAccount(@RequestBody LoginBodyInput input) {
        boolean exists = mobileProfileService.checkMobileProfileExists(input.user);
        if (!exists) {
                MobileProfile p = mobileProfileService.createMobileProfile(input.user, input.password);
                return new ResponseEntity(p, HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Username is already taken", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value="/webuser", method = RequestMethod.PUT)
    public ResponseEntity<MobileProfile> loginWeb(@RequestBody LoginBodyInput input) {
        boolean authenticated = loginService.loginWeb(input.user, input.password);
        if (authenticated) {
            WebProfile wp = webProfileService.getProfileByUserName(input.user);
            return new ResponseEntity(wp, HttpStatus.OK);
        } else {
            return new ResponseEntity("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @RequestMapping(value="/webuser", method = RequestMethod.POST)
    public ResponseEntity<MobileProfile> makeWebAccount(@RequestBody LoginBodyInput input) {
        boolean exists = webProfileService.checkWebProfileExists(input.user);
        if (!exists) {
            WebProfile wp = webProfileService.createWebProfile(input.user, input.password);
            return new ResponseEntity(wp, HttpStatus.CREATED);
        } else {
            return new ResponseEntity("Username is already taken", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    @RequestMapping(value="/mobileuser", method = RequestMethod.DELETE)
    ResponseEntity<Object> deleteMobileProfile(@RequestBody LoginBodyInput input) {
        int rowsAffected = 0;
        rowsAffected = mobileProfileService.deleteMobileProfile(input.user, input.password);
        if (rowsAffected == 0) {
            return new ResponseEntity<>("Invalid credentials", HttpStatus.UNAUTHORIZED);
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


