package com.tourtle.web.controllers;

import com.tourtle.web.domain.MobileProfile;
import com.tourtle.web.services.MobileProfileService;
import com.tourtle.web.services.PoiService;
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

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MobileUserControllerTest {

    private static final String BASE_URL = "/mobileuser";

    private MockMvc mockMvc;

    @Mock
    private MobileProfileService mockMobileProfileService;
    @Mock
    private PoiService mockPoiService;

    @InjectMocks
    private MobileUserController mobileUserController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(mobileUserController).build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void addNewPOIToVisitedWithValidProfileSucceeds() throws Exception {

        List<String> pois = new ArrayList<>();
        pois.add("p-o-i");

        String[] visitedArray = new String[1];
        visitedArray[0] = "p-o-i";

        MobileProfile mobileProfile = new MobileProfile();
        mobileProfile.setUsername("profile0");

        mobileProfile.setPoiVisited(pois);

        when(mockMobileProfileService.checkMobileProfileExists("profile0")).thenReturn(true);
        when(mockMobileProfileService.updateVisitedPOIs("profile0", visitedArray))
                .thenReturn(mobileProfile);
        when(mockPoiService.checkAllPOIExist(visitedArray)).thenReturn(true);

        JSONObject input = new JSONObject();
        input.put("uuids", visitedArray);

        mockMvc.perform(post(BASE_URL + "/profile0/poivisited")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input.toString()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$..username").value("profile0"))
                .andExpect(jsonPath("$..toursCompleted").exists())
                .andExpect(jsonPath("$..poisVisited").isArray())
                .andExpect(jsonPath("$..poisVisited[0]").value("p-o-i"));
    }

    @Test
    public void addNewPOIToVisitedWithInvalidProfileFails() throws Exception {
        when(mockMobileProfileService.checkMobileProfileExists("profile0")).thenReturn(false);
        when(mockPoiService.checkPOIExists("p-o-i")).thenReturn(true);

        JSONObject input = new JSONObject();
        input.put("uuid", "p-o-i");

        mockMvc.perform(post(BASE_URL + "/profile0/poivisited")
                .contentType(MediaType.APPLICATION_JSON)
                .content(input.toString()))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource does not exist"));
    }


}