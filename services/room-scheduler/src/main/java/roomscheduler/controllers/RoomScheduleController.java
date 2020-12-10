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

    /**
     * Method for getting all room schedules.

     *
     *  @return list of schedule information
     * @throws IOException exception
     */
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
        Integer lunchHour = 9 + roomScheduleRepository.getLunchSlot();
        Time lunchTime = utcTime("" + lunchHour  + ":45:00");
        int minPercentage = roomScheduleRepository.getMinPerc();
        int maxPercentage = roomScheduleRepository.getMaxPerc();
        List<IdNamePair> roomInfoWithRequiredCapacity =
                RoomScheduleCommunication.getRoomsIdsWithRequiredCapacity(numOfStudents,
                        minPercentage, maxPercentage);
        List<SlotInfo> dateIntPairs = RoomScheduleCommunication
                .getAvailableRoomsSlots(prefDate, numSlots, lunchTime);

        //TODO do not let lectures of the same year overlap
        List<Integer> slotIdsOfSameYearLectures = roomScheduleRepository
                .getSlotIdsForLecturesOfTheSameYear(yearOfStudy);

        List<NameDateInfo> finalResult = new ArrayList<>();
        for (SlotInfo pair : dateIntPairs) {
            for (IdNamePair i : roomInfoWithRequiredCapacity) {
                if (i.getId() == pair.getRoomId() && !slotIdsOfSameYearLectures
                        .contains(pair.roomSlotId)) {
                    finalResult.add(new NameDateInfo(pair.getDate(),
                            i.getName(), pair.getRoomSlotId()));
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
            return "Your lecture was successfully scheduled for: " + result.getDate().toString() +
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
    String deleteLecture(@PathVariable int id) throws IOException {
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


