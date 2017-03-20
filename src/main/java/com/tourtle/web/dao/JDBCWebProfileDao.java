package com.tourtle.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JDBCWebProfileDao implements WebProfileDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean checkWebProfileExists(String username) {
            String sql = "SELECT EXISTS(SELECT username FROM webprofile WHERE username = ?)";
            boolean response = jdbcTemplate.queryForObject(sql, new Object[]{username}, boolean.class);
            return response;
    }

    public void createWebProfile(String username, String password) {
        String sqlInsert = String.format("INSERT INTO webprofile (username, userpass) VALUES ('%s', '%s')", username, password);
        jdbcTemplate.update(sqlInsert);
    }
}
