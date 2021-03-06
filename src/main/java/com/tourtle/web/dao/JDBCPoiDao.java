package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.StringIDExtractor;
import com.tourtle.web.dao.util.extractor.PoiExtractor;
import com.tourtle.web.domain.POI;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.List;

@Repository
public class JDBCPoiDao implements PoiDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean checkPoiExists(String beaconId) {
        String sql = "SELECT EXISTS(SELECT beaconid FROM poi WHERE beaconid = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{beaconId}, boolean.class);
    }

    @Override
    public POI getPoiByID(String beaconId) {
        String sql = "SELECT * FROM poi WHERE beaconid = ?";
        POI result = jdbcTemplate.query(sql, new Object[]{beaconId}, new PoiExtractor());
        return result;
    }

    @Override
    public int createPoi(String poiId, String body) throws JSONException {

        JSONObject poiJson = new JSONObject(body);
        String name = poiJson.getString("name");
        double lat = poiJson.getDouble("lat");
        double lon = poiJson.getDouble("lon");
        String description = poiJson.getString("description");
        String pictureLink = poiJson.getString("imageurl");

        String sql = String.format("INSERT INTO poi VALUES ('%s', %e, %e, '%s', '%s', '%s', 'fiona')",
                poiId, lat, lon, description, pictureLink, name);

        return jdbcTemplate.update(sql);
    }

    @Override
    public int postPoi(String poiId, String body) {
        JSONObject poiJson = new JSONObject(body);
        String updatePoiString = "UPDATE poi SET";
        int i = 0;
        for (Iterator<String> it = poiJson.keys(); it.hasNext(); ) {
            String key = it.next();
            if (i == 0) {
                updatePoiString = String.format("%s %s = '%s'", updatePoiString, key, poiJson.get(key).toString());
                i++;
            } else {
                updatePoiString = String.format("%s, %s = '%s'", updatePoiString, key, poiJson.get(key).toString());
            }
        }
        updatePoiString = updatePoiString + " " + "WHERE beaconid = '" + poiId + "'";
        return jdbcTemplate.update(updatePoiString);
    }

    @Override
    public int deletePoi(String poiId) {
        checkPoiExists(poiId);
        String updatePoiString = "DELETE FROM poi WHERE beaconid = '" + poiId + "'";
        return jdbcTemplate.update(updatePoiString);
    }

    @Override
    public int addPoiToMobileUser(String profileName, String uuid) {
        String sql = "INSERT IGNORE into userprofiles_pois values (?, ?)";
        return jdbcTemplate.update(sql, new Object[]{profileName, uuid});

    }

    @Override
    public List<String> getCompletedPOIByMobileUser(String username) {
        String sql = "SELECT beaconid AS id FROM userprofiles_pois WHERE username = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, new StringIDExtractor());
    }

    @Override
    public List<String> getCompletedPOIIdForTourByMobileUser(String username, int tourId) {
        String sql = "SELECT up.beaconid AS id FROM userprofiles_pois up JOIN tours_pois tp ON up.beaconid = tp.beaconid WHERE username = ? and tourid = ?";
        return jdbcTemplate.query(sql, new Object[]{username, tourId}, new StringIDExtractor());
    }

    @Override
    public List<String> getPOIIdForTour(int tourId) {
        String sql = "SELECT beaconid AS id FROM tours_pois WHERE tourid = ?";
        return jdbcTemplate.query(sql, new Object[]{tourId}, new StringIDExtractor());
    }

    @Override
    public List<String> getOwnedPOIByWebUser(String username) {
        String sql = "SELECT beaconid as id FROM poi WHERE owner = ?";
        return jdbcTemplate.query(sql, new Object[]{username}, new StringIDExtractor());
    }
}
