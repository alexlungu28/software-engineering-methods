package op29sem58.room.controllers;

import java.util.List;
import java.util.Optional;
import op29sem58.room.communication.authorization.Authorization;
import op29sem58.room.communication.authorization.Role;
import op29sem58.room.entities.Room;
import op29sem58.room.entities.RoomInfo;
import op29sem58.room.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * Controller for Room.
 * With methods for general and id-specific retrieval
 */
@Controller // This means that this class is a Controller
public class RoomController {
    @Autowired
    private RoomRepository roomRepository;

    final transient String authHeader = "Authorization";

    public RoomRepository getRoomRepository() {
        return roomRepository;
    }

    public void setRoomRepository(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    /**
     * Method for creating a room.
     *
     * @param token token
     * @param r Room to be added
     * @return if the token corresponds to an admin, return "created" message, exception otherwise
     */
    @PostMapping(path = "/createRoom") // Map ONLY POST Requests
    public ResponseEntity<String> addNewRoom(@RequestHeader(authHeader) String token,
                             @RequestBody Room r) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        roomRepository.saveAndFlush(r);
        return ResponseEntity.ok("Saved room");
    }

    /**
     * Retrieve all Rooms.
     *
     * @return all Rooms stored in the database
     */
    @GetMapping(path = "/allrooms")
    public ResponseEntity<Iterable<Room>> getAllRooms(@RequestHeader(authHeader) String token) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(roomRepository.findAll());
    }

    /**
     * Method for initializing the rooms table.
     *
     * @param token token
     * @return message ("saved" if token corresponds to an admin, exception otherwise)
     */
    @PutMapping(path = "/generateRooms")
    public ResponseEntity<String> generateRooms(@RequestHeader(authHeader) String token) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(roomRepository.createInitialRooms());
    }


    /**
     * Method for getting a Room.
     *
     * @param id id of the Room that is to be retrieved
     * @return Room with id 'id'
     */
    @GetMapping(path = "/room/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable int id) {
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            return ResponseEntity.ok(room.get());
        } else {
            throw new RuntimeException("Room not found for the id " + id);
        }
    }

    /**
     * Method for retrieving the number of rooms.
     *
     * @return number of rooms
     */
    @GetMapping(path = "/countRooms")
    public ResponseEntity<Long> getNumberOfRooms() {
        return ResponseEntity.ok(roomRepository.count());
    }

    /**
     * Get rooms with capacity that's at least a set amount.
     *
     * @param numOfStudents number of students
     * @param minPer minimum persons
     * @param maxPer maximum persons
     * @return the rooms that meet these conditions
     */
    @GetMapping(path = "/getRoomsWithCapacityAtLeast/{numOfStudents}/{minPer}/{maxPer}")
    public ResponseEntity<List<RoomInfo>> getRoomWithCapAtLeast(@PathVariable Integer numOfStudents,
                                         @PathVariable Integer minPer,
                                         @PathVariable Integer maxPer) {
        return ResponseEntity.ok(roomRepository.getRoomsWithCapacityAtLeast(
                numOfStudents, minPer, maxPer));
    }

    /**
     * Method for editing a Room.
     *
     * @param r Room to be edited
     * @return update message
     */
    @PutMapping(path = "/modifyRoom")
    public ResponseEntity<String> editRoom(@RequestHeader(authHeader) String token,
                                           @RequestBody Room r) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Room> room = roomRepository.findById(r.getId());
        if (room.isPresent()) {
            room.get().setName(r.getName());
            room.get().setCapacity(r.getCapacity());
            roomRepository.saveAndFlush(room.get());
            return ResponseEntity.ok("Changed Room");
        } else {
            throw new RuntimeException("Room not found for the id " + r.getId());
        }
    }

    /**
     * Get the corona capacity of a room.
     *
     * @param roomId id of room
     * @param minPerc min people
     * @param maxPerc max people
     * @return the capacity
     */
    @GetMapping(path = "/getCoronaCapacity/{roomId}/{minPerc}/{maxPerc}")
    public ResponseEntity<Integer> coronaCapacity(@PathVariable Integer roomId,
                             @PathVariable Integer minPerc,
                             @PathVariable Integer maxPerc) {
        return ResponseEntity.ok(roomRepository.getCoronaCapacity(roomId, minPerc, maxPerc));
    }




    /**
     * Method for deleting a Room.
     *
     * @param id id of the Room to be deleted
     * @return Room with id 'id'
     */
    @DeleteMapping(path = "/deleteRoom/{id}")
    public ResponseEntity<String> deleteRoom(@RequestHeader(authHeader) String token,
                                             @PathVariable int id) {
        if (!Authorization.authorize(token, Role.Admin)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Room> room = roomRepository.findById(id);
        if (room.isPresent()) {
            roomRepository.deleteById(id);
            return ResponseEntity.ok("Deleted Room");
        } else {
            throw new RuntimeException("Room not found for the id " + id);
        }
    }

}


