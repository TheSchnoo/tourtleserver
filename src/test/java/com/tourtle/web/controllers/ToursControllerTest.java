package com.tourtle.web.controllers;

import com.tourtle.web.domain.Tour;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToursControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String BASE_URL = "https://tourtle-app.herokuapp.com";

    private Stack<String> tourIdsToDelete;

    @Before
    public void setUp() throws Exception {
        tourIdsToDelete = new Stack<>();
    }

    @Test
    public void toursReturnObjectContainsAllFields() throws Exception {
        String body = restTemplate.getForObject(BASE_URL + "/tours", String.class);
        assertThat(body).contains("tourId");
        assertThat(body).contains("name");
        assertThat(body).contains("beacons");
    }

    @Test
    public void getToursById() throws Exception {

    }

    @Test
    public void createTourByIdSuccessfullyCreatesATour() throws Exception {
        Tour tour = new Tour();
        tour.setName("Testing Tour");
        tour.setTourId("99");
        tour.setPois(Collections.emptyList());
        putTour(tour);
        String body = restTemplate.getForObject(BASE_URL + "/tours/99", String.class);
        assertThat(body).contains("tourId");
        assertThat(body).contains("name");
        assertThat(body).contains("beacons");
    }

    private void putTour(Tour tour) {
        restTemplate.put(BASE_URL + "/tours/" + tour.getTourId(), tour, String.class);
        tourIdsToDelete.push(tour.getTourId());
    }

    @After
    public void tearDown() throws Exception {
        while (!tourIdsToDelete.isEmpty()) {
            String tourId = tourIdsToDelete.pop();
            restTemplate.delete(BASE_URL + "/tours/" + tourId);
        }
    }

}