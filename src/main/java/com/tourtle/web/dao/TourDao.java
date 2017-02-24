package com.tourtle.web.dao;

import com.tourtle.web.domain.Tour;

import java.util.List;

/**
 * DAO for Tour
 */
public interface TourDao {

    boolean checkTourExistsById(String tourId);

    boolean checkTourExistsByName(String tourName);

    Tour getTourById(String tourId);

    Tour getTourByName(String tourName);

    List<Tour> getAllTours();
}
