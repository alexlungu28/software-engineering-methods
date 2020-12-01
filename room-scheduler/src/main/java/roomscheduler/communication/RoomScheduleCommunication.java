package roomscheduler.communication;

import com.google.gson.Gson;
import roomscheduler.entities.RoomSlot;
import roomscheduler.entities.SlotInfo;
import roomscheduler.entities.IdNamePair;
import roomscheduler.repositories.RoomSlotRepository;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class RoomScheduleCommunication extends ServerCommunication {

    public static List<IdNamePair> getRoomsIdsWithRequiredCapacity(Integer numOfStudents, Integer minPer, Integer maxPer) throws IOException {
        String port = "8080";
        String path = "/getRoomsWithCapacityAtLeast/" + numOfStudents + "/" + minPer + "/" + maxPer;
        return stringToArray(getObjectJson(port, path), IdNamePair[].class);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    public static List<SlotInfo>
    getAvailableRoomsSlots(Date prefDate, Integer numOfSlots, Time lunchTime) throws IOException {
        String objectJson = getObjectJson("8081", "/availableSlots/" + prefDate + "/" + numOfSlots
                + "/" + lunchTime);
        return stringToArray(objectJson, SlotInfo[].class);
    }

    public static void makeRoomSlotsOccupied(Integer roomSlotId, Integer numberOfSlots) throws IOException {
        Integer statusCode = executePostRequest("8081", "/updateRoomSlot/" + roomSlotId + "/" + numberOfSlots, "");
    }

}
