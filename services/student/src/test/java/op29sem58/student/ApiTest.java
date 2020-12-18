package op29sem58.student;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import op29sem58.student.communication.adapters.LocalDateTimeAdapter;
import op29sem58.student.communication.authorization.Authorization;
import op29sem58.student.communication.authorization.Role;
import op29sem58.student.database.entities.Student;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = StudentService.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class ApiTest {
    @Autowired
    protected transient MockMvc mockMvc;

    final transient Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    transient MockedStatic<Authorization> mockedAuth;

    /**
     * Initialize the Authorization class to allow admins to perform operations.
     */
    @Before
    public void mockAuthorization() {
        this.mockedAuth = Mockito.mockStatic(Authorization.class);
        this.mockedAuth.when(() -> Authorization.authorize("Bearer token", Role.Admin))
            .thenReturn(true);
    }
    
    @Test
    public void fail() {
        assertEquals(false, true);
    }

    @Test
    public void exampleTest() throws Exception {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Student student = new Student();
            student.setLastVisited(LocalDateTime.now());
            student.setNetId("student" + i);
            student.setWantsToGo(true);
            students.add(student);
        }
        String requestJson = gson.toJson(students);
        this.mockMvc.perform(post("/initializeStudents").contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer token")
                .content(requestJson)).andExpect(status().isOk());
        this.mockMvc.perform(get("/getStudents").header("Authorization", "Bearer token"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(8)));
    }
}
