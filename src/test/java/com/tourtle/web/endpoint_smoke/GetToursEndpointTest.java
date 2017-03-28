package com.tourtle.web.endpoint_smoke;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetToursEndpointTest {

    private final static String BASE_URL = "https://tourtle-app.herokuapp.com/tours";

    @Test
    public void toursEndpointReturnsTours() throws IOException, JSONException {

        HttpUriRequest request = new HttpGet( BASE_URL);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        String responseString = EntityUtils.toString(httpResponse.getEntity());
        JSONArray responseJsonArray = new JSONArray(responseString);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(responseJsonArray.length()).isGreaterThan(0);

        JSONObject firstTourJson = (JSONObject) responseJsonArray.get(0);
        assertThat(firstTourJson.get("tourId")).isNotNull().isInstanceOf(String.class);
        assertThat(firstTourJson.get("imageurl")).isNotNull().isInstanceOf(String.class);
        assertThat(firstTourJson.get("name")).isNotNull().isInstanceOf(String.class);
        assertThat(firstTourJson.get("beacons")).isNotNull().isInstanceOf(JSONArray.class);
    }

    @Test
    public void toursEndpointReturnsTourForValidTourId() throws IOException, JSONException {

        HttpUriRequest request = new HttpGet( BASE_URL + "/2");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        String responseString = EntityUtils.toString(httpResponse.getEntity());
        JSONObject responseJson = new JSONObject(responseString);

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        assertThat(responseJson.get("tourId")).isNotNull().isInstanceOf(String.class);
        assertThat(responseJson.get("imageurl")).isNotNull().isInstanceOf(String.class);
        assertThat(responseJson.get("name")).isNotNull().isInstanceOf(String.class);
        assertThat(responseJson.get("beacons")).isNotNull().isInstanceOf(JSONArray.class);
    }

    @Test
    public void toursEndpointGetInvalidTourIdFails() throws IOException, JSONException {

        HttpUriRequest request = new HttpGet( BASE_URL + "/empty");

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        String responseString = EntityUtils.toString(httpResponse.getEntity());

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(responseString).isEqualTo("Resource not found");
    }
}
