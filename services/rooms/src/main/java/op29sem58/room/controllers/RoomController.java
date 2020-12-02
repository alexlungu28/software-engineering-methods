package op29sem58.room.controllers;
import op29sem58.room.entities.RoomInfo;
import op29sem58.room.repositories.RoomRepository;
import op29sem58.room.entities.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @PutMapping(path = "/generateRooms")
    public @ResponseBody String generateRooms() {
        return roomRepository.createInitialRooms();
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

    @GetMapping(path = "/countRooms")
    public @ResponseBody
    Long getNumberOfRooms(){
        return roomRepository.count();
    }

    @GetMapping(path = "/getRoomsWithCapacityAtLeast/{numOfStudents}/{minPer}/{maxPer}")
    public @ResponseBody
    List<RoomInfo> getRoomWithCapAtLeast(@PathVariable Integer numOfStudents,
                                         @PathVariable Integer minPer,
                                         @PathVariable Integer maxPer){
        return roomRepository.getRoomsWithCapacityAtLeast(numOfStudents, minPer, maxPer);
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

    @GetMapping(path = "/getCoronaCapacity/{roomId}/{minPerc}/{maxPerc}")
    public @ResponseBody
    Integer coronaCapacity(@PathVariable Integer roomId,
                             @PathVariable Integer minPerc,
                             @PathVariable Integer maxPerc){
        return roomRepository.getCoronaCapacity(roomId, minPerc, maxPerc);
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


