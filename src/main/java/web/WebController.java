package web;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {

    @RequestMapping(value = "/tours", method = RequestMethod.GET)
    public
    @ResponseBody
    String getTours() {
        System.out.println("moki");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Adult");

        JSONArray beaconsArray = new JSONArray();
        beaconsArray.put(createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B57FE6D",
                49.270622, -123.13474100000002));
        beaconsArray.put(createJsonBeaconObject("B9407F30-F5F8-466E-AFF9-25556B5FIONA",
                49.270622, -123.13474100000002));
        jsonObject.put("beacons", beaconsArray);
        return jsonObject.toString();
    }

    private JSONObject createJsonBeaconObject(String uuid, double lat, double lon) {
        JSONObject obj = new JSONObject();
        obj.put("uuid", uuid);
        obj.put("lat", lat);
        obj.put("lon", lon);
        return obj;
    }
}