package roomscheduler.controllers;

import roomscheduler.entities.RoomSchedule;
import roomscheduler.repositories.RoomScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

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


