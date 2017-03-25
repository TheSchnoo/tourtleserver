package com.tourtle.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.domainviews.Views;
import lombok.Data;
import java.util.List;

@Data
public class Tour {

    @JsonView(Views.ResponseData.class) String tourId;
    @JsonView(Views.ResponseData.class) String name;
    @JsonProperty("beacons") List<POI> pois;
    @JsonView(Views.ResponseData.class) String imageurl;
}
