package com.tourtle.web.dao.util.extractor;

import com.tourtle.web.domain.POI;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PoiListExtractor implements ResultSetExtractor<List<POI>> {

    @Override
    public List<POI> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<POI> poiList = new ArrayList<>();

        while (rs.next()) {
            POI poi = new POI();
            poi.setBeaconId(rs.getString("beaconid"));
            poi.setName(rs.getString("name"));
            poi.setLat(rs.getDouble("lat"));
            poi.setLon(rs.getDouble("lon"));
            poi.setDescription(rs.getString("description"));
            poi.setImageurl(rs.getString("imageurl"));

            poiList.add(poi);
        }
        return poiList;
    }
}
