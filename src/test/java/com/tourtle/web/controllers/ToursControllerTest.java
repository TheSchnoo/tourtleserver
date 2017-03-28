package com.tourtle.web.controllers;

import com.tourtle.web.domain.Tour;
import com.tourtle.web.services.TourService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    @Test
    public void returnAllTours() throws Exception {
        List<Tour> tours = createTours();
        when(mockTourService.getAllTours()).thenReturn(tours);
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.[0].tourId").value("t0"))
                .andExpect(jsonPath("$.[0]..name").exists())
                .andExpect(jsonPath("$.[0]..imageurl").exists())
                .andExpect(jsonPath("$.[0]..beacons").exists())
                .andExpect(jsonPath("$.[1]..tourId").value("t1"));
    }

    @Test
    public void getToursByNameReturnsTourForValidName() throws Exception {
        Tour t0 = new Tour();
        t0.setTourId("t0");
        t0.setName("Tour0");

        when(mockTourService.getTourByName("Tour0")).thenReturn(t0);
        mockMvc.perform(get(BASE_URL + "?tourName=Tour0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourId").value("t0"))
                .andExpect(jsonPath("$..name").value("Tour0"))
                .andExpect(jsonPath("$..imageurl").exists())
                .andExpect(jsonPath("$..beacons").exists());
    }

    @Test
    public void getToursWithInvalidNameFails() throws Exception {
        when(mockTourService.getTourByName("Tour0"))
                .thenThrow(new DataRetrievalFailureException("THIS SHOULDN'T CHANGE OUTCOME"));
        mockMvc.perform(get(BASE_URL + "?tourName=Tour0"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found"));
    }

    @Test
    public void getToursByIdReturnsTourForValidId() throws Exception {
        Tour t0 = new Tour();
        t0.setTourId("t0");

        when(mockTourService.getTourById("t0")).thenReturn(t0);
        mockMvc.perform(get(BASE_URL + "/t0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tourId").value("t0"))
                .andExpect(jsonPath("$..name").exists())
                .andExpect(jsonPath("$..imageurl").exists())
                .andExpect(jsonPath("$..beacons").exists());
    }

    @Test
    public void noTourReturnedWhenTourIdNotValid() throws Exception {
        when(mockTourService.getTourById("t2"))
                .thenThrow(new DataRetrievalFailureException("THIS SHOULDN'T CHANGE OUTCOME"));

        mockMvc.perform(get(BASE_URL + "/t2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Resource not found"));
    }

    @Test
    public void createTourWithUniqueIdSucceeds() throws Exception {
        when(mockTourService.createTour("t3", "{}")).thenReturn(1);
        mockMvc.perform(put(BASE_URL + "/t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void createTourWithNonUniqueIdFails() throws Exception {
        when(mockTourService.createTour("t3", "{}")).thenThrow(new DuplicateKeyException("Duplicate key"));
        mockMvc.perform(put(BASE_URL + "/t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isImUsed())
                .andExpect(content().string("A tour already exists with that tourid"));
    }

    @Test
    public void modifyValidTourSucceeds() throws Exception {
        when(mockTourService.postTour("t3", "{}")).thenReturn(1);
        mockMvc.perform(post(BASE_URL + "/t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 1"));
    }

    @Test
    public void modifyInvalidTourFails() throws Exception {
        when(mockTourService.postTour("t3", "{}")).thenReturn(0);
        mockMvc.perform(post(BASE_URL + "/t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rows Affected: 0"));
    }

    @Test
    public void deleteWithBodyDeletesFromTour() throws Exception {
        when(mockTourService.deleteFromTours("t3", "{}")).thenReturn(1);
        mockMvc.perform(delete(BASE_URL + "/t3")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 1"));

        verify(mockTourService, times(1)).deleteFromTours("t3", "{}");
        verifyNoMoreInteractions(mockTourService);
    }

    @Test
    public void deleteWithoutBodyDeletesTour() throws Exception {
        when(mockTourService.deleteTour("t3")).thenReturn(1);
        mockMvc.perform(delete(BASE_URL + "/t3"))
                .andExpect(status().isOk())
                .andExpect(content().string("Rows Affected: 1"));

        verify(mockTourService, times(1)).deleteTour("t3");
        verifyNoMoreInteractions(mockTourService);
    }

    @Test
    public void deleteInvalidTourFails() throws Exception {
        when(mockTourService.deleteTour("t3")).thenReturn(0);
        mockMvc.perform(delete(BASE_URL + "/t3"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Rows Affected: 0"));

        verify(mockTourService, times(1)).deleteTour("t3");
        verifyNoMoreInteractions(mockTourService);
    }

    @After
    public void tearDown() throws Exception {
    }

    private List<Tour> createTours() {
        List<Tour> tours = new ArrayList<>();
        Tour t0 = new Tour();
        t0.setTourId("t0");
        Tour t1 = new Tour();
        t1.setTourId("t1");

        tours.add(t0);
        tours.add(t1);

        return tours;
    }

}