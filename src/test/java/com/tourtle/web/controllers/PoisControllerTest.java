package com.tourtle.web.controllers;

import com.tourtle.web.domain.POI;
import com.tourtle.web.services.PoiService;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoisControllerTest {

    private final static String BASE_URL = "/pois";

    private MockMvc mockMvc;

    @Mock
    private PoiService mockPoiService;

    @InjectMocks
    private PoisController poisController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(poisController).build();
    }

    @Test
    public void getPoiWithValidIdSucceeds() throws Exception {
        POI poi = new POI();
        poi.setBeaconId("p0");
        poi.setName("The Lair");

        when(mockPoiService.getPoiById("p0")).thenReturn(poi);
        mockMvc.perform(get(BASE_URL + "/p0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.uuid").value("p0"))
                .andExpect(jsonPath("$.name").value("The Lair"));
    }

    @Test
    public void getPoiWithInvalidIdFails() throws Exception {

        when(mockPoiService.getPoiById("p0"))
                .thenThrow(new DataRetrievalFailureException("Inconceivable!"));
        mockMvc.perform(get(BASE_URL + "/p0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found"));
    }

    @Test
    public void createPoiWithValidDataSucceeds() throws Exception {
        when(mockPoiService.createPoi("p0", "{}")).thenReturn(2);

        mockMvc.perform(put(BASE_URL + "/p0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 2"));
    }

    @Test
    public void createPoiWithMalformedDataFails() throws Exception {
        when(mockPoiService.createPoi("p0", "{}")).thenThrow(new JSONException("Dark Side of the Moon"));

        mockMvc.perform(put(BASE_URL + "/p0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Malformed POI data"));
    }

    @Test
    public void modifyPoiWithValidPoiIdSucceeds() throws Exception {
        when(mockPoiService.postPoi("p0", "{}")).thenReturn(1);
        when(mockPoiService.checkPOIExists("p0")).thenReturn(true);
        mockMvc.perform(post(BASE_URL + "/p0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 1"));
    }

    @Test
    public void modifyPoiWithInvalidPoiIdAffectsZeroRows() throws Exception {
        when(mockPoiService.postPoi("p0", "{}")).thenReturn(0);
        mockMvc.perform(post(BASE_URL + "/p0")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 0"));
    }

    @Test
    public void deleteWithValidPoiIdSucceeds() throws Exception {
        when(mockPoiService.deletePoi("p0")).thenReturn(1);
        mockMvc.perform(delete(BASE_URL + "/p0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 1"));

        verify(mockPoiService, times(1)).deletePoi("p0");
        verifyNoMoreInteractions(mockPoiService);
    }

    @Test
    public void deleteWithInvalidPoiIdFails() throws Exception {
        when(mockPoiService.deletePoi("p0"))
                .thenThrow(new DataRetrievalFailureException("That poi is not real."));
        mockMvc.perform(delete(BASE_URL + "/p0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found"));

        verify(mockPoiService, times(1)).deletePoi("p0");
        verifyNoMoreInteractions(mockPoiService);
    }
}