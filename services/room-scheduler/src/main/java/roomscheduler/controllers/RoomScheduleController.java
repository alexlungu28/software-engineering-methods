package roomscheduler.controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import roomscheduler.communication.RoomScheduleCommunication;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.IntPair;
import roomscheduler.entities.NameDateInfo;
import roomscheduler.entities.RoomSchedule;
import roomscheduler.entities.RoomSlotStat;
import roomscheduler.entities.ScheduleInfo;
import roomscheduler.entities.ScheduleInformation;
import roomscheduler.entities.SlotInfo;
import roomscheduler.repositories.RoomScheduleRepository;




/**
 * Controller for Room Schedule.
 * With methods for general and id-specific retrieval
 */
@Controller // This means that this class is a Controller
public class RoomScheduleController {
    @Autowired
    private RoomScheduleRepository roomScheduleRepository;

    final transient String authHeader = "Authorization";

    transient String errorMessage = "You do not have the privilege to perform this action.";

    transient String student = "Student";

    public RoomScheduleRepository getRoomScheduleRepository() {
        return roomScheduleRepository;
    }

    public void setRoomScheduleRepository(RoomScheduleRepository roomScheduleRepository) {
        this.roomScheduleRepository = roomScheduleRepository;
    }

    /**
     * Add a new room schedule.
     *
     * @param token jwt token
     * @param r room schedule
     * @return saved if successful, throw exception otherwise
     */
    @PostMapping(path = "/addroomschedule") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRoomSchedule(@RequestHeader(authHeader) String token,
                              @RequestBody RoomSchedule r) {
        if (Authorization.authorize(token, "Admin")) {
            roomScheduleRepository.saveAndFlush(r);
            return "Saved room schedule";
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Method for getting all room schedules.

     *
     *  @return list of schedule information
     * @throws IOException exception
     */
    @GetMapping(path = "/getSchedule")
    public @ResponseBody
    List<ScheduleInformation> getAllSchedules()
        throws IOException {
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

    /**
     * Get the available slots.
     *
     * @param prefDate pref date
     * @param numSlots number of slots
     * @param lunchTime time of lunch
     * @return a list of available slots
     */
    @GetMapping(path = "/availableSlots/{prefDate}/{numSlots}/{lunchTime}")
    public @ResponseBody
    Iterable<RoomSlotStat> getAvailableSlots(@PathVariable Date prefDate,
                                       @PathVariable Integer numSlots,
                                       @PathVariable Time lunchTime) {
        return roomScheduleRepository.availableSlots(prefDate, numSlots, lunchTime);
    }

    /**
     * Method for scheduling a lecture.


     *  @param prefDate preferred Date
     * @param numSlots number of slots
     * @param numOfStudents number of students
     * @param lectureId lecture id
     * @param yearOfStudy year of study
     * @return message
     * @throws IOException ioException
     * @throws ParseException parseException
     */
    @GetMapping(path = "/scheduleLecture/{prefDate}/"
            + "{numSlots}/{numOfStudents}/{lectureId}/{yearOfStudy}")
    @SuppressWarnings("PMD")
    public @ResponseBody
    String scheduleNewLecture(@PathVariable Date prefDate,
                                    @PathVariable Integer numSlots,
                                    @PathVariable Integer numOfStudents,
                              @PathVariable Integer lectureId,
                              @PathVariable Integer yearOfStudy)
        throws IOException, ParseException {
        List<RoomSchedule> roomScheduleWithGivenLectureId = roomScheduleRepository
                .getRoomScheduleWithLectureId(lectureId);
        //it should not be possible to schedule a lecture that is already scheduled
        if (!roomScheduleWithGivenLectureId.isEmpty()) {
            throw new RuntimeException("The lecture with id " + lectureId + " has" +
                    " been already scheduled!");
        }
        Integer lunchHour = 7 + roomScheduleRepository.getLunchSlot();
        Time lunchTime = utcTime("" + lunchHour + ":45:00");
        int minPercentage = roomScheduleRepository.getMinPerc();
        int maxPercentage = roomScheduleRepository.getMaxPerc();
        List<IdNamePair> roomInfoWithRequiredCapacity =
                RoomScheduleCommunication.getRoomsIdsWithRequiredCapacity(numOfStudents,
                        minPercentage, maxPercentage);
        List<SlotInfo> dateIntPairs = RoomScheduleCommunication
                .getAvailableRoomsSlots(prefDate, numSlots, lunchTime);

        List<Integer> slotIdsToNotOverlapSameYear = roomScheduleRepository
                .getSlotIdsToNotOverlapSameYear(yearOfStudy);


        List<NameDateInfo> finalResult = new ArrayList<>();
        outer:
        for (IdNamePair i : roomInfoWithRequiredCapacity) {
            for (SlotInfo pair : dateIntPairs) {
                if (i.getId() == pair.getRoomId() && !slotIdsToNotOverlapSameYear
                        .contains(pair.roomSlotId)) {
                    finalResult.add(new NameDateInfo(pair.getDate(),
                            i.getName(), pair.getRoomSlotId()));
                    //once we found one room slot to schedule the lecture, we can
                    //stop searching
                    break outer;
                }
            }
        }
        if (finalResult.size() == 0) { //no available slots for the input given
            return "There are no available slots for the input given. Try again!";
        } else {
            NameDateInfo result = finalResult.get(0);
            roomScheduleRepository.saveAndFlush(new RoomSchedule(
                    result.getRoomSlotId(), lectureId, yearOfStudy));
            for (int i = 0; i < numSlots; i++) {
                roomScheduleRepository.saveAndFlush(new
                        RoomSchedule(result.getRoomSlotId()
                        + (i + 1) * 20, lectureId, yearOfStudy));
            }
            RoomScheduleCommunication.makeRoomSlotsOccupied(result.getRoomSlotId(), numSlots);
            return "Your lecture was successfully scheduled for: " +
                    result.getDate().toString() +
                    " in the room with name: " + result.getRoomName();
        }
    }

    /**
     * UTC Converter.

     * @param s time as string
     * @return UTC time
     * @throws ParseException exception
     */
    public static Time utcTime(String s) throws ParseException {
        final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        final java.util.Date dateObj = sdf.parse(s);
        Time result = Time.valueOf(dateObj.toInstant().toString().substring(11, 19));
        return result;
    }

    /**
     * Method for canceling a scheduled lecture.


     * @param id id of the lecture to be cancelled
     * @return message
     * @throws IOException ioException
     */
    @DeleteMapping(path = "/cancelLecture/{id}")
    public @ResponseBody
    String deleteLecture(@RequestHeader(authHeader) String token,
                         @PathVariable int id) throws IOException {
        if (Authorization.authorize(token, "Teacher")) {
            List<IntPair> values = roomScheduleRepository.getSlotIdAndRoomSlotId(id);
            if (values.size() == 0) {
                return "There is no scheduled lecture with the given id";
            } else {
                for (IntPair p : values) {
                    roomScheduleRepository.deleteById(p.getRoomScheduleId());
                    RoomScheduleCommunication.makeRoomSlotAvailable(p.getRoomSlotId());
                }
            }
            return "Canceled lecture";
        } else {
            throw new RuntimeException(errorMessage);
        }
    }


    /**
     * Retrieve all Rooms Schedules.
     *
     * @return all Rooms Schedules stored in the database
     */
    @GetMapping(path = "/allroomsschedules")
    public @ResponseBody
    Iterable<RoomSchedule> getAllRoomsSchedules(@RequestHeader(authHeader) String token) {
        if (Authorization.authorize(token, student)) {
            return roomScheduleRepository.findAll();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Method for getting a Room Schedule.
     *
     * @param id id of the RoomSchedule that is to be retrieved
     * @return RoomSchedule with id 'id'
     */
    @GetMapping(path = "/roomschedule/{id}")
    public @ResponseBody
    RoomSchedule getRoomSchedule(@RequestHeader(authHeader) String token,
                                 @PathVariable int id) {
        if (Authorization.authorize(token, student)) {
            Optional<RoomSchedule> roomSchedule = roomScheduleRepository.findById(id);
            if (roomSchedule.isPresent()) {
                return roomSchedule.get();
            } else {
                throw new RuntimeException("Room Schedule not found for the id " + id);
            }
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

}


