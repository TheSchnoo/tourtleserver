package com.tourtle.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.tourtle.web.domain.domainviews.Views;
import lombok.Data;

import java.util.List;

@Data
public class Profile {

    @JsonView(Views.ResponseData.class) String username;
    @JsonView(Views.ResponseData.class) List<Tour> toursCompleted;
}
