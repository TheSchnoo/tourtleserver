package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.ProfileExtractor;
import com.tourtle.web.dao.util.extractor.TourListExtractor;
import com.tourtle.web.domain.Profile;
import com.tourtle.web.domain.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public class JDBCProfileDao implements ProfileDao {

    private int retries;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Profile getProfileByUsername(String username) {
        String sql = "SELECT * FROM userprofile WHERE username = ?";
        Profile result = jdbcTemplate.query(sql, new Object[]{username}, new ProfileExtractor());
        result.setToursCompleted(addCompletedTours(username));
        return result;
    }

    private List<Tour> addCompletedTours(String username) {
        List<Tour> tourids = Collections.emptyList();
        String selectProfileTours =
                "SELECT tour.tourname, userprofiles_tours.tourid FROM userprofiles_tours, tour " +
                        "WHERE userprofiles_tours.username = ? AND tour.tourid = userprofiles_tours.tourid";
        try {
            tourids = jdbcTemplate.query(selectProfileTours,
                    new Object[]{username}, new TourListExtractor());
        } catch (DataAccessException e) {
            while (retries <= 3) {
                tourids = jdbcTemplate.query(selectProfileTours,
                        new Object[]{username}, new TourListExtractor());
                retries++;
            }
            retries = 0;
        }
        return tourids;
    }

    @Override
    public boolean checkProfileExists(String username) {
        String sql = "SELECT EXISTS(SELECT username FROM userprofile WHERE username = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, boolean.class);
    }
}
