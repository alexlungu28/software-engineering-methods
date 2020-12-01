package op29sem58.room;

import op29sem58.room.entities.Room;
import op29sem58.room.controllers.RoomController;
import op29sem58.room.repositories.RoomRepository;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@SpringBootTest
class RoomApplicationTests {

    @Autowired
    private RoomController roomController;
    @Autowired
    private RoomRepository roomRepository;

    @Test
    void contextLoads() {
    }

    /**
     * Testing the entity Room's constructor method.
     * Checking to see if a Room instance returns an exception if a negative capacity is inputted.
     */
    @Test
    public void roomNegativeCapacityTest() {
        try {
            Room room = new Room("tz2", -100);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "invalid capacity");
        }
    }

    /**
     * Testing the entity Room's constructor method.
     * Checking to see if a Room instance can be created and if we can return the Name and capacity.
     */
    @Test
    public void roomConstructorTest() {
        Room room = new Room("tz2", 100);
        assertEquals(room.getName(), "tz2");
        assertEquals(room.getCapacity(), (Integer) 100);
    }

    /**
     * Testing the entity Room's equal method.
     * Checking to see if two instances of the same Room return true.
     */
    @Test
    public void roomEqaulsTest() {
        Room room1 = new Room("tz2", 100);
        Room room2 = new Room("tz2", 100);
        Room room3 = new Room("tz3", 100);
        Room room4 = new Room("tz2", 130);
        //test two instances of same room.
        assertTrue(room1.equals(room2));
        //test two instances of room with different name.
        assertFalse(room2.equals(room3));
        //test two instances of room with different capacity.
        assertFalse(room2.equals(room4));
    }

    /**
     * Testing the controller RoomController's addNewRoom method.
     * Checking to see if a Room instance can be saved in the room repo and then deleted afterwards.
     */
    @Test
    public void saveAndGetRoomTest(){
        Room room1 = new Room("tz2", 100);
        roomRepository.saveAndFlush(room1);
        assertTrue(roomRepository.findAll().contains(room1));
        roomController.deleteRoom(room1.getId());
        assertFalse(roomRepository.findAll().contains(room1));
    }

    /**
     * Testing the controller RoomController's getAllRooms method.
     * Checking to see if a room is added if we can return a list of all rooms.
     */
//    @Test
//    public void saveAndGetAllRoomsTest(){
//        Room room1 = new Room("tz2", 100);
//        List x = new ArrayList<Room>();
//        x.add(room1);
//        roomRepository.saveAndFlush(room1);
//        assertEquals(roomController.getAllRooms(), x);
//        roomController.deleteRoom(room1.getId());
//    }

    /**
     * Testing the controller RoomController's getNumberOfRooms method.
     * Checking to see if we add a couple instances of Room, if we can return the number of rooms in the roomRepositoru.
     */
//    @Test
//    public void countRooms() {
//        Room room1 = new Room("tz2", 100);
//        Room room2 = new Room("tz2", 110);
//        Room room3 = new Room("tz2", 150);
//        roomRepository.saveAndFlush(room1);
//        roomRepository.saveAndFlush(room2);
//        roomRepository.saveAndFlush(room3);
////      assertEquals(roomController.getNumberOfRooms(), (Long) roomRepository.count());
//        assertEquals(roomController.getNumberOfRooms(), (Long) 3L);
//        roomRepository.delete(room1);
//        roomRepository.delete(room2);
//        roomRepository.delete(room3);
//    }

    /**
     * Testing the controller RoomController's modifyRoomCapacity method.
     * Checking to see if we can edit the capacity of an existing instance of Room in roomRepository.
     */
    @Test
    public void modifyRoomCapacity() {
        Optional <Room> room1 = roomRepository.findById(1);
        assertNotNull(room1);
        int id = room1.get().getId();
        int capacity = room1.get().getCapacity();
        Room r1 = new Room(room1.get().getName(), 5);
        r1.setId(room1.get().getId());
        roomController.editRoom(r1);
        assertNotEquals(roomRepository.findById(id).get().getCapacity(), (Long) 50L);
        r1.setCapacity(capacity);
        roomController.editRoom(r1);
        assertEquals( (int) roomRepository.findById(id).get().getCapacity(), capacity);
    }

    /**
     * Testing the controller RoomController's modifyRoomCapacity method.
     * Checking to see if an exception is thrown when a negative capacity is edited.
     */
    @Test
    public void modifyNegativeRoomCapacity() {
        Optional <Room> room1 = roomRepository.findById(1);
        assertNotNull(room1);
        try {
            roomController.editRoom(new Room(room1.get().getName(), -350));
        } catch (Exception e) {
            assertEquals(e.getMessage(), "invalid capacity");
        }
    }

    /**
     * Testing the controller RoomController's modifyRoomCapacity method.
     * Checking to see if we can edit the name of an existing instance of Room in roomRepository.
     */
    @Test
    public void modifyRoomName() {
        Optional <Room> room1 = roomRepository.findById(1);
        assertNotNull(room1);
        int id = room1.get().getId();
        String temp = room1.get().getName();
        Room r1 = new Room("IZ-33", room1.get().getCapacity());
        r1.setId(room1.get().getId());
        roomController.editRoom(r1);
        assertNotEquals(roomRepository.findById(id).get().getName(), "IZ-3");
        r1.setName(temp);
        roomController.editRoom(r1);
        assertEquals(roomRepository.findById(id).get().getName(), temp);
    }

    /**
     * Testing the controller RoomController's deleteRoom method.
     * Checking to see if we can delete an existing instance of Room in roomRepository.
     */
    @Test
    public void deleteRoom() {
        Room room1 = new Room("tz2", 100);
        roomRepository.saveAndFlush(room1);
        int id = room1.getId();
        roomController.deleteRoom(room1.getId());
        try {
            roomController.getRoom(id);
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Room not found for the id " + id);
        }
    }

}
