package op29sem58.student.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import op29sem58.student.local.entities.CourseLectures;
import op29sem58.student.communication.adapters.LocalDateTimeAdapter;
import op29sem58.student.database.entities.RoomSchedule;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * This is meant for all our communication to other services. We start with defining the other services URL's, setting
 * up the Gson builder so that we can serialize our requests.
 */
public class ServerCommunication {
    private static final String COURSES_SERVICE_URL = "http://localhost:8085";
    private static final String ROOM_SCHEDULE_SERVICE_URL = "http://localhost:8081";
    private static final CredentialsProvider CREDENTIALS = new BasicCredentialsProvider();
    private static final CloseableHttpClient client = HttpClientBuilder.create()
        .setDefaultCredentialsProvider(ServerCommunication.CREDENTIALS).build();
    private static final Gson gson = new GsonBuilder()
        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
        .create();

    private <T> T makeGetRequest(String url, Class<T> classOf) {
        try (
            CloseableHttpResponse response = ServerCommunication.client.execute(new HttpGet(url))
        ) {
            String body = EntityUtils.toString(response.getEntity());
            response.close();
            T value = ServerCommunication.gson.fromJson(body, classOf);
            return value;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Uses the `/getAllLectures` endpoint from the Courses service to get all lectures.
     *
     * @return List of course lectures.
     */
    public List<CourseLectures> getAllLectures() {
        String url = ServerCommunication.COURSES_SERVICE_URL + "/getAllLectures";
        CourseLectures[] result = this.makeGetRequest(url, CourseLectures[].class);
        if (result == null) {
            return new ArrayList<CourseLectures>();
        }

        return Arrays.asList(result);
    }

    /**
     * Uses the `/getSchedule` endpoint from the Room Schedule service to get the entire schedule.
     *
     * @return List of scheduled rooms.
     */
    public List<RoomSchedule> getSchedule() {
        String url = ServerCommunication.ROOM_SCHEDULE_SERVICE_URL + "/getSchedule";
        RoomSchedule[] result = this.makeGetRequest(url, RoomSchedule[].class);
        if (result == null) {
            return new ArrayList<RoomSchedule>();
        }

        return Arrays.asList(result);
    }
}
