package com.tourtle.web.services;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ToursService {

    public JSONObject createJsonBeaconObject(String uuid, double lat, double lon) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", uuid);
        obj.put("lat", lat);
        obj.put("lon", lon);
        return obj;
    }
}
