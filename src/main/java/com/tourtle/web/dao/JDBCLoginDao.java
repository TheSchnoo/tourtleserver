package com.tourtle.web.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * JDBC implementation of the LoginDAO.
 */
@Repository
public class JDBCLoginDao implements LoginDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public boolean loginMobile(String username, String password) {
        String sql = "SELECT exists (SELECT * FROM userprofile WHERE username = ? AND userpass = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{username, password}, boolean.class);
    }

    public boolean loginWeb(String username, String password) {
        String sql = "SELECT exists (SELECT * FROM webprofile WHERE username = ? AND userpass = ?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{username, password}, boolean.class);
    }
}
