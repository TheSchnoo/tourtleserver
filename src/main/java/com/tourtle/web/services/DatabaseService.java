package com.tourtle.web.services;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private TourtleConversionService conversionService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public JSONArray getAllTours()
    {

        String sql = "SELECT tour.tourname, poi.* " +
                "FROM tour, poi, tours_pois " +
                "WHERE tour.tourid=tours_pois.tourid " +
                "AND poi.beaconid=tours_pois.beaconid";

        List tours = jdbcTemplate.queryForList(sql);

        return conversionService.convertToursListToJsonArray(tours);

    }

    public JSONArray getTourById(String tourId) {
        String sql = "SELECT tour.tourname, poi.* " +
                "FROM tour, poi, tours_pois " +
                "WHERE tour.tourid=" + tourId + " " +
                "AND tour.tourid=tours_pois.tourid " +
                "AND poi.beaconid=tours_pois.beaconid";

        List tours = jdbcTemplate.queryForList(sql);

        return conversionService.convertToursListToJsonArray(tours);
    }

    public JSONArray getTourByName(String tourName) {
        String sql = "SELECT tour.tourname, poi.* " +
                "FROM tour, poi, tours_pois " +
                "WHERE tour.tourname LIKE '" +  tourName.toLowerCase() + "' " +
                "AND tour.tourid=tours_pois.tourid " +
                "AND poi.beaconid=tours_pois.beaconid";

        List tours = jdbcTemplate.queryForList(sql);

        return conversionService.convertToursListToJsonArray(tours);
    }
}
