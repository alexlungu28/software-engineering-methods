package roomscheduler.communication;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import roomscheduler.entities.Rule;

public class RoomSlotCommunication extends ServerCommunication {

    /** Placeholder.
     *
     * @param id Placeholder.
     * @return Placeholder.
     * @throws IOException Placeholder.
     */
    public static Object getRoom(int id) throws IOException {
        String port = "8080"; //this is the port of the room-service
        String path = "/room/" + id;
        JsonObject jsonObject = gson.fromJson(getObjectJson(port, path), JsonObject.class);
        if (jsonObject.get("status") == null) {
            return "ok";
        } else {
            return "not found";
        }
    }
    
    /** Placeholder.
     */
    public static Long numberOfRooms() throws IOException {
        String port = "8080"; //this is the port of the room-service
        String path = "/countRooms";
        Long count = gson.fromJson(getObjectJson(port, path), Long.class);
        return count;
    }

    /** Placeholder.
     */
    public static List<Rule> getRules() throws IOException {
        String port = "8081";
        String path = "/allRules";
        return stringToArray(getObjectJson(port, path), Rule[].class);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }
}
