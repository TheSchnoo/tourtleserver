package com.tourtle.web.controllers;

import com.tourtle.web.domain.Tour;
import com.tourtle.web.services.TourService;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToursControllerTest {

    private final static String BASE_URL = "/tours";

    private MockMvc mockMvc;

    @Mock
    private TourService mockTourService;

    @InjectMocks
    private ToursController toursController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(toursController).build();
    }

    private List<Tour> createTours() {
        List<Tour> tours = new ArrayList<>();
        Tour t0 = new Tour();
        Tour t1 = new Tour();

        tours.add(t0);
        tours.add(t1);

        return tours;
    }

    @Test
    public void returnAllTours() throws Exception {
        List<Tour> tours = createTours();
        when(mockTourService.getAllTours()).thenReturn(tours);
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("Tours"));
    }

    @Test
    public void getToursByIdReturnsTourForValidId() throws Exception {
//        String body = restTemplate.getForObject(BASE_URL + "/tours/2", String.class);
//        assertThat(body).contains("tourId");
//        assertThat(body).contains("name");
//        assertThat(body).contains("beacons");
//        JSONArray beacons = getBeaconArray(body);
//        assertThat(beacons.length()).isGreaterThan(0);
    }

    @Test
    public void noTourReturnedWhenTourIdNotValid() throws Exception {
//        String body = restTemplate.getForObject(BASE_URL + "/tours/-1", String.class);
//        assertThat(body).contains("Resource not found");
    }

    @Test
    public void createTourByIdSuccessfullyCreatesATour() throws Exception {
//        Tour tour = new Tour();
//        tour.setName("Testing Tour");
//        tour.setTourId("99");
//        tour.setPois(Collections.emptyList());
//        putTour(tour);
//        String body = restTemplate.getForObject(BASE_URL + "/tours/99", String.class);
//        assertThat(body).contains("tourId");
//        assertThat(body).contains("99");
//        assertThat(body).contains("name");
//        assertThat(body).contains("Testing Tour");
//        assertThat(body).contains("beacons");
//        JSONArray beacons = getBeaconArray(body);
//        assertThat(beacons.length()).isEqualTo(0);

    }

    private void putTour(Tour tour) {
//        restTemplate.put(BASE_URL + "/tours/" + tour.getTourId(), tour, String.class);
//        tourIdsToDelete.push(tour.getTourId());
    }

    private JSONArray getBeaconArray(String body) {
        JSONArray beacons = null;
        try {
            JSONObject beaconsObject = new JSONObject(body);
            beacons = beaconsObject.getJSONArray("beacons");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return beacons;
    }

    @After
    public void tearDown() throws Exception {
    }

}