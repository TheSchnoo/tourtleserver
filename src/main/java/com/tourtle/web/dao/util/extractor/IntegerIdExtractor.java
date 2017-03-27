package com.tourtle.web.dao.util.extractor;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Extracts lists of ids, as long as they're integers
 */
public class IntegerIdExtractor implements ResultSetExtractor<List<Integer>> {
    @Override
    public List<Integer> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Integer> integerList = new ArrayList<>();

        while (rs.next()) {
            int id = rs.getInt("id");
            integerList.add(id);
        }

        return integerList;
    }
}
