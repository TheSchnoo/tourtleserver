package com.tourtle.web.dao.util.extractor;

import com.tourtle.web.domain.Tour;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Creates a single Tour from a result set
 */
public class TourExtractor implements ResultSetExtractor<Tour> {

    @Override
    public Tour extractData(ResultSet rs) throws SQLException, DataAccessException {
        rs.next();      // Move into the first position.  We are only expecting one thing here.
        Tour tour = new Tour();
        tour.setName(rs.getString("tourname"));
        tour.setTourId(rs.getString("tourid"));
        tour.setImageurl(rs.getString("imageURL"));
        return tour;
    }
}