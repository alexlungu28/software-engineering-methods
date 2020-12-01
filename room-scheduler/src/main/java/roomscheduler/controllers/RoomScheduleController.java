package roomscheduler.controllers;

import roomscheduler.communication.RoomScheduleCommunication;
import roomscheduler.entities.*;
import roomscheduler.repositories.RoomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

/**
 * Controller for Room Schedule.
 * With methods for general and id-specific retrieval
 */
@Controller // This means that this class is a Controller
public class RoomScheduleController {
    @Autowired
    private RoomScheduleRepository roomScheduleRepository;

    public RoomScheduleRepository getRoomScheduleRepository() {
        return roomScheduleRepository;
    }

    public void setRoomScheduleRepository(RoomScheduleRepository roomScheduleRepository) {
        this.roomScheduleRepository = roomScheduleRepository;
    }

    @PostMapping(path = "/addroomschedule") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRoomSchedule(@RequestBody RoomSchedule r) {
        roomScheduleRepository.saveAndFlush(r);
        return "Saved room schedule";
    }


    @GetMapping(path = "/availableSlots/{prefDate}/{numSlots}/{lunchTime}")
    public @ResponseBody
    Iterable<RoomSlotStat> getAvailableSlots(@PathVariable Date prefDate,
                                       @PathVariable Integer numSlots,
                                       @PathVariable Time lunchTime) {
        return roomScheduleRepository.scheduleLecture(prefDate, numSlots, lunchTime);
    }

    @GetMapping(path = "/roomSlotsAfterGivenDate/{prefDate}/{numSlots}/{numOfStudents}")
    public @ResponseBody
    List<DateIntPair> scheduleNewLecture(@PathVariable Date prefDate,
                                    @PathVariable Integer numSlots,
                                    @PathVariable Integer numOfStudents) throws IOException, ParseException {
        Integer lunchHour = 9 + roomScheduleRepository.getLunchSlot();
        Time lunchTime = UTCTime("" + lunchHour  + ":45:00");
        int minPercentage = roomScheduleRepository.getMinPerc();
        int maxPercentage = roomScheduleRepository.getMaxPerc();
        List<Integer> roomIdsWithRequiredCapacity =
                RoomScheduleCommunication.getRoomsIdsWithRequiredCapacity(numOfStudents, minPercentage, maxPercentage);
        //System.out.println(roomIdsWithRequiredCapacity);
        List<DateIntPair> dateIntPairs = RoomScheduleCommunication.
                getAvailableRoomsSlots(prefDate,numSlots,lunchTime);
        //System.out.println(dateIntPairs);

        List<DateIntPair> filtered = new ArrayList<>();
        for(DateIntPair pair : dateIntPairs){
            if(roomIdsWithRequiredCapacity.contains(pair.getRoomId())) filtered.add(pair);
        }
        //System.out.println(filtered);
        return filtered;
    }

    public static Time UTCTime(String s) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final java.util.Date dateObj = sdf.parse(s);
        Time result = Time.valueOf(dateObj.toInstant().toString().substring(11,19));
        System.out.println(result);
        return result;
    }





    /**
     * Retrieve all Rooms Schedules.
     *
     * @return all Rooms Schedules stored in the database
     */
    @GetMapping(path = "/allroomsschedules")
    public @ResponseBody
    Iterable<RoomSchedule> getAllRoomsSchedules() {
        return roomScheduleRepository.findAll();
    }

    /**
     * Method for getting a Room Schedule.
     *
     * @param id id of the RoomSchedule that is to be retrieved
     * @return RoomSchedule with id 'id'
     */
    @GetMapping(path = "/roomschedule/{id}")
    public @ResponseBody
    RoomSchedule getRoomSchedule(@PathVariable int id) {
        Optional<RoomSchedule> roomSchedule = roomScheduleRepository.findById(id);
        if (roomSchedule.isPresent()) {
            return roomSchedule.get();
        } else {
            throw new RuntimeException("Room Schedule not found for the id " + id);
        }
    }

}


