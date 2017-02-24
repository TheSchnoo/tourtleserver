package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.PoiExtractor;
import com.tourtle.web.domain.POI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

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
}
