package op29sem58.room;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import op29sem58.room.controllers.Authorization;
import op29sem58.room.controllers.RoomController;
import op29sem58.room.entities.Room;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@SpringBootTest(classes = RoomService.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
class RoomControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @InjectMocks
    private transient RoomController roomController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(roomController).build();
    }
    @BeforeAll
    public static void mockAuthorization() {
        MockedStatic<Authorization> mockedAuth = Mockito.mockStatic(Authorization.class);
        mockedAuth.when(() -> Authorization.authorize("token", "Admin")).thenReturn(true);
    }

    final transient Gson gson = new GsonBuilder().create();
    final transient String createRoom = "/createRoom";

    /**
     * Testing the controller RoomController's createRoom method.
     * Checking to see if a Room instance can be saved.
     */
    @Test
    public void saveRoomTest() throws Exception {

        String requestJson = gson.toJson(new Room("tz90", 100));
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(createRoom).header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Saved room"));

        String req = "/room/" + "1";
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(req).header("Authorization", "Bearer token")
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tz90"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(100));
    }

    /**
     * Testing the controller RoomController's modifyRoomCapacity method.
     * Checking to see if we can edit the capacity of an existing instance of
     * Room in roomRepository.
     */
    @Test
    public void modifyRoomTest() throws Exception {
        Room room = new Room("room_2", 50);
        String requestJson = gson.toJson(room);
        this.mockMvc.perform(MockMvcRequestBuilders.post("/createRoom").header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andDo(print());

        String req = "/room/" + "1";
        this.mockMvc.perform(MockMvcRequestBuilders.get(req).header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("room_2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(50));

        room.setId(1);
        room.setName("room_3");
        room.setCapacity(350);
        String requestJson2 = gson.toJson(room);

        this.mockMvc.perform(MockMvcRequestBuilders.put("/modifyRoom").header("Authorization", "Bearer token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Changed Room"));

        this.mockMvc.perform(MockMvcRequestBuilders.get(req).header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("room_3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(350));
    }


    /**
     * Testing the controller RoomController's countRooms method.
     * Checking to see if the countRooms method returns the right amount of rooms.
     */
    @Test
    public void countTest() throws Exception {

        for (int i = 0; i < 8; i++) {
            Room room = new Room("room" + i, i + 5);
            this.mockMvc.perform(MockMvcRequestBuilders.post(createRoom).header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(room))).andExpect(status().isOk());
        }

        this.mockMvc.perform(MockMvcRequestBuilders.get("/countRooms").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(8)));
    }

    /**
     * Testing the controller RoomController's allRooms method.
     * Checking to see if all rooms are returned.
     */
    @Test
    public void allRoomsTest() throws Exception {
        for (int i = 0; i < 2; i++) {
            Room room = new Room("room" + i, i + 5);
            this.mockMvc.perform(MockMvcRequestBuilders.post(createRoom).header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(room))).andExpect(status().isOk());
        }

        this.mockMvc.perform(MockMvcRequestBuilders.get("/allrooms").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("room0"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("room1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].capacity").value("5"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].capacity").value("6"));
    }

    /**
     * Testing the controller RoomController's deleteTest method.
     * Testing to see if a room instance can be deleted.
     */
    @Test
    public void deleteTest() throws Exception {
        for (int i = 0; i < 2; i++) {
            Room room = new Room("room" + i, i + 5);
            this.mockMvc.perform(MockMvcRequestBuilders.post(createRoom).header("Authorization", "Bearer token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(room))).andExpect(status().isOk());
        }
        this.mockMvc.perform(MockMvcRequestBuilders.get("/countRooms").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(2)));

        String req = "/deleteRoom/" + "2";
        this.mockMvc.perform(MockMvcRequestBuilders.delete(req).header("Authorization", "Bearer token"))
                .andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get("/countRooms").header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.is(1)));
    }

    //still need to test getCoronaCapacityTest
    //    @Test
    //    public void getCoronaCapacityTest() throws Exception {
    //        Room room = new Room("tz2", 100);
    //        this.mockMvc.perform(MockMvcRequestBuilders.post(createRoom)
    //                .contentType(MediaType.APPLICATION_JSON)
    //                .content(gson.toJson(room))).andExpect(status().isOk());
    //
    //        String req = "/getCoronaCapacity/" + 1 + "/" + 10 + "/" + 60;
    //            this.mockMvc.perform(MockMvcRequestBuilders.get(req))
    //                    .andDo(print());
    //    }
    //
    //    @Test
    //    public void getRoomsWithCapacityAtleastTest() throws Exception {
    //        Room room = new Room("tz2", 100);
    //        this.mockMvc.perform(MockMvcRequestBuilders.post(createRoom)
    //                .contentType(MediaType.APPLICATION_JSON)
    //                .content(gson.toJson(room))).andExpect(status().isOk());
    //
    //        String req = "/getRoomsWithCapacityAtLeast/" + "50" + "/" + "10" + "/" + "60";
    //        this.mockMvc.perform(MockMvcRequestBuilders.get(req))
    //                .andDo(print());  }
}
