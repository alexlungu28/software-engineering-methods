package roomscheduler.communication;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;

public class RoomSlotCommunication extends ServerCommunication {

    //TODO
    public static Object getRoom(int id) throws IOException {
        String port = "8080"; //this is the port of the room-service
        String path = "/room/" + id;
        JsonObject jsonObject = gson.fromJson(getObjectJson(port, path), JsonObject.class);
        System.out.println(jsonObject);
        if(jsonObject.get("status") == null){
            return "ok";
        }else{
            return "not found";
        }
    }
}
