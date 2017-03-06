package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.ProfileExtractor;
import com.tourtle.web.dao.util.extractor.TourListExtractor;
import com.tourtle.web.domain.Profile;
import com.tourtle.web.domain.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
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
        Profile result = null;
        String sql = "SELECT * FROM userprofile WHERE username = ?";
        try {
            result = jdbcTemplate.query(sql, new Object[]{username}, new ProfileExtractor());
        } catch (RecoverableDataAccessException e) {
            while (retries <= 3) {
                result = jdbcTemplate.query(sql, new Object[]{username}, new ProfileExtractor());
                retries++;
            }
            retries = 0;
        }
        if (result != null) {
            result.setToursCompleted(addCompletedTours(username));
        }
        return result;
    }

    private List<Tour> addCompletedTours(String username) {
        List<Tour> tourids;
        String selectProfileTours =
                "SELECT tour.tourname, userprofiles_tours.tourid FROM userprofiles_tours, tour " +
                        "WHERE userprofiles_tours.username = ? AND tour.tourid = userprofiles_tours.tourid";
        try {
            tourids = jdbcTemplate.query(selectProfileTours,
                    new Object[]{username}, new TourListExtractor());
        } catch (RecoverableDataAccessException e) {
            tourids = retryQueryForTourList(selectProfileTours, username, new TourListExtractor());
        }
        return tourids;
    }

    @Override
    public boolean checkProfileExists(String username) {
        boolean response = false;
        String sql = "SELECT EXISTS(SELECT username FROM userprofile WHERE username = ?)";
        try {
            response = jdbcTemplate.queryForObject(sql, new Object[]{username}, boolean.class);
        } catch (RecoverableDataAccessException e) {
            while (retries <= 3) {
                response = jdbcTemplate.queryForObject(sql, new Object[]{username}, boolean.class);
                retries++;
            }
            retries = 0;
        }
        return response;
    }

    private List<Tour> retryQueryForTourList(String sql, String queryParameter, TourListExtractor extractor) {
        List<Tour> returnList = Collections.emptyList();
        while (retries <= 3) {
            returnList = jdbcTemplate.query(sql, new Object[]{queryParameter}, extractor);
            retries++;
        }
        retries = 0;
        return returnList;
    }
}
