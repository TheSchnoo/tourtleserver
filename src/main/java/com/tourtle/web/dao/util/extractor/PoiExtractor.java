package com.tourtle.web.dao.util.extractor;

import com.tourtle.web.domain.POI;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a single POI from a result set
 */
public class PoiExtractor implements ResultSetExtractor<POI> {

    @Override
    public POI extractData(ResultSet rs) throws SQLException, DataAccessException {
        rs.next();      // Move into the first position.  We are only expecting one thing here.
        POI p = new POI();
        p.setBeaconId(rs.getString("beaconid"));
        p.setName(rs.getString("name"));
        p.setLat(rs.getDouble("lat"));
        p.setLon(rs.getDouble("lon"));
        p.setDescription(rs.getString("description"));
        p.setImageurl(rs.getString("imageurl"));

        return p;
    }
}
