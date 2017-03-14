package com.tourtle.web.controllers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ToursControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String BASE_URL = "https://tourtle-app.herokuapp.com";

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

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
    public void createTour() throws Exception {

    }

    @Test
    public void postTour() throws Exception {

    }

    @Test
    public void deleteTour() throws Exception {

    }

}