package com.tourtle.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.domainviews.Views;
import lombok.Data;
import java.util.List;

@Data
public class Tour {

    @JsonView(Views.ResponseData.class) String tourId;
    @JsonView(Views.ResponseData.class) String name;
    List<POI> pois;

}
