package com.tourtle.web.services;

import com.tourtle.web.dao.TourDao;
import com.tourtle.web.domain.Tour;
import com.tourtle.web.domain.WebProfile;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TourServiceTest {

    private static final String ID = "t0";
    private static final String NAME = "Elwood City";

    @Mock
    private TourDao tourDao;

    @InjectMocks
    private TourService tourService;

    private Tour t0;
    private Tour t1;


    @Before
    public void setUp() throws Exception {
        t0 = new Tour();
        t0.setName(NAME);
        t0.setTourId(ID);

        t1 = new Tour();
        t1.setName("The Local Library");
        t1.setTourId("t1");

        when(tourDao.checkTourExistsById(ID)).thenReturn(true);
        when(tourDao.checkTourExistsByName(NAME)).thenReturn(true);
        when(tourDao.checkTourExistsById("InvalidId")).thenReturn(false);
        when(tourDao.checkTourExistsByName("InvalidName")).thenReturn(false);
        when(tourDao.getTourById(ID)).thenReturn(t0);
        when(tourDao.getTourByName(NAME)).thenReturn(t0);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getTourById() throws Exception {
        assertThat(tourService.getTourById(ID)).isEqualToComparingFieldByField(t0);
    }

    @Test
    public void getTourByInvalidIdFails() throws Exception {
        assertThatThrownBy(() -> { tourService.getTourById("InvalidId"); })
                .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void getTourByName() throws Exception {
        assertThat(tourService.getTourByName(NAME)).isEqualToComparingFieldByField(t0);
    }

    @Test
    public void getTourByInvalidNameFails() throws Exception {
        assertThatThrownBy(() -> { tourService.getTourByName("InvalidName"); })
                .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void getAllTours() throws Exception {
        List<Tour> tours = new ArrayList<>();
        tours.add(t0);
        tours.add(t1);

        when(tourDao.getAllTours()).thenReturn(tours);

        assertThat(tourService.getAllTours()).containsAll(tours);
    }

    @Test
    public void createTour() throws Exception {
        when(tourDao.createTour(ID, "{}")).thenReturn(1);
        assertThat(tourService.createTour(ID, "{}")).isEqualTo(1);
    }

    @Test
    public void deleteTour() throws Exception {
        when(tourDao.deleteTour(ID)).thenReturn(1);
        assertThat(tourService.deleteTour(ID)).isEqualTo(1);
    }

    @Test
    public void deleteFromTours() throws Exception {
        when(tourDao.deleteFromTours(ID, "{}")).thenReturn(1);
        assertThat(tourService.deleteFromTours(ID, "{}")).isEqualTo(1);
    }

    @Test
    public void postTour() throws Exception {
        when(tourDao.postTour(ID, "{}")).thenReturn(1);
        assertThat(tourService.postTour(ID, "{}")).isEqualTo(1);
    }

}