package roomscheduler.controllers;

import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;
import roomscheduler.communication.RoomSlotCommunication;
import roomscheduler.entities.RoomSlot;
import roomscheduler.entities.Rule;
import roomscheduler.repositories.RoomSlotRepository;



/**
 * Controller for RoomSlot.
 * With methods for general and id-specific retrieval
 */
@Controller // This means that this class is a Controller
public class RoomSlotController {

    @Autowired
    private RoomSlotRepository roomSlotRepository;

    final transient String authHeader = "Authorization";

    transient String errorMessage = "You do not have the privilege to perform this action.";

    public RoomSlotRepository getRoomSlotRepository() {
        return roomSlotRepository;
    }

    public void setRoomSlotRepository(RoomSlotRepository roomSlotRepository) {
        this.roomSlotRepository = roomSlotRepository;
    }

    /**
     * Method for adding a new room slot.
     *
     *
     * @param r room slot to be added
     * @return message
     * @throws IOException ioException
     */
    @PostMapping(path = "/addroomslot") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRoomSlot(@RequestHeader(authHeader) String token,
                          @RequestBody RoomSlot r) throws IOException {
        if (Authorization.authorize(token, "Teacher")) {
            Object a = RoomSlotCommunication.getRoom(r.getRooms_id());
            if (a.toString().equals("not found")) {
                return "There is no room with the id of the Room Slot entered!";
            } else {
                roomSlotRepository.saveAndFlush(r);
                return "Saved room slot";
            }
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Get the number of rooms.
     *
     * @param token jwt token
     * @return the number of rooms
     * @throws IOException in case something goes wrong
     */
    @GetMapping(path = "/countRooms") // Map ONLY POST Requests
    public @ResponseBody
    Long getNumberOfRooms(@RequestHeader(authHeader) String token) throws IOException {
        if (Authorization.authorize(token, "Student")) {
            Long count = RoomSlotCommunication.numberOfRooms();
            return count;
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Method for initializing the room slot table.
     *
     * @param numDays numberOfDays
     * @param firstSlotDateTime dateTime of the first slot
     * @return message
     * @throws IOException ioException
     */
    @PutMapping(path = "/generateRoomSlots/{numDays}/{firstSlotDateTime}")
    public @ResponseBody String generateRoomSlots(@RequestHeader(authHeader) String token,
                                                  @PathVariable int numDays,
              @PathVariable String firstSlotDateTime) throws IOException {
        if (Authorization.authorize(token, "Admin")) {
            Long numberOfRooms = RoomSlotCommunication.numberOfRooms();
            List<Rule> allRules = RoomSlotCommunication.getRules();
            HashMap<String, Integer> rules = new HashMap<>();
            for (Rule r : allRules) {
                rules.put(r.getName(), Integer.parseInt(r.getValue()));
            }
            int breakSlot = rules.get("lunch slot");
            int timeBetweenSlotsInHours = (rules.get("slot duration") +
                    rules.get("break time")) / 60;
            int timeBetweenSlotsInMin = (rules.get("slot duration") +
                    rules.get("break time")) % 60;
            int slotsPerDay = rules.get("slots per day");
            DateTimeFormatter formatter
                    = org.joda.time.format.DateTimeFormat.forPattern("HH:mm:ss");
            String s;
            if (timeBetweenSlotsInMin / 10 == 0) {
                s = "0";
            } else {
                s = "";
            }
            DateTime t = DateTime.parse(timeBetweenSlotsInHours + ":" + s + timeBetweenSlotsInMin
                    + ":00", formatter);
            String timeBetweenSlotsTime = formatter.print(t);

            return roomSlotRepository.createRoomSlots(slotsPerDay, numDays,
                    firstSlotDateTime, timeBetweenSlotsTime,
                    breakSlot, numberOfRooms);
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Method for making a room slot occupied.
     *
     * @param id id of the room slot
     * @param numOfSlots numberOfSlots
     * @return message
     */
    @PostMapping(path = "/updateRoomSlot/{id}/{numOfSlots}")
    public @ResponseBody
    String makeRoomSlotsOccupied(@RequestHeader(authHeader) String token,
                                 @PathVariable Integer id,
                                 @PathVariable Integer numOfSlots) {
        if (Authorization.authorize(token, "Teacher")) {
            Optional<RoomSlot> roomSlot = roomSlotRepository.findById(id);
            if (roomSlot.isPresent()) {
                roomSlot.get().setOccupied(1);
                roomSlotRepository.saveAndFlush(roomSlot.get());
                for (int i = 0; i < numOfSlots; i++) {
                    if (i < numOfSlots - 1) {
                        Optional<RoomSlot> next = roomSlotRepository.findById(id + (i + 1) * 20);
                        next.get().setOccupied(1);
                        roomSlotRepository.saveAndFlush(next.get());
                    } else {
                        Optional<RoomSlot> next = roomSlotRepository.findById(id + (i + 1) * 20);
                        next.get().setOccupied(3);
                        roomSlotRepository.saveAndFlush(next.get());
                    }

                }
                return "Marked room slots as occupied!";
            } else {
                throw new RuntimeException("Room slot not found for the id " + id);
            }
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Method for making a room slot available.
     *
     * @param id id of the room slot
     * @return message
     */
    @PostMapping(path = "/makeRoomSlotAvailable/{id}")
    public @ResponseBody
    String makeRoomSlotAvail(@RequestHeader(authHeader) String token,
                             @PathVariable Integer id) {
        if (Authorization.authorize(token, "Teacher")) {
            Optional<RoomSlot> roomSlot = roomSlotRepository.findById(id);
            if (roomSlot.isPresent()) {
                Timestamp timestamp = roomSlot.get().getDate_time();
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        .format(new Date(timestamp.getTime()));
                Timestamp b = Timestamp.valueOf(date + " 12:45:00");
                if (b.getTime() == timestamp.getTime()) { //it is a lunch slot
                    roomSlot.get().setOccupied(2);
                } else {
                    roomSlot.get().setOccupied(0);
                }
                roomSlotRepository.saveAndFlush(roomSlot.get());
                return "Marked room slot as available!";
            } else {
                throw new RuntimeException("Room slot not found for the id " + id);
            }
        } else {
            throw new RuntimeException(errorMessage);
        }
    }


    /**
     * Retrieve all Room Slots.
     *
     * @return all Rooms Slots stored in the database
     */
    @GetMapping(path = "/allroomslots")
    public @ResponseBody
    Iterable<RoomSlot> getAllRoomsSlots(@RequestHeader(authHeader) String token) {
        if (Authorization.authorize(token, "Student")) {
            return roomSlotRepository.findAll();
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

    /**
     * Method for getting a RoomSlot.
     *
     * @param id id of the RoomSlot that is to be retrieved
     * @return RoomSlot with id 'id'
     */
    @GetMapping(path = "/roomslot/{id}")
    public @ResponseBody
    RoomSlot getRoomSlot(@RequestHeader(authHeader) String token,
                         @PathVariable int id) {
        if (Authorization.authorize(token, "Student")) {
            Optional<RoomSlot> roomSlot = roomSlotRepository.findById(id);
            if (roomSlot.isPresent()) {
                return roomSlot.get();
            } else {
                throw new RuntimeException("Room Slot not found for the id " + id);
            }
        } else {
            throw new RuntimeException(errorMessage);
        }
    }

}


