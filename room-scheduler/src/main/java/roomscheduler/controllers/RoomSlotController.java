package roomscheduler.controllers;

import roomscheduler.entities.RoomSlot;
import roomscheduler.repositories.RoomSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
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


