package com.tourtle.web.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Chris on 2017-03-19.
 */
@Data
public class WebProfile {
    private String username;
    @JsonProperty("Tours") private List<String> toursOwned;
    @JsonProperty("Beacons") private List<String> poiOwned;
}
