package com.tourtle.web.Domain;

import lombok.Data;
import java.util.List;

@Data
public class Tour {

    String tourId;
    String name;
    List<POI> pois;

    public Tour(String name, List<POI> pois) {
        this.name = name;
        this.pois = pois;
    }

    public Tour() {}

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<POI> getpois() {
        return pois;
    }

    public void setPois(List<POI> pois) {
        this.pois = pois;
    }

    public void addPoi(POI poi) {
        pois.add(poi);
    }
}
