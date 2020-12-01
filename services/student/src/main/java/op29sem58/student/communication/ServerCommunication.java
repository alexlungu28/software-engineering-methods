package op29sem58.student.communication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import op29sem58.student.database.entities.RoomSchedule;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import op29sem58.student.CourseLectures;
import op29sem58.student.communication.adapters.LocalDateTimeAdapter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerCommunication {
	private static final String COURSES_SERVICE_URL = "http://localhost:8085";
	private static final String ROOM_SCHEDULE_SERVICE_URL = "http://localhost:8081";

	private CredentialsProvider credentials = new BasicCredentialsProvider();
	private CloseableHttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(this.credentials).build();
	private Gson gson = new GsonBuilder()
		.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
		.create();

	private <T> T makeGetRequest(String url, Class<T> classOf) {
		try {
			CloseableHttpResponse response = this.client.execute(new HttpGet(url));
			String body = EntityUtils.toString(response.getEntity());
			T value = this.gson.fromJson(body, classOf);
			return value;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<CourseLectures> getAllLectures() {
		CourseLectures[] result = this.makeGetRequest(ServerCommunication.COURSES_SERVICE_URL + "/getAllLectures", CourseLectures[].class);
		if (result == null) return new ArrayList<CourseLectures>();

		return Arrays.asList(result);
	}

	public List<RoomSchedule> getSchedule() {
		RoomSchedule[] result = this.makeGetRequest(ServerCommunication.ROOM_SCHEDULE_SERVICE_URL + "/getSchedule", RoomSchedule[].class);
		if (result == null) return new ArrayList<RoomSchedule>();

		return Arrays.asList(result);
	}
}
