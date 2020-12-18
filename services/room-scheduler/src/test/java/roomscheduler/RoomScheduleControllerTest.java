package roomscheduler;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import roomscheduler.communication.RoomScheduleCommunication;
import roomscheduler.controllers.Authorization;
import roomscheduler.controllers.RoomScheduleController;
import roomscheduler.entities.RoomSchedule;

import java.util.Date;

@SpringBootTest(classes = RoomScheduleService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RoomScheduleControllerTest {

    @Autowired
    private transient MockMvc mockMvc;
    private transient RoomScheduleController roomScheduleController;
    final transient Gson gson = new GsonBuilder().create();
    final transient String addRoomSchedule = "/addroomschedule";
    final transient String authorization = "Authorization";
    @SuppressWarnings("PMD")
    final transient String bearer = "Bearer token";
    private static MockedStatic<Authorization> mockedAuth;
    private static MockedStatic<RoomScheduleCommunication> mockedRoomCom;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(roomScheduleController).build();
    }

    /**
     * Before all mock Authorization and RoomScheduleCommunication.
     */
    @BeforeAll
    public static void mockAuthorization() {
        mockedAuth = Mockito.mockStatic(Authorization.class);
        mockedAuth.when(() -> Authorization.authorize("Bearer token", "Admin")).thenReturn(true);
        mockedAuth.when(() -> Authorization.authorize("Bearer token", "Student")).thenReturn(true);
        mockedAuth.when(() -> Authorization.authorize("Bearer token", "Teacher")).thenReturn(true);
        mockedRoomCom = Mockito.mockStatic(RoomScheduleCommunication.class);
    }

    @AfterAll
    public static void closeMock() {
        mockedAuth.close();
        mockedRoomCom.close();
    }

    @AfterEach
    public void reset() {
        mockedRoomCom.reset();
    }


    @Test
    public void addNewRoomScheduleTest() throws Exception {

        String requestJson = gson.toJson(new RoomSchedule(1, 1, 2));
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(addRoomSchedule)
                        .header(authorization, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Saved room schedule"));

        String req = "/roomschedule/" + "1";
        this.mockMvc.perform(
                MockMvcRequestBuilders.get(req)
                        .header(authorization, bearer)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.room_slots_id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lectures_id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.year_of_study")
                        .value(2));
    }

    @Test
    public void allRoomsSchedulesTest() throws Exception {
        for (int i = 0; i < 2; i++) {
            RoomSchedule roomSchedule = new RoomSchedule(i + 1, i + 1, i + 1);
            this.mockMvc.perform(MockMvcRequestBuilders.post(addRoomSchedule)
                    .header(authorization, bearer)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(roomSchedule))).andExpect(status().isOk());
        }

        this.mockMvc.perform(MockMvcRequestBuilders.get("/allroomsschedules")
                .header(authorization, bearer))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].room_slots_id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].room_slots_id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lectures_id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lectures_id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].year_of_study").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].year_of_study").value(2));
    }

    @Test
    public void cancelLecture() throws Exception {
        for (int i = 0; i < 3; i++) {
            RoomSchedule room = new RoomSchedule(1, 3, 1);
            this.mockMvc.perform(MockMvcRequestBuilders.post(addRoomSchedule)
                    .header(authorization, bearer)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(room))).andExpect(status().isOk());
        }

        String req = "/cancelLecture/" + "3";
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(req)
                        .header(authorization, bearer))
                .andExpect(MockMvcResultMatchers.content().string("Canceled lecture"));

        mockedRoomCom.verify(Mockito.times(3), () ->
                RoomScheduleCommunication.makeRoomSlotAvailable(1));
    }

    @Test
    public void cancelLectureThatIsNotPresentTest() throws Exception {
        for (int i = 0; i < 3; i++) {
            RoomSchedule room = new RoomSchedule(1, 3, 1);
            this.mockMvc.perform(MockMvcRequestBuilders.post(addRoomSchedule)
                    .header(authorization, bearer)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(gson.toJson(room))).andExpect(status().isOk());
        }

        String req = "/cancelLecture/" + "2";
        this.mockMvc.perform(
                MockMvcRequestBuilders.delete(req)
                        .header(authorization, bearer))
                .andExpect(MockMvcResultMatchers.content().string("" +
                        "There is no scheduled lecture with the given id"));

        mockedRoomCom.verify(Mockito.times(0), () ->
                RoomScheduleCommunication.makeRoomSlotAvailable(Mockito.anyInt()));
    }

}
