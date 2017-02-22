package com.tourtle.web.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ToursService {

    public JSONObject createJsonBeaconObject(String uuid, double lat, double lon,
                                             String description, String imageUrl) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", uuid);
        obj.put("lat", lat);
        obj.put("lon", lon);
        obj.put("description", description);
        obj.put("imageUrl", imageUrl);
        return obj;
    }
}
