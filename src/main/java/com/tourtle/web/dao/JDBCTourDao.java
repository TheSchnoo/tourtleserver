package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.PoiListExtractor;
import com.tourtle.web.dao.util.extractor.TourExtractor;
import com.tourtle.web.dao.util.extractor.TourListExtractor;
import com.tourtle.web.domain.POI;
import com.tourtle.web.domain.Tour;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class JDBCTourDao implements TourDao {

    private int retries;

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
        List<Tour> result = Collections.emptyList();
        try {
            result = jdbcTemplate.query(sql, new Object[]{}, new TourListExtractor());
        } catch (DataAccessException e) {
            while (retries <= 3) {
                result = jdbcTemplate.query(sql, new Object[]{}, new TourListExtractor());
                retries++;
            }
            retries = 0;
        }
        for (Tour tour : result) {
            addTourBeaconIds(tour);
        }
        return result;
    }

    @Override
    public int createTour(String tourId, String body) throws DuplicateKeyException {
        JSONObject tourJson = new JSONObject(body);
        JSONArray poiArray = tourJson.getJSONArray("beacons");
        String sql = "INSERT INTO tour VALUES (" + tourId + ", '" + tourJson.get("name") + "')";
        String poisSql = "INSERT INTO tours_pois VALUES ";
        String toursToInsert = "(" + tourId + ", %s)";
        for (int i=0; i<poiArray.length(); i++){
            if (i == 0) {
                poisSql = String.format(poisSql + toursToInsert, poiArray.get(i));
            } else {
                poisSql = String.format(poisSql + "," + toursToInsert, poiArray.get(i));
            }
        }
        int sum = 0;
        for (int i : jdbcTemplate.batchUpdate(sql, poisSql)) {
            sum = sum + i;
        }
        return sum;
    }

    @Override
    public int deleteTour(String tourId) {
        String deletePoisOnTourString = "DELETE FROM tours_pois WHERE tourid = " + tourId;
        String deleteTourString = "DELETE FROM tour WHERE tourid = " + tourId;
        int sum = 0;
        for (int i : jdbcTemplate.batchUpdate(deletePoisOnTourString, deleteTourString)) {
            sum = sum + i;
        }
        return sum;
    }

    @Override
    public int deleteFromTours(String tourId, String body) {
        JSONObject bodyJson = new JSONObject(body);
        JSONArray poisToDelete = (JSONArray) bodyJson.get("beacons");
        String[] deletePoisSqlArray = new String[poisToDelete.length()];
        for (int i=0; i<poisToDelete.length(); i++) {
            deletePoisSqlArray[i] =
                    "DELETE FROM tours_pois WHERE tourid = " + tourId + " AND beaconId = " + poisToDelete.get(i);
        }
        int sum = 0;
        for (int i : jdbcTemplate.batchUpdate(deletePoisSqlArray)) {
            sum = sum + i;
        }
        return sum;
    }

    @Override
    public int postTour(String tourId, String body) {
        JSONObject tourObject = new JSONObject(body);
        String updateTourSql = "";
        String updateStopsSql = "";
        if (tourObject.has("name")) {
            updateTourSql = "UPDATE tour SET tourname = '" + tourObject.get("name") +
                    "' WHERE tourid = " + tourId + ";";
        }
        if (tourObject.has("beacons")) {
            JSONArray beaconsArray = (JSONArray) tourObject.get("beacons");
            updateStopsSql = "INSERT INTO tours_pois VALUES ";
            for (int i=0; i<beaconsArray.length(); i++) {
                if (i==0) {
                    updateStopsSql = updateStopsSql + "(" + tourId + "," + beaconsArray.get(i) + ")";
                } else {
                    updateStopsSql = updateStopsSql + ",(" + tourId + "," + beaconsArray.get(i) + ")";
                }
            }
        }
        int sum = 0;
        for (int i : jdbcTemplate.batchUpdate(updateTourSql, updateStopsSql)) {
            sum = sum + i;
        }
        return sum;
    }

    private void addTourBeaconIds(Tour result) {
        String selectBeaconIds =
                "SELECT poi.* FROM tours_pois, poi " +
                        "WHERE tours_pois.tourid = ? AND tours_pois.beaconid = poi.beaconid";
        try {
            List<POI> tourids = jdbcTemplate.query(selectBeaconIds,
                    new Object[]{result.getTourId()},
                    new PoiListExtractor());
            result.setPois(tourids);
        } catch (DataAccessException e) {
            if (retries < 3) {
                retries++;
                addTourBeaconIds(result);
            } else {
                retries = 0;
                throw e;
            }
        }
    }
}
