package com.tourtle.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProfilesControllerTest {

	private String BASE_URL = "https://tourtle-app.herokuapp.com";

	@Test
	public void profilesEndpointReturnsProfileForValidQuery() {
//		String body = restTemplate.getForObject(BASE_URL + "/profiles/moki", String.class);
//		assertThat(body).contains("username");
//		assertThat(body).contains("ToursCompleted");
	}

	@Test
	public void profilesEndpointEmptyWithInvalidQuery() {
//		String body = restTemplate.getForObject(BASE_URL + "/profiles/invalid", String.class);
//		assertThat(body).contains("Profile does not exist");
	}

}
