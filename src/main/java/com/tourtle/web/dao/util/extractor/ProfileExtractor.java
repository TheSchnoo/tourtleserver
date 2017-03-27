package com.tourtle.web.dao.util.extractor;

import com.tourtle.web.domain.MobileProfile;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Apathetic spawn of Wesb on 3/5/17.
 */
public class ProfileExtractor implements ResultSetExtractor<MobileProfile> {

    @Override
    public MobileProfile extractData(ResultSet rs) throws SQLException, DataAccessException {
        rs.next();      // Move into the first position.  We are only expecting one thing here.
        MobileProfile mobileProfile = new MobileProfile();
        mobileProfile.setUsername(rs.getString("username"));
        return mobileProfile;
    }
}