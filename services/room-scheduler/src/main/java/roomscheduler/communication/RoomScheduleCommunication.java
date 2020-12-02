package roomscheduler.communication;

import com.google.gson.Gson;
import roomscheduler.entities.SlotInfo;
import roomscheduler.entities.IdNamePair;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;

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

    public static void makeRoomSlotAvailable(Integer roomSlotId) throws IOException {
        Integer statusCode = executePostRequest("8081", "/makeRoomSlotAvailable/" + roomSlotId, "");
    }

    public static Integer getCoronaCapacity(Integer roomId, Integer minPerc,
                                            Integer maxPerc) throws IOException {

        String port = "8080"; //this is the port of the room-service
        String path = "/getCoronaCapacity/" + roomId + "/" + minPerc + "/" + maxPerc;
        Integer count = gson.fromJson(getObjectJson(port, path), Integer.class);
        return count;
    }



}
