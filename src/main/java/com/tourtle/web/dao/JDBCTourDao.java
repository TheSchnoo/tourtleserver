package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.*;
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
        String sql = String.format("INSERT INTO tour VALUES ('%s', '%s', '%s', '%s')",
                tourId, tourJson.get("name"), tourJson.get("owner"), tourJson.get("imageurl"));
        int[] columnsAffectedArray;
        if (tourJson.has("beacons")) {
            JSONArray poiArray = tourJson.getJSONArray("beacons");
            String poisSql = "INSERT INTO tours_pois VALUES ";
            String toursToInsert = "(" + tourId + ", %s)";
            for (int i=0; i<poiArray.length(); i++){
                if (i == 0) {
                    poisSql = String.format(poisSql + toursToInsert, poiArray.get(i));
                } else {
                    poisSql = String.format(poisSql + "," + toursToInsert, poiArray.get(i));
                }
            }
            columnsAffectedArray = jdbcTemplate.batchUpdate(sql, poisSql);
        } else {
            columnsAffectedArray = jdbcTemplate.batchUpdate(sql);
        }
        int sum = 0;
        for (int i : columnsAffectedArray) {
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
        int sum = 0;
        if (tourObject.has("name")) {
            updateTourSql = "UPDATE tour SET tourname = '" + tourObject.get("name") +
                    "' WHERE tourid = " + tourId;
            sum += jdbcTemplate.update(updateTourSql);
        }
        if (tourObject.has("beacons")) {
            JSONArray beaconsArray = (JSONArray) tourObject.get("beacons");
            updateStopsSql = "INSERT INTO tours_pois VALUES ";
            for (int i=0; i<beaconsArray.length(); i++) {
                if (i==0) {
                    updateStopsSql = updateStopsSql + "(" + tourId + ", '" + beaconsArray.get(i) + "')";
                } else {
                    updateStopsSql = updateStopsSql + ",(" + tourId + ", '" + beaconsArray.get(i) + "')";
                }
            }
            sum += jdbcTemplate.update(updateStopsSql);
        }
        return sum;
    }

    @Override
    public int addCompletedTourToMobileUser(int tourId, String username) {
        String sql = "INSERT IGNORE INTO userprofiles_tours VALUES (?,?)";
        return jdbcTemplate.update(sql, new Object[]{username, tourId});
    }

    private void addTourBeaconIds(Tour result) {
        List<POI> tourids = Collections.emptyList();
        String selectBeaconIds =
                "SELECT poi.* FROM tours_pois, poi " +
                        "WHERE tours_pois.tourid = ? AND tours_pois.beaconid = poi.beaconid";
        try {
            tourids = jdbcTemplate.query(selectBeaconIds,
                    new Object[]{result.getTourId()},
                    new PoiListExtractor());
            result.setPois(tourids);
        } catch (DataAccessException e) {
            while (retries <= 3) {
                tourids = jdbcTemplate.query(selectBeaconIds,
                        new Object[]{result.getTourId()},
                        new PoiListExtractor());
                retries++;
            }
            retries = 0;
        }
        result.setPois(tourids);
    }

    public List<String> getCompletedToursByMobileUser(String username) {
        String sql = "SELECT tourid AS id FROM userprofiles_tours WHERE username = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, new StringIDExtractor());
    }

    public List<String> getOwnedToursByWebUser(String username) {
        String sql = "SELECT tourid AS id FROM tour WHERE owner = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, new StringIDExtractor());
    }

    @Override
    public List<Integer> getAllTourIdsUsingPOIId(String beaconId) {
        String sql = "SELECT t.tourid AS id FROM tours_pois tp JOIN tour t ON t.tourid = tp.tourid WHERE tp.beaconid=?";
        return jdbcTemplate.query(sql, new Object[]{beaconId}, new IntegerIdExtractor());

    }

}
