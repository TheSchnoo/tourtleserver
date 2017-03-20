package com.tourtle.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.domainviews.Views;
import com.tourtle.web.domain.Tour;
import lombok.Data;

import java.util.List;

@Data
public class Profile {
    @JsonView(Views.ResponseData.class) String username;
    @JsonProperty("ToursCompleted") @JsonView(Views.ResponseData.class) List<String> toursCompleted;
    @JsonProperty("PoiVisited") @JsonView(Views.ResponseData.class) List<String> poiVisited;
}
