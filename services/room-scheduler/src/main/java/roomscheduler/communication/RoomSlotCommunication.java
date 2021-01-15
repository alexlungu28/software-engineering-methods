package roomscheduler.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import roomscheduler.entities.Rule;

public class RoomSlotCommunication extends ServerCommunication {

    /** Placeholder.
     *
     * @param id Placeholder.
     * @return Placeholder.
     * @throws IOException Placeholder.
     */
    public static Object getRoom(int id) throws IOException {
        Gson gson = new Gson();
        String port = "8080"; //this is the port of the room-service
        String path = "/room/" + id;
        HttpGet req = new HttpGet("http://localhost:" + port + "/" + path);
        JsonObject jsonObject = gson.fromJson(getObjectJson(req), JsonObject.class);
        if (jsonObject.get("status") == null) {
            return "ok";
        } else {
            return "not found";
        }
    }
    
    /** Placeholder.
     */
    public static Long numberOfRooms() throws IOException {
        Gson gson = new Gson();
        String port = "8080"; //this is the port of the room-service
        String path = "/countRooms";
        HttpGet req = new HttpGet("http://localhost:" + port + "/" + path);
        Long count = gson.fromJson(getObjectJson(req), Long.class);
        return count;
    }

    /** Placeholder.
     */
    public static List<Rule> getRules() throws IOException {
        String port = "8081";
        String path = "/allRules";
        HttpGet req = new HttpGet("http://localhost:" + port + "/" + path);
        return stringToArray(getObjectJson(req), Rule[].class);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }
}
