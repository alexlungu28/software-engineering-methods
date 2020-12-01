package op29sem58.courses;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.time.LocalDateTime;

public class ServerCommunication {
    private static final String STUDENT_SERVICE_URL = "http://localhost:8085";
    private static final String ROOM_SCHEDULE_SERVICE_URL = "http://localhost:8081";

    private static CredentialsProvider credentials = new BasicCredentialsProvider();
    private static CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(credentials).build();

    public static String makeGetRequest(String url) {
        try {
            CloseableHttpResponse response = client.execute(new HttpGet(url));
            String body = EntityUtils.toString(response.getEntity());
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
