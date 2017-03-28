package com.tourtle.web.services;

import com.tourtle.web.dao.PoiDao;
import com.tourtle.web.domain.POI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PoiServiceTest {

    private static final String ID = "id";
    private static final String INVALID_ID = "InvalidId";
    private static final String BODY = "{'body':'body'}";

    POI poi0;

    @Mock
    private PoiDao poiDao;

    @InjectMocks
    private PoiService poiService;

    @Before
    public void setUp() throws Exception {
        poi0 = new POI();
        poi0.setBeaconId(ID);

        when(poiDao.checkPoiExists(ID)).thenReturn(true);
        when(poiDao.checkPoiExists(INVALID_ID)).thenThrow(new DataRetrievalFailureException("Couldn't find resource"));
        when(poiDao.getPoiByID(ID)).thenReturn(poi0);
        when(poiDao.createPoi(ID, BODY)).thenReturn(1);
        when(poiDao.postPoi(ID, BODY)).thenReturn(1);
        when(poiDao.deletePoi(ID)).thenReturn(1);
    }

    @Test
    public void checkValidPOIExists() throws Exception {
        assertThat(poiService.checkPOIExists(ID)).isTrue();
    }

    @Test
    public void checkInvalidPOIDoesNotExists() throws Exception {
        assertThatThrownBy(() -> { poiService.checkPOIExists(INVALID_ID); })
            .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void checkAllPOIExist() throws Exception {
        final String[] pois = new String[2];
        pois[0] = poi0.getBeaconId();
        assertThat(poiService.checkAllPOIExist(pois)).isFalse();

        pois[1] = poi0.getBeaconId();
        assertThat(poiService.checkAllPOIExist(pois)).isTrue();
    }

    @Test
    public void getPoiByValidIdReturnsPoi() throws Exception {
        assertThat(poiService.getPoiById(ID)).isEqualTo(poi0);
    }

    @Test
    public void getPoiByInvalidIdThrowsException() throws Exception {
        assertThatThrownBy(() -> { poiService.getPoiById(INVALID_ID); })
                .isInstanceOf(DataRetrievalFailureException.class);
    }

    @Test
    public void createPoi() throws Exception {
        assertThat(poiService.createPoi(ID, BODY)).isEqualTo(1);
    }

    @Test
    public void postPoi() throws Exception {
        assertThat(poiService.postPoi(ID, BODY)).isEqualTo(1);
    }

    @Test
    public void deletePoi() throws Exception {
        assertThat(poiService.deletePoi(ID)).isEqualTo(1);
    }

}