package com.tourtle.web.services;

import com.tourtle.web.dao.MobileProfileDao;
import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.dao.TourDao;
import com.tourtle.web.domain.MobileProfile;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MobileProfileServiceTest {
    private static final String USERNAME = "username";
    private static final String PW = "pass";
    private static final String UUID = "uuid";

    private MobileProfile profile;

    @Mock
    private MobileProfileDao mobileProfileDao;
    @Mock
    private TourDao tourDao;
    @Mock
    private PoiDao poiDao;

    @InjectMocks
    private MobileProfileService mobileProfileService;

    @Before
    public void setUp() throws Exception {
        profile = new MobileProfile();
        profile.setUsername(USERNAME);
        profile.setPoiVisited(Collections.emptyList());
        profile.setToursCompleted(Collections.emptyList());

        when(mobileProfileDao.checkMobileProfileExists(USERNAME)).thenReturn(true);
        when(mobileProfileDao.deleteMobileProfile(USERNAME, PW)).thenReturn(1);
        when(poiDao.getCompletedPOIByMobileUser(USERNAME)).thenReturn(Collections.emptyList());
        when(poiDao.addPoiToMobileUser(USERNAME, UUID)).thenReturn(1);
        when(poiDao.getPOIIdForTour(1)).thenReturn(Collections.emptyList());
        when(tourDao.getCompletedToursByMobileUser(USERNAME)).thenReturn(Collections.emptyList());
        when(tourDao.getAllTourIdsUsingPOIId(UUID)).thenReturn(Collections.emptyList());
        when(tourDao.addCompletedTourToMobileUser(1, USERNAME)).thenReturn(1);

    }

    @Test
    public void getMobileProfileByValidUsernameReturnsProfile() throws Exception {
        assertThat(mobileProfileService.getMobileProfileByUsername(USERNAME)).isEqualTo(profile);
    }

    @Test
    public void getMobileProfileWithInvalidUsernameThrowsException() throws Exception {
        assertThatThrownBy(() -> { mobileProfileService.getMobileProfileByUsername("Invalid"); })
                .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void checkValidMobileProfileExists() throws Exception {
        assertThat(mobileProfileService.checkMobileProfileExists(USERNAME)).isTrue();
    }

    @Test
    public void checkInvalidMobileProfileDoesNotExists() throws Exception {
        assertThat(mobileProfileService.checkMobileProfileExists("Invalid")).isFalse();
    }

    @Test
    public void createMobileProfile() throws Exception {
        assertThat(mobileProfileService.createMobileProfile(USERNAME, PW)).isEqualTo(profile);
    }

    @Test
    public void deleteMobileProfile() throws Exception {
        assertThat(mobileProfileService.deleteMobileProfile(USERNAME, PW)).isEqualTo(1);
    }

    @Test
    public void updateVisitedPOIs() throws Exception {
        String[] uuids = new String[1];
        uuids[0] = "id";
        assertThat(mobileProfileService.updateVisitedPOIs(USERNAME, new String[1])).isEqualTo(profile);
    }

}