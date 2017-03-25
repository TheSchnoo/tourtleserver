package com.tourtle.web.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Stack;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    // TODO TESTS FOR FUNNY STRINGS?

    @Autowired
    private TestRestTemplate restTemplate;

    private String BASE_URL = "https://tourtle-app.herokuapp.com/login";

    private Stack<JSONObject> profilesToDelete;

    private HttpHeaders headers;

    @Before
    public void setUp() throws Exception {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        profilesToDelete = new Stack<>();

    }

    @Test
    public void ValidMobileLoginReturnsProfile() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("user", "moki");
        credentials.put("password", "tourtle");

        HttpEntity<Object> entity = new HttpEntity<>(credentials.toString(), headers);

        ResponseEntity<String> re = restTemplate.exchange(
                BASE_URL + "/mobile", HttpMethod.PUT, entity, String.class);

        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(re.getBody()).contains("username");
        assertThat(re.getBody()).contains("ToursCompleted");
        assertThat(re.getBody()).contains("PoiVisited");
    }

    @Test
    public void UnauthorizedMobileLoginProducesUnauthorized() throws JSONException {
        JSONObject credentials = new JSONObject();
        credentials.put("user", "noone");
        credentials.put("password", "noone");

        profilesToDelete.push(credentials);

        HttpEntity<Object> entity = new HttpEntity<>(credentials.toString(), headers);

        ResponseEntity<String> re = restTemplate.exchange(
                BASE_URL + "/mobile", HttpMethod.PUT, entity, String.class);

        assertThat(re.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(re.getBody()).contains("Unauthorized");
    }

    @Test
    public void UniqueMobileAccountCreationSucceeds() {
    }

    @Test
    public void NonUniqueMobileAccountCreationFailsWithMessage() {
    }

    @Test
    public void ValidWebLoginReturnsProfile() {
    }

    @Test
    public void UnauthorizedWebLoginProducesUnauthorized() {
    }

    @Test
    public void UniqueWebAccountCreationSucceeds() {
    }

    @Test
    public void NonUniqueWebAccountCreationFailsWithMessage() {
    }

    @After
    public void tearDown() throws Exception {
        while (!profilesToDelete.isEmpty()) {
            JSONObject profile = profilesToDelete.pop();
            HttpEntity<Object> entity = new HttpEntity<>(profile.toString(), headers);

            ResponseEntity<String> re = restTemplate.exchange(
                    BASE_URL + "/mobile", HttpMethod.DELETE, entity, String.class);
        }
    }
}
