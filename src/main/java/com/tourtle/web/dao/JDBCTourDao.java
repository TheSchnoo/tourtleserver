package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.PoiListExtractor;
import com.tourtle.web.dao.util.extractor.TourExtractor;
import com.tourtle.web.dao.util.extractor.TourListExtractor;
import com.tourtle.web.domain.POI;
import com.tourtle.web.domain.Tour;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JDBCTourDao implements TourDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public boolean checkTourExistsById(String tourId) {
        String sql = "SELECT EXISTS(SELECT tourid FROM tour WHERE tourid = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{tourId}, boolean.class);
    }

    @Override
    public boolean checkTourExistsByName(String tourName) {
        String sql = "SELECT EXISTS(SELECT tourid FROM tour WHERE tourname = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{tourName}, boolean.class);
    }

    @Override
    public Tour getTourById(String tourId) {
        String sql = "SELECT * FROM tour WHERE tourid = ?";
        Tour result = jdbcTemplate.query(sql, new Object[]{tourId}, new TourExtractor());
        addTourBeaconIds(result);
        return result;
    }

    @Override
    public Tour getTourByName(String tourName) {
        String sql = "SELECT * FROM tour WHERE tourname = ?";
        Tour result = jdbcTemplate.query(sql, new Object[]{tourName}, new TourExtractor());
        addTourBeaconIds(result);
        return result;
    }

    @Override
    public List<Tour> getAllTours() {
        String sql = "SELECT * FROM tour";
        List<Tour> result = jdbcTemplate.query(sql, new Object[]{}, new TourListExtractor());

        for (Tour tour : result) {
            addTourBeaconIds(tour);
        }

        return result;
    }

    @Override
    public int createTour(String tourId, String body) {
        JSONObject tourJson= new JSONObject(body);
        JSONArray poiArray = tourJson.getJSONArray("beacons");
        String sql = "INSERT INTO tour VALUES (" + tourId + ", '" + tourJson.get("name") + "')";
        String poisSql = "INSERT INTO tours_pois VALUES (" + tourId + ", :beaconid)";
        return jdbcTemplate.update(sql) + jdbcTemplate.update(poisSql, poiArray);
    }

    private void addTourBeaconIds(Tour result) {
        String selectBeaconIds =
                "SELECT poi.* FROM tours_pois, poi " +
                "WHERE tours_pois.tourid = ? AND tours_pois.beaconid = poi.beaconid";
        List<POI> tourids = jdbcTemplate.query(selectBeaconIds,
                new Object[]{result.getTourId()},
                new PoiListExtractor());
        result.setPois(tourids);
    }
}
