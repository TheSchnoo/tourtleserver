package com.tourtle.web.dao.util.extractor;

import com.tourtle.web.domain.Tour;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TourListExtractor implements ResultSetExtractor<List<Tour>> {

    @Override
    public List<Tour> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Tour> toursList = new ArrayList<>();

        while (rs.next()) {
            Tour tour = new Tour();
            tour.setName(rs.getString("tourname"));
            tour.setTourId(rs.getString("tourid"));
            toursList.add(tour);
        }
        return toursList;
    }
}
