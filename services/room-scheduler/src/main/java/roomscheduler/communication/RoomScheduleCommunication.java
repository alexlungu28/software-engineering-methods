package roomscheduler.communication;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.StringEntity;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.SlotInfo;

public class RoomScheduleCommunication extends ServerCommunication {

    private static String lh = "http://localhost:";

    /** Placeholder.
     *
     * @param numOfStudents Placeholder
     * @param minPer Placeholder
     * @param maxPer Placeholder
     * @return Placeholder
     * @throws IOException Placeholder.
     */
    public static List<IdNamePair> getRoomsIdsWithRequiredCapacity(
        Integer numOfStudents, Integer minPer, Integer maxPer
    ) throws IOException {
        String port = "8080";
        String path = "/getRoomsWithCapacityAtLeast/" + numOfStudents + "/" + minPer + "/" + maxPer;
        HttpGet req = new HttpGet(lh + port + "/" + path);
        return stringToArray(getObjectJson(req), IdNamePair[].class);
    }

    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr);
    }

    /**
     * Placeholder.
     *
     * @param prefDate Placeholder
     * @param numOfSlots Placeholder
     * @param lunchTime Placeholder
     * @return Placeholder.
     * @throws IOException Placeholder.
     */
    public static List<SlotInfo> getAvailableRoomsSlots(
        Date prefDate, Integer numOfSlots, Time lunchTime
    ) throws IOException {
        String port = "8081";
        String path = "/availableSlots/" + prefDate + "/" + numOfSlots
                + "/" + lunchTime;
        HttpGet req = new HttpGet(lh + port + "/" + path);
        String objectJson = getObjectJson(req);
        return stringToArray(objectJson, SlotInfo[].class);
    }

    /**
     * Placeholder.
     *
     * @param roomSlotId Placeholder
     * @param numberOfSlots Placeholder
     * @throws IOException Placeholder
     */
    public static void makeRoomSlotsOccupied(
        Integer roomSlotId, Integer numberOfSlots
    ) throws IOException {
        String port = "8081";
        String path = lh + roomSlotId + "/" + numberOfSlots;
        StringEntity se = new StringEntity("");
        try {
            URI req = new URI(lh + port + "/" + path);
            executePostRequest(req, se);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Make room slot available again.
     *
     * @param roomSlotId The number of the roomslot
     * @throws IOException IoException
     */
    public static void makeRoomSlotAvailable(Integer roomSlotId) throws IOException {
        StringEntity se = new StringEntity("");
        try {
            URI req = new URI(lh + "8081" + "/" +  "/makeRoomSlotAvailable/" + roomSlotId);
            executePostRequest(req, se);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Placeholder.
     *
     * @param roomId Placeholder
     * @param minPerc Placeholder
     * @param maxPerc Placeholder
     * @return Placeholder
     * @throws IOException Placeholder
     */
    public static Integer getCoronaCapacity(Integer roomId, Integer minPerc,
                                            Integer maxPerc) throws IOException {
        Gson gson = new Gson();
        String port = "8080"; //this is the port of the room-service
        String path = "/getCoronaCapacity/" + roomId + "/" + minPerc + "/" + maxPerc;
        HttpGet req = new HttpGet(lh + port + "/" + path);
        Integer count = gson.fromJson(getObjectJson(req), Integer.class);
        return count;
    }
}
