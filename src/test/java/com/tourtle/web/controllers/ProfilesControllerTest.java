package com.tourtle.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfilesControllerTest {

	@Autowired
	private TestRestTemplate restTemplate;

	private String BASE_URL = "https://tourtle-app.herokuapp.com";

	@Test
	public void profilesEndpointReturnsProfileForValidQuery() {
		String body = restTemplate.getForObject(BASE_URL + "/tours/", String.class);
		assertThat(body).contains("pois");
		assertThat(body).contains("beacons");
	}

	@Test
	public void profilesEndpointEmptyWithInvalidQuery() {
		String body = restTemplate.getForObject(BASE_URL + "/profiles/invalid", String.class);
		assertThat(body).contains("Profile does not exist");
	}

}
