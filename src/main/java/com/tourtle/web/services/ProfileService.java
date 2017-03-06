package com.tourtle.web.services;

import com.tourtle.web.dao.ProfileDao;
import com.tourtle.web.domain.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;

/**
 * Business logic for profiles
 */
@Service
public class ProfileService {

    @Autowired
    ProfileDao profileDao;

    public Profile getProfileByUsername(String username) {
        if (profileDao.checkProfileExists(username)) {
            return profileDao.getProfileByUsername(username);
        } else {
            throw new DataRetrievalFailureException("Profile does not exist");
        }
    }
}
