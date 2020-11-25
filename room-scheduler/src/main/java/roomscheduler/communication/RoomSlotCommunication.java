package roomscheduler.communication;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class RoomSlotCommunication extends ServerCommunication {

    //TODO
    public static Object getRoom(int id) throws IOException {
        String port = "8080"; //this is the port of the room-service
        String path = "/room/" + id;
        Object result = gson.fromJson(getObjectJson(port, path), Object.class);
        System.out.println(result);
        return result;
    }
}
