package roomscheduler;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matchers;
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
import roomscheduler.controllers.RuleController;
import roomscheduler.entities.Rule;

@SpringBootTest(classes = RoomScheduleService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class RuleControllerTest {

    @Autowired
    private transient MockMvc mockMvc;
    private transient RuleController ruleController;
    final transient Gson gson = new GsonBuilder().create();
    final transient String authorization = "Authorization";
    final transient String bearer = "Bearer token";
    private static MockedStatic<Authorization> mockedAuth;
    private static MockedStatic<RoomScheduleCommunication> mockedRoomSlotCom;
    private transient String createRule = "/createRule";
    private transient String saveRule = "Saved rule";
    private transient String allRules = "/allRules";
    private transient String ruleName = "rule1";
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(ruleController).build();
    }

    /**
     * Before all mock Authorization and RoomScheduleCommunication.
     */
    @SuppressWarnings("PMD")
    @BeforeAll
    public static void mockAuthorization() {
        String br = "Bearer token";
        mockedAuth = Mockito.mockStatic(Authorization.class);
        mockedAuth.when(() -> Authorization.authorize(br, "Admin")).thenReturn(true);
        mockedAuth.when(() -> Authorization.authorize(br, "Student")).thenReturn(true);
        mockedAuth.when(() -> Authorization.authorize(br, "Teacher")).thenReturn(true);
        mockedRoomSlotCom = Mockito.mockStatic(RoomScheduleCommunication.class);
    }

    @AfterAll
    public static void closeMock() {
        mockedAuth.reset();
        mockedRoomSlotCom.reset();
    }

    @AfterEach
    public void reset() {
        mockedRoomSlotCom.reset();
    }

    @Test
    public void addNewRoomSlotTest() throws Exception {
        Rule rs = new Rule(ruleName, "v1");
        String requestJson = gson.toJson(rs);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(createRule)
                        .header(authorization, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(saveRule));
    }

    @Test
    public void getAllRulesTest() throws Exception {
        for (int i = 1; i < 3; i++) {
            Rule rs = new Rule("rule" + i, "v" + i);
            rs.setIdrules(i + 1);
            String requestJson = gson.toJson(rs);
            this.mockMvc.perform(
                    MockMvcRequestBuilders.post(createRule)
                            .header(authorization, bearer)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson)
            )
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(saveRule));
        }

        this.mockMvc.perform(MockMvcRequestBuilders.get(allRules)
                .header(authorization, bearer))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(ruleName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].value").value("v1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("rule2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].value").value("v2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

    }

    @Test
    public void editRule() throws Exception {
        Rule rs = new Rule(ruleName, "v1");
        rs.setIdrules(1);
        String requestJson = gson.toJson(rs);
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(createRule)
                        .header(authorization, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(saveRule));

        this.mockMvc.perform(MockMvcRequestBuilders.get(allRules)
                .header(authorization, bearer))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(ruleName))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].value").value("v1"));

        rs.setName("rule2");
        rs.setValue("v2");
        String requestJson2 = gson.toJson(rs);
        this.mockMvc.perform(MockMvcRequestBuilders.put("/modifyRule")
                .header(authorization, bearer)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson2))
                .andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get(allRules)
                .header(authorization, bearer))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("rule2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].value").value("v2"));

    }

    @Test
    public void deleteRule() throws Exception {
        for (int i = 1; i < 3; i++) {
            Rule rs = new Rule(ruleName, "v1");
            rs.setIdrules(i);
            String requestJson = gson.toJson(rs);
            this.mockMvc.perform(
                    MockMvcRequestBuilders.post(createRule)
                            .header(authorization, bearer)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson)
            )
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.content().string(saveRule));
        }

        this.mockMvc.perform(MockMvcRequestBuilders.get(allRules)
                .header(authorization, bearer))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)));

        String req = "/deleteRule/" + "2";
        this.mockMvc.perform(MockMvcRequestBuilders.delete(req)
                .header(authorization, bearer))
                .andExpect(status().isOk());

        this.mockMvc.perform(MockMvcRequestBuilders.get(allRules)
                .header(authorization, bearer))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)));
    }
}
