package com.tourtle.web.endpoint_smoke;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EditToursEndpointTest {

    private final static String BASE_URL = "https://tourtle-app.herokuapp.com/tours";
    private final static String ID_SUFFIX = "/999";

    @Test
    public void editTourSmokeTest() throws IOException, JSONException {

        JSONObject body = new JSONObject();
        body.put("name", "Godzillas Tokyo Food Tour");
        body.put("owner", "fiona");
        body.put("imageurl", "example.com");
        HttpEntity entity = new StringEntity(body.toString());

        HttpPut request = new HttpPut(BASE_URL + ID_SUFFIX);
        request.setEntity(entity);

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute( request );

        String responseString = EntityUtils.toString(httpResponse.getEntity());

        assertThat(httpResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(responseString).isEqualTo("1");

        // Test that the tour made it to the database

        HttpUriRequest getRequest = new HttpGet( BASE_URL + ID_SUFFIX);

        HttpResponse getResponse = HttpClientBuilder.create().build().execute( getRequest );

        String getResponseString = EntityUtils.toString(getResponse.getEntity());
        JSONObject responseJson = new JSONObject(getResponseString);

        assertThat(getResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        assertThat(responseJson.get("tourId")).isEqualTo("999");
        assertThat(responseJson.get("imageurl")).isEqualTo("example.com");
        assertThat(responseJson.get("name")).isEqualTo("Godzillas Tokyo Food Tour");
        assertThat(responseJson.get("beacons")).isNotNull().isInstanceOf(JSONArray.class);

        // Edit the tour name
        JSONObject postBody = new JSONObject();
        body.put("name", "Godzilla snack time");
        HttpEntity postEntity = new StringEntity(body.toString());

        HttpPost postRequest = new HttpPost(BASE_URL + ID_SUFFIX);
        postRequest.setEntity(postEntity);

        HttpResponse postResponse = HttpClientBuilder.create().build().execute( postRequest );

        String postResponseString = EntityUtils.toString(postResponse.getEntity());

        assertThat(postResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_ACCEPTED);
        assertThat(postResponseString).isEqualTo("Rows Affected: 1");

        // And check database

        HttpResponse putGetResponse = HttpClientBuilder.create().build().execute( getRequest );

        String putGetResponseString = EntityUtils.toString(putGetResponse.getEntity());
        JSONObject putGetResponseJson = new JSONObject(putGetResponseString);

        assertThat(putGetResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);

        assertThat(putGetResponseJson.get("tourId")).isEqualTo("999");
        assertThat(putGetResponseJson.get("imageurl")).isEqualTo("example.com");
        assertThat(putGetResponseJson.get("name")).isEqualTo("Godzilla snack time");
        assertThat(putGetResponseJson.get("beacons")).isNotNull().isInstanceOf(JSONArray.class);

        // Delete the tour
        HttpUriRequest deleteRequest = new HttpDelete( BASE_URL + ID_SUFFIX);

        HttpResponse deleteResponse = HttpClientBuilder.create().build().execute( deleteRequest );

        String deleteResponseString = EntityUtils.toString(deleteResponse.getEntity());

        assertThat(deleteResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(deleteResponseString).isEqualTo("Rows Affected: 1");

        HttpResponse finalGetResponse = HttpClientBuilder.create().build().execute( getRequest );

        String finalResponseString = EntityUtils.toString(finalGetResponse.getEntity());

        assertThat(finalGetResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_NOT_FOUND);
        assertThat(finalResponseString).isEqualTo("Resource not found");

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
