package com.tourtle.web.dao;

import com.tourtle.web.dao.util.extractor.ProfileExtractor;
import com.tourtle.web.domain.MobileProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCMobileProfileDao implements MobileProfileDao {

    private int retries;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public boolean checkMobileProfileExists(String username) {
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

    public void createMobileProfile(String username, String password) {
        String sqlInsert = String.format("INSERT INTO userprofile (username, userpass) VALUES ('%s', '%s')",
                username, password);
        jdbcTemplate.update(sqlInsert);
    }

    @Override
    public int deleteMobileProfile(String username, String password) {
        String sqlInsert = String.format("DELETE FROM userprofile WHERE username = '%s' AND userpass = '%s'",
                username, password);
        return jdbcTemplate.update(sqlInsert);
    }
}
