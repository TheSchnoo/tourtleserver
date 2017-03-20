package com.tourtle.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.domainviews.Views;
import lombok.Data;

@Data
public class POI {
    @JsonView(Views.ResponseData.class) @JsonProperty("uuid") String beaconId;
    @JsonView(Views.ResponseData.class) String name;
    @JsonView(Views.ResponseData.class) Double lat;
    @JsonView(Views.ResponseData.class) Double lon;
    @JsonView(Views.ResponseData.class) String description;
    @JsonView(Views.ResponseData.class) String imageurl;
}
