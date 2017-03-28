package com.tourtle.web.controllers;

import com.tourtle.web.domain.MobileProfile;
import com.tourtle.web.domain.WebProfile;
import com.tourtle.web.services.LoginService;
import com.tourtle.web.services.MobileProfileService;
import com.tourtle.web.services.WebProfileService;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginControllerTest {

    private static final String BASE_URL = "/login";
    private static final String USERNAME = "Batman";
    private static final String PW = "Alfred";

    private MockMvc mockMvc;

    private JSONObject batmanCredentials;
    private JSONObject invalidCredentials;
    private JSONObject dogCredentials;

    @Mock
    private LoginService mockLoginService;
    @Mock
    private MobileProfileService mockMobileProfileService;
    @Mock
    private WebProfileService mockWebProfileService;

    @InjectMocks
    private LoginController loginController;

    @Before
    public void setUp() throws Exception {

        batmanCredentials = new JSONObject();
        batmanCredentials.put("user", USERNAME);
        batmanCredentials.put("password", PW);

        invalidCredentials = new JSONObject();
        invalidCredentials.put("user", "noone");
        invalidCredentials.put("password", "nothing");

        dogCredentials = new JSONObject();
        dogCredentials.put("user", "Dog");
        dogCredentials.put("password", "getdogbones");

        MobileProfile batmanMobileProfile = new MobileProfile();
        batmanMobileProfile.setUsername(USERNAME);
        batmanMobileProfile.setPoiVisited(Collections.emptyList());
        batmanMobileProfile.setToursCompleted(Collections.emptyList());

        WebProfile batmanWebProfile = new WebProfile();
        batmanWebProfile.setUsername(USERNAME);
        batmanWebProfile.setPoiOwned(Collections.emptyList());
        batmanWebProfile.setToursOwned(Collections.emptyList());

        when(mockLoginService.loginMobile(USERNAME, PW)).thenReturn(true);
        when(mockLoginService.loginWeb(USERNAME, PW)).thenReturn(true);

        when(mockMobileProfileService.checkMobileProfileExists(USERNAME)).thenReturn(true);
        when(mockMobileProfileService.getMobileProfileByUsername(USERNAME)).thenReturn(batmanMobileProfile);

        when(mockWebProfileService.checkWebProfileExists(USERNAME)).thenReturn(true);
        when(mockWebProfileService.getProfileByUserName(USERNAME)).thenReturn(batmanWebProfile);

        mockMvc = MockMvcBuilders.standaloneSetup(loginController)
                .build();
    }

    @Test
    public void ValidMobileLoginReturnsProfile() throws Exception {

        mockMvc.perform(put(BASE_URL + "/mobileuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batmanCredentials.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'username':'Batman','toursCompleted':[], 'poisVisited':[]}"));

        verify(mockLoginService, times(1)).loginMobile(USERNAME, PW);
        verifyNoMoreInteractions(mockLoginService);

        verify(mockMobileProfileService, times(1)).getMobileProfileByUsername(USERNAME);
        verifyNoMoreInteractions(mockMobileProfileService);
    }

    @Test
    public void UnauthorizedMobileLoginProducesUnauthorizedNotification() throws Exception {
        JSONObject credentials = new JSONObject();
        credentials.put("user", "noone");
        credentials.put("password", "nothing");

        mockMvc.perform(put(BASE_URL + "/mobileuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(credentials.toString()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));

        verify(mockLoginService, times(1)).loginMobile("noone", "nothing");
        verifyNoMoreInteractions(mockLoginService);
        verifyZeroInteractions(mockMobileProfileService);

    }

    @Test
    public void UniqueMobileAccountCreationSucceeds() throws Exception {

        mockMvc.perform(post(BASE_URL + "/mobileuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dogCredentials.toString()))
                .andExpect(status().isCreated());

        verify(mockMobileProfileService, times(1)).checkMobileProfileExists("Dog");
        verify(mockMobileProfileService, times(1)).createMobileProfile(
                "Dog", "getdogbones");
        verifyNoMoreInteractions(mockMobileProfileService);
    }

    @Test
    public void NonUniqueMobileAccountCreationFailsWithMessage() throws Exception {

        mockMvc.perform(post(BASE_URL + "/mobileuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batmanCredentials.toString()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Username is already taken"));

        verify(mockMobileProfileService, times(1)).checkMobileProfileExists(USERNAME);
        verifyNoMoreInteractions(mockMobileProfileService);
    }

    @Test
    public void ValidWebLoginReturnsProfile() throws Exception {

        mockMvc.perform(put(BASE_URL + "/webuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batmanCredentials.toString()))
                .andExpect(status().isOk())
                .andExpect(content().json("{'username':'Batman','Tours':[], 'Beacons':[]}"));

        verify(mockLoginService, times(1)).loginWeb(USERNAME, PW);
        verifyNoMoreInteractions(mockLoginService);

        verify(mockWebProfileService, times(1)).getProfileByUserName(USERNAME);
        verifyNoMoreInteractions(mockMobileProfileService);
    }

    @Test
    public void UnauthorizedWebLoginProducesUnauthorizedNotification() throws Exception {

        mockMvc.perform(put(BASE_URL + "/webuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidCredentials.toString()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized"));

        verify(mockLoginService, times(1)).loginWeb("noone", "nothing");
        verifyNoMoreInteractions(mockLoginService);
        verifyZeroInteractions(mockWebProfileService);

    }

    @Test
    public void UniqueWebAccountCreationSucceeds() throws Exception {

        mockMvc.perform(post(BASE_URL + "/webuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dogCredentials.toString()))
                .andExpect(status().isCreated());

        verify(mockWebProfileService, times(1)).checkWebProfileExists("Dog");
        verify(mockWebProfileService, times(1)).createWebProfile(
                "Dog", "getdogbones");
        verifyNoMoreInteractions(mockWebProfileService);
    }

    @Test
    public void NonUniqueWebAccountCreationFailsWithMessage() throws Exception {

        mockMvc.perform(post(BASE_URL + "/webuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batmanCredentials.toString()))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(content().string("Username is already taken"));

        verify(mockWebProfileService, times(1)).checkWebProfileExists(USERNAME);
        verifyNoMoreInteractions(mockWebProfileService);
    }

    @Test
    public void deleteAuthorizedMobileProfileSucceeds() throws Exception {

        when(mockMobileProfileService.deleteMobileProfile(USERNAME, PW)).thenReturn(1);
        mockMvc.perform(delete(BASE_URL + "/mobileuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batmanCredentials.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void deleteUnauthorizedMobileProfileFails() throws Exception {

        when(mockMobileProfileService.deleteMobileProfile(USERNAME, PW)).thenReturn(0);
        mockMvc.perform(delete(BASE_URL + "/mobileuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(batmanCredentials.toString()))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @After
    public void tearDown() throws Exception {
    }
}
