package roomscheduler.controllers;

import org.joda.time.DateTime;
import roomscheduler.entities.RoomSlot;
import roomscheduler.entities.Rule;
import roomscheduler.repositories.RoomSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;

import roomscheduler.communication.RoomSlotCommunication;


/**
 * Controller for RoomSlot.
 * With methods for general and id-specific retrieval
 */
@Controller // This means that this class is a Controller
public class RoomSlotController {

    @Autowired
    private RoomSlotRepository roomSlotRepository;

    public RoomSlotRepository getRoomSlotRepository() {
        return roomSlotRepository;
    }

    public void setRoomSlotRepository(RoomSlotRepository roomSlotRepository) {
        this.roomSlotRepository = roomSlotRepository;
    }

    @PostMapping(path = "/addroomslot") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRoomSlot(@RequestBody RoomSlot r) throws IOException {
        Object a = RoomSlotCommunication.getRoom(r.getRooms_id());
        if(a.toString().equals("not found")){
            return "There is no room with the id of the Room Slot entered!";
        }else{
            roomSlotRepository.saveAndFlush(r);
            return "Saved room slot";
        }
    }

    @GetMapping(path = "/countRooms") // Map ONLY POST Requests
    public @ResponseBody
    Long getNumberOfRooms() throws IOException {
        Long count = RoomSlotCommunication.numberOfRooms();
        return count;
    }

    @PutMapping(path = "/generateRoomSlots/{numDays}/{firstSlotDateTime}")
    public @ResponseBody String generateRoomSlots(@PathVariable int numDays,
                                                  @PathVariable
                                                          String firstSlotDateTime) throws IOException {
        Long numberOfRooms = RoomSlotCommunication.numberOfRooms();
        List<Rule> allRules = RoomSlotCommunication.getRules();
        HashMap<String, Integer> rules = new HashMap<>();
        for(Rule r : allRules){
            rules.put(r.getName(), Integer.parseInt(r.getValue()));
        }
        int breakSlot = rules.get("lunch slot");
        int timeBetweenSlotsInHours = (rules.get("buffer time") + rules.get("break time")) / 60;
        int timeBetweenSlotsInMin = (rules.get("buffer time") + rules.get("break time")) % 60;
        int slots_per_day = rules.get("slots per day");
        DateTimeFormatter formatter = org.joda.time.format.DateTimeFormat.forPattern("HH:mm:ss");
        String s = "";
        if (timeBetweenSlotsInMin / 10 == 0) s = "0";
        DateTime t = DateTime.parse(timeBetweenSlotsInHours+ ":" + s + timeBetweenSlotsInMin
                + ":00", formatter);
        String timeBetweenSlotsTime = formatter.print(t);

        return roomSlotRepository.createRoomSlots(slots_per_day, numDays, firstSlotDateTime, timeBetweenSlotsTime,
                breakSlot, numberOfRooms);
    }


    /**
     * Retrieve all Room Slots.
     *
     * @return all Rooms Slots stored in the database
     */
    @GetMapping(path = "/allroomslots")
    public @ResponseBody
    Iterable<RoomSlot> getAllRoomsSlots() {
        return roomSlotRepository.findAll();
    }

    /**
     * Method for getting a RoomSlot.
     *
     * @param id id of the RoomSlot that is to be retrieved
     * @return RoomSlot with id 'id'
     */
    @GetMapping(path = "/roomslot/{id}")
    public @ResponseBody
    RoomSlot getRoomSlot(@PathVariable int id) {
        Optional<RoomSlot> roomSlot = roomSlotRepository.findById(id);
        if (roomSlot.isPresent()) {
            return roomSlot.get();
        } else {
            throw new RuntimeException("Room Slot not found for the id " + id);
        }
    }

}


