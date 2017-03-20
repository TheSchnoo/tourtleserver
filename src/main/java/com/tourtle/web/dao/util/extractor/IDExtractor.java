package com.tourtle.web.dao.util.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts lists of ids, as long as they're strings.
 */
public class IDExtractor implements ResultSetExtractor<List<String>> {
    @Override
    public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<String> stringList = new ArrayList<>();

        while (rs.next()) {
            String id = rs.getString("id");
            stringList.add(id);
        }

        return stringList;
    }
}
