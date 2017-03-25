package com.tourtle.web.services;

import com.tourtle.web.dao.TourDao;
import com.tourtle.web.domain.Tour;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToursService {

    @Autowired
    TourDao tourDao;

    public Tour getTourById(String tourId) {
        if (tourDao.checkTourExistsById(tourId)) {
            return tourDao.getTourById(tourId);
        } else {
            throw new DataRetrievalFailureException("Couldn't find resource");
        }
    }

    public Tour getTourByName(String tourName) {
        if (tourDao.checkTourExistsByName(tourName)) {
            return tourDao.getTourByName(tourName);
        } else {
            throw new DataRetrievalFailureException("Couldn't find resource");
        }
    }

    public List<Tour> getAllTours() {
        return tourDao.getAllTours();
    }

    public int createTour(String tourId, String body) throws DuplicateKeyException {
        return tourDao.createTour(tourId, body);
    }

    public int deleteTour(String tourId) {
        return tourDao.deleteTour(tourId);
    }

    public int deleteFromTours(String tourId, String body) {
        return tourDao.deleteFromTours(tourId, body);
    }

    public int postTour(String tourId, String body) {
        return tourDao.postTour(tourId, body);
    }
}
