package op29sem58.courses;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class ServerCommunication {
    private static final String STUDENT_SERVICE_URL = "http://localhost:8085";
    private static final String ROOM_SCHEDULE_SERVICE_URL = "http://localhost:8081";

    private static CredentialsProvider credentials;
    private static CloseableHttpClient client;

    /**
     * A standard method to make a get request with any url.
     *
     * @param url the url with which this request needs to be made
     * @return the body of the get request
     */
    public static String makeGetRequest(String url) {
        credentials = new BasicCredentialsProvider();
        client = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentials).build();

        try (CloseableHttpResponse response = client.execute(new HttpGet(url))) {
            String body = EntityUtils.toString(response.getEntity());
            client.close();
            return body;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getStudentServiceUrl() {
        return STUDENT_SERVICE_URL;
    }

    public static String getRoomScheduleServiceUrl() {
        return ROOM_SCHEDULE_SERVICE_URL;
    }
}
