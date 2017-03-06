package com.tourtle.web.dao.util.extractor;

import com.tourtle.web.domain.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Apathetic spawn of Wesb on 3/5/17.
 */
public class ProfileExtractor implements ResultSetExtractor<Profile> {

    @Override
    public Profile extractData(ResultSet rs) throws SQLException, DataAccessException {
        rs.next();      // Move into the first position.  We are only expecting one thing here.
        Profile profile = new Profile();
        profile.setUsername(rs.getString("username"));
        return profile;
    }
}