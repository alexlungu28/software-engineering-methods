package roomscheduler.communication;

import com.google.gson.Gson;
import roomscheduler.entities.DateIntPair;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

public class RoomScheduleCommunication extends ServerCommunication {

    public static List<Integer> getRoomsIdsWithRequiredCapacity(Integer numOfStudents, Integer minPer, Integer maxPer) throws IOException {
        String port = "8080";
        String path = "/getRoomsWithCapacityAtLeast/" + numOfStudents + "/" + minPer + "/" + maxPer;
        return stringToArray(getObjectJson(port, path), Integer[].class);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    public static List<DateIntPair>
    getAvailableRoomsSlots(Date prefDate, Integer numOfSlots, Time lunchTime) throws IOException {
        String objectJson = getObjectJson("8081", "/availableSlots/" + prefDate + "/" + numOfSlots
                + "/" + lunchTime);
        System.out.println(objectJson);
        return stringToArray(objectJson, DateIntPair[].class);
    }
}
