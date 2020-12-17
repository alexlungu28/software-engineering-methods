package roomscheduler.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import roomscheduler.RoomScheduleService;
import roomscheduler.entities.RoomSchedule;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = RoomScheduleService.class)
@RunWith(SpringRunner.class)
@WithMockUser(roles = "ADMIN")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class RoomScheduleControllerTest {

    @Autowired
    private transient MockMvc mockMvc;

    @InjectMocks
    private transient RoomScheduleController roomScheduleController;

    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(roomScheduleController).build();
    }

    final transient Gson gson = new GsonBuilder().create();

    @Test
    public void saveRoomTest() throws Exception {
        String requestJson = gson.toJson(new RoomSchedule(1, 2, 1));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/addroomschedule")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Saved room schedule"));

//        String req = "/room/" + "1";
//        this.mockMvc.perform(MockMvcRequestBuilders.get(req))
//                .andExpect(status().isOk())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tz90"))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.capacity").value(100));
    }
}