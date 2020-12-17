package op29sem58.student.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import op29sem58.student.communication.adapters.LocalDateTimeAdapter;
import op29sem58.student.database.entities.RoomSchedule;
import op29sem58.student.local.entities.Course;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * This is meant for all our communication to other services.
 * We start with defining the other services URL's, setting
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

    private <T> T makeGetRequest(String url, Class<T> classOf, String token) {
        HttpGet request = new HttpGet((url));
        request.addHeader("Authorization", token);
        try (
            CloseableHttpResponse response = ServerCommunication.client.execute(request)
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
     * Uses the `/getAllCourses` endpoint from the Courses service to get all the courses with their
     * lectures.
     *
     * @return List of course lectures.
     */
    public List<Course> getAllCourses(String token) {
        String url = ServerCommunication.COURSES_SERVICE_URL + "/getAllCourses";
        Course[] result = this.makeGetRequest(url, Course[].class, token);
        if (result == null) {
            return new ArrayList<Course>();
        }
        return Arrays.asList(result);
    }

    /**
     * Uses the `/getUsername` endpoint from the Authentication service
     * to get the username.
     *
     * @return the username
     */
    public String getUserId(String token) {
        String url = ServerCommunication.COURSES_SERVICE_URL + "/getUsername";
        String result = this.makeGetRequest(url, String.class, token);
        if (result == null) {
            return "no user";
        }

        return result;
    }

    /**
     * Uses the `/getSchedule` endpoint from the Room Schedule service to get the entire schedule.
     *
     * @return List of scheduled rooms.
     */
    public List<RoomSchedule> getSchedule(String token) {
        String url = ServerCommunication.ROOM_SCHEDULE_SERVICE_URL + "/getSchedule";
        RoomSchedule[] result = this.makeGetRequest(url, RoomSchedule[].class, token);
        if (result == null) {
            return new ArrayList<RoomSchedule>();
        }

        return Arrays.asList(result);
    }


}
