package web;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Apathetic spawn of Wesb on 11/11/16.
 */
public class DatabaseController {

    public DatabaseController() {

    }

//    private void openConnection() {
//        System.out.println(System.getenv("CLEARDB_DATABASE_URL"));
//        try {
//            URI dbUri = new URI(System.getenv("CLEARDB_DATABASE_URL"));
//            String username = dbUri.getUserInfo().split(":")[0];
//            String password = dbUri.getUserInfo().split(":")[1];
//            String dbUrl = "jdbc:mysql://" + dbUri.getHost() + dbUri.getPath();
//            connection = DriverManager.getConnection(dbUrl, username, password);
//        } catch (Exception e) {
//            System.out.println("Issue");
//            e.printStackTrace();
//        }
//    }
//
//    private void closeConnection() {
//        try {
//            connection.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public String read(byte[] byteArray) {

        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(byteArray);

//        ByteArrayEntity byteArrayEntity = new ByteArrayEntity(byteArray);

        HttpClient httpclient = HttpClients.createDefault();

        try {
            URIBuilder builder = new URIBuilder("https://api.projectoxford.ai/vision/v1.0/ocr");

            builder.setParameter("language", "unk");
            builder.setParameter("detectOrientation ", "true");

            URI uri = builder.build();
            HttpPost request = new HttpPost(uri);
//            request.setHeader("Content-Type", "application/json");
            request.setHeader("Content-Type", "application/octet-stream");
            request.setHeader("Ocp-Apim-Subscription-Key", "ac96f336d1a44758b4d277a43f791de5");

            File file = new File("ld.jpg");
            byte[] fileData = new byte[(int) file.length()];
            FileInputStream in = new FileInputStream(file);
            in.read(fileData);
            in.close();
            System.out.println(fileData);

            ByteArrayEntity reqEntity = new ByteArrayEntity(fileData);
            request.setEntity(reqEntity);

//            request.setEntity(byteArrayEntity);

            HttpResponse response = httpclient.execute(request);
            HttpEntity entity = response.getEntity();
            if (entity != null)
            {
                System.out.println(EntityUtils.toString(entity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }
}
