package com.tourtle.web.services;

import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.dao.TourDao;
import com.tourtle.web.dao.WebProfileDao;
import com.tourtle.web.domain.WebProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebProfileServiceTest {

    private static final String USERNAME = "Godzilla";
    private static final String PW = "babygodzilla";
    private WebProfile godzillaProfile;
    private List<String> tours;
    private List<String> pois;

    @Mock
    private WebProfileDao webProfileDao;

    @Mock
    private TourDao tourDao;

    @Mock
    private PoiDao poiDao;

    @InjectMocks
    private WebProfileService webProfileService;


    @Before
    public void setUp() throws Exception {
        tours = new ArrayList<>();
        tours.add("t0");
        tours.add("t1");

        pois = new ArrayList<>();
        pois.add("p0");
        pois.add("p1");

        godzillaProfile = new WebProfile();
        godzillaProfile.setToursOwned(tours);
        godzillaProfile.setPoiOwned(pois);
        godzillaProfile.setUsername(USERNAME);

        when(tourDao.getOwnedToursByWebUser(USERNAME)).thenReturn(tours);
        when(poiDao.getOwnedPOIByWebUser(USERNAME)).thenReturn(pois);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getProfileByUserName() throws Exception {
        WebProfile poi0 = webProfileService.getProfileByUserName(USERNAME);

        assertThat(poi0).isEqualToComparingFieldByField(godzillaProfile);

    }

    @Test
    public void checkWebProfileExists() throws Exception {
        when(webProfileDao.checkWebProfileExists(USERNAME)).thenReturn(true);
        assertThat(webProfileService.checkWebProfileExists(USERNAME)).isTrue();

    }

    @Test
    public void createWebProfile() throws Exception {
        assertThat(webProfileService.createWebProfile(USERNAME, PW)).isEqualToComparingFieldByField(godzillaProfile);
    }

}