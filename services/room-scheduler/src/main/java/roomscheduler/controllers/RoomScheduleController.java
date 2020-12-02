package roomscheduler.controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import roomscheduler.communication.RoomScheduleCommunication;
import roomscheduler.entities.*;
import roomscheduler.repositories.RoomScheduleRepository;
import roomscheduler.repositories.RoomSlotRepository;



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

    @GetMapping(path = "/getSchedule")
    public @ResponseBody
    List<ScheduleInformation> getAllSchedules() throws IOException {
        Integer breakDuration = roomScheduleRepository.getBreakDuration();
        Integer slotDuration = roomScheduleRepository.getSlotDuration();
        List<ScheduleInfo> result = roomScheduleRepository
                .getScheduleInfo(slotDuration, breakDuration);
        System.out.println(result);
        List<ScheduleInformation> finalRes = new ArrayList<>();
        for (ScheduleInfo s : result) {
            finalRes.add(new ScheduleInformation(s.getRoomScheduleId(), s.getStartTime(),
                    s.getEndTime(), s.getLectureId(), s.getRoomId(), RoomScheduleCommunication
                    .getCoronaCapacity(s.getRoomId(), roomScheduleRepository.getMinPerc(),
                            roomScheduleRepository.getMaxPerc())));
        }
        System.out.println(finalRes);
        return finalRes;
    }


    @GetMapping(path = "/availableSlots/{prefDate}/{numSlots}/{lunchTime}")
    public @ResponseBody
    Iterable<RoomSlotStat> getAvailableSlots(@PathVariable Date prefDate,
                                       @PathVariable Integer numSlots,
                                       @PathVariable Time lunchTime) {
        return roomScheduleRepository.scheduleLecture(prefDate, numSlots, lunchTime);
    }

    @GetMapping(path = "/scheduleLecture/{prefDate}/"
            + "{numSlots}/{numOfStudents}/{lectureId}/{yearOfStudy}")
    public @ResponseBody
    String scheduleNewLecture(@PathVariable Date prefDate,
                                    @PathVariable Integer numSlots,
                                    @PathVariable Integer numOfStudents,
                              @PathVariable Integer lectureId,
                              @PathVariable Integer yearOfStudy) throws IOException, ParseException {
        Integer lunchHour = 9 + roomScheduleRepository.getLunchSlot();
        Time lunchTime = UTCTime("" + lunchHour  + ":45:00");
        int minPercentage = roomScheduleRepository.getMinPerc();
        int maxPercentage = roomScheduleRepository.getMaxPerc();
        List<IdNamePair> roomInfoWithRequiredCapacity =
                RoomScheduleCommunication.getRoomsIdsWithRequiredCapacity(numOfStudents, minPercentage, maxPercentage);
        //System.out.println(roomInfoWithRequiredCapacity);
        //System.out.println(roomIdsWithRequiredCapacity);
        List<SlotInfo> dateIntPairs = RoomScheduleCommunication
                .getAvailableRoomsSlots(prefDate,numSlots,lunchTime);
        System.out.println(dateIntPairs);

        //TODO do not let lectures of the same year overlap
        List<Integer> slotIdsOfSameYearLectures = roomScheduleRepository.getSlotIdsForLecturesOfTheSameYear(yearOfStudy);
        System.out.println(slotIdsOfSameYearLectures);

        List<NameDateInfo> finalResult = new ArrayList<>();
        for (SlotInfo pair : dateIntPairs){
            for (IdNamePair i : roomInfoWithRequiredCapacity){
                if (i.getId() == pair.getRoomId() && !slotIdsOfSameYearLectures.contains(pair.roomSlotId)) finalResult.
                        add(new NameDateInfo(pair.getDate(), i.getName(), pair.getRoomSlotId()));
            }
        }
        if(finalResult.size() == 0){ //no available slots for the input given
            return "There are no available slots for the input given. Try again!";
        }else{
            NameDateInfo result = finalResult.get(0);
            roomScheduleRepository.saveAndFlush(new RoomSchedule(
                    result.getRoomSlotId(), lectureId, yearOfStudy));
            for(int i = 0; i < numSlots; i++){
                roomScheduleRepository.saveAndFlush(new RoomSchedule(result.getRoomSlotId() + (i + 1)*20, lectureId, yearOfStudy));
            }
            RoomScheduleCommunication.makeRoomSlotsOccupied(result.getRoomSlotId(), numSlots);
            return "Your lecture was successfully scheduled for: " + result.getDate().toString() +
                    " in the room with name: " + result.getRoomName();
        }
    }

    public static Time UTCTime(String s) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final java.util.Date dateObj = sdf.parse(s);
        Time result = Time.valueOf(dateObj.toInstant().toString().substring(11,19));
        return result;
    }


    @DeleteMapping(path = "/cancelLecture/{id}")
    public @ResponseBody
    String deleteLecture(@PathVariable int id) throws IOException {
        List<IntPair> values = roomScheduleRepository.getSlotIdAndRoomSlotId(id);
        if(values.size() == 0){
            return "There is no scheduled lecture with the given id";
        }else{
            for(IntPair p : values){
                roomScheduleRepository.deleteById(p.getRoomScheduleId());
                RoomScheduleCommunication.makeRoomSlotAvailable(p.getRoomSlotId());
            }
        }
        return "Canceled lecture";
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


