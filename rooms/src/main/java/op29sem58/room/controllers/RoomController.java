package op29sem58.room.controllers;
import op29sem58.room.repositories.RoomRepository;
import op29sem58.room.entities.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

/**
 * Controller for Room.
 * With methods for general and id-specific retrieval
 */
@Controller // This means that this class is a Controller
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @PostMapping(path = "/createRoom") // Map ONLY POST Requests
    public @ResponseBody
    String addNewRoom(@RequestBody Room r) {
        roomRepository.saveAndFlush(r);
        return "Saved room";
    }

    /**
     * Retrieve all Rooms.
     *
     * @return all Rooms stored in the database
     */
    @GetMapping(path = "/allrooms")
    public @ResponseBody
    Iterable<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    /**
     * Method for getting a Room.
     *
     * @param id id of the Room that is to be retrieved
     * @return Room with id 'id'
     */
    @GetMapping(path = "/room/{id}")
    public @ResponseBody
    Room getRoom(@PathVariable int id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return room.get();
        } else {
            throw new RuntimeException("Room not found for the id " + id);
        }
    }

    /**
     * Method for editing a Room.
     *
     * @param r Room to be edited
     * @return update message
     */
    @PutMapping(path = "/modifyRoom")
    public @ResponseBody
    String editRoom(@RequestBody Room r) {
        Optional<Room> room = roomRepository.findById(r.getId());
        if(room.isPresent()){
            room.get().setName(r.getName());
            room.get().setCapacity(r.getCapacity());
            roomRepository.saveAndFlush(room.get());
            return "Changed Room";
        }else{
            throw new RuntimeException("Room not found for the id " + r.getId());
        }
    }

    /**
     * Method for deleting a Room.
     *
     * @param id id of the Room to be deleted
     * @return Room with id 'id'
     */
    @DeleteMapping(path = "/deleteRoom/{id}")
    public @ResponseBody
    String deleteRoom(@PathVariable int id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            roomRepository.deleteById(id);
            return "Deleted Room";
        } else {
            throw new RuntimeException("Room not found for the id " + id);
        }
    }

}


