package com.tourtle.web.services;

import com.tourtle.web.Domain.POI;
import com.tourtle.web.Domain.Tour;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class DatabaseService {

    @Autowired
    private TourtleConversionService conversionService;

    @Autowired
    private ToursService toursService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Tour> getAllTours() {

        String sql = "SELECT tour.tourname, poi.* " +
                "FROM tour, poi, tours_pois " +
                "WHERE tour.tourid=tours_pois.tourid " +
                "AND poi.beaconid=tours_pois.beaconid";

        List<POI> tours = this.jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    POI poi = new POI();
                    poi.setTourname(rs.getString("tourname"));
                    poi.setName(rs.getString("name"));
                    poi.setBeaconid(rs.getString("beaconid"));
                    poi.setLat(rs.getDouble("lat"));
                    poi.setLon(rs.getDouble("lon"));
                    poi.setDescription(rs.getString("description"));
                    poi.setImageurl(rs.getString("imageurl"));
                    return poi;
                });

        return toursService.filterPoisToToursList(tours);
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

    public List<Tour> getTourByName(String tourName) {
        String sql = "SELECT tour.tourname, poi.* " +
                "FROM tour, poi, tours_pois " +
                "WHERE tour.tourname LIKE '" +  tourName.toLowerCase() + "' " +
                "AND tour.tourid=tours_pois.tourid " +
                "AND poi.beaconid=tours_pois.beaconid";

        List<POI> tours = this.jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    POI poi = new POI();
                    poi.setTourname(rs.getString("tourname"));
                    poi.setName(rs.getString("name"));
                    poi.setBeaconid(rs.getString("beaconid"));
                    poi.setLat(rs.getDouble("lat"));
                    poi.setLon(rs.getDouble("lon"));
                    poi.setDescription(rs.getString("description"));
                    poi.setImageurl(rs.getString("imageurl"));
                    return poi;
                });

        return toursService.filterPoisToToursList(tours);
    }

    public int[] createTour(String tourId, JSONObject body) {

        String name = body.getString("name");

        JSONArray poisOnTour = body.getJSONArray("beacons");

        String sql = String.format("INSERT INTO tour VALUES (%s, '%s');", tourId, name);

        String valueString = "";

        for(int i=0; i < poisOnTour.length(); i++) {
            if (i == 0) {
                valueString = String.format("INSERT INTO tours_pois VALUES (%s, %s)", tourId, poisOnTour.get(i));
            } else {
                valueString = String.format("%s, (%s, %s)", valueString, tourId, poisOnTour.get(i));
            }
        }

        return jdbcTemplate.batchUpdate(sql, valueString);

    }

    public int[] deleteTour(String tourId, JSONObject body) {

        int[] rowsAffected;

        String valueString = "";

        if (body.has("beacons")) {
            JSONArray poisOnTour = body.getJSONArray("beacons");

            List<Object[]> tourPoisList = new LinkedList<>();

            for(int i=0; i < poisOnTour.length(); i++) {
                valueString = String.format("%s DELETE FROM tours_pois WHERE tourid=%s AND beaconid=%s;", valueString,
                        tourId, poisOnTour.get(i));
            }
        } else {
            valueString = String.format("DELETE FROM tour where tourid=%s; DELETE FROM tours_pois WHERE tourid=%s",
                    tourId, tourId);
        }
        return jdbcTemplate.batchUpdate(valueString);
    }
}
