package op29sem58.student.controllers;

import static org.hamcrest.Matchers.hasSize;
// import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import op29sem58.student.StudentService;
// import op29sem58.student.communication.ServerCommunication;
import op29sem58.student.communication.adapters.LocalDateTimeAdapter;
import op29sem58.student.communication.authorization.Authorization;
import op29sem58.student.communication.authorization.Role;
import op29sem58.student.database.entities.Student;
// import op29sem58.student.local.entities.Course;
import op29sem58.student.local.entities.UserPreference;
import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(classes = StudentService.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class StudentControllerTest {
    @Autowired
    protected transient MockMvc mockMvc;

    final transient Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();
    private static MockedStatic<Authorization> mockedAuth;

    /**
     * Initialize the Authorization class to allow admins to perform operations.
     */
    @BeforeAll
    public static void mockAuthorization() {
        StudentControllerTest.mockedAuth = 
            Mockito.mockStatic(Authorization.class);
        StudentControllerTest.mockedAuth.when(
            () -> Authorization.authorize("Bearer admin", Role.Admin)
        ).thenReturn(true);
        StudentControllerTest.mockedAuth.when(
			() -> Authorization.authorize("Bearer student", Role.Student)
		).thenReturn(true);
    }

    private void initializeStudents(List<Student> students) throws Exception {
        String requestJson = this.gson.toJson(students);
        this.mockMvc.perform(
            post("/initializeStudents")
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer admin")
            .content(requestJson)
        )
            .andExpect(status().isOk());
    }

    @Test
    public void initializingStudentsTest() throws Exception {
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Student student = new Student();
            student.setLastVisited(LocalDateTime.now());
            student.setNetId("student" + i);
            student.setWantsToGo(true);
            students.add(student);
        }
        this.initializeStudents(students);
        this.mockMvc.perform(get("/getStudents").header("Authorization", "Bearer admin"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(8)));
    }
    
    @Test
    public void setPreferenceTest() throws Exception {
        List<Student> students = new ArrayList<>();
        students.add(new Student(
            "student0",
            LocalDateTime.now(),
            false
        ));
        this.initializeStudents(students);

        UserPreference newPreference = new UserPreference("student0", true);
        this.mockMvc.perform(
            post("/setPreferences")
            .contentType(APPLICATION_JSON)
            .header("Authorization", "Bearer student")
            .content(this.gson.toJson(newPreference))
        ).andExpect(status().isOk());
    }
    
    // @Test
    // public void assignStudentsUntilTest() throws Exception {
    //     List<Student> students = new ArrayList<>();
    //     students.add(new Student(
    //         "student0",
    //         LocalDateTime.now(),
    //         false
    //     ));
    //     this.initializeStudents(students);

    //     List<Course> courses = new ArrayList<>();
    //     for (int i = 0; i < 3; i++) {
    //         courses.add(new Course(
    //             i,
    //             new int[]{i}, 
    //             "teacher",
    //             "Course " + i,
    //             "cse" + i,
    //             1)
    //         );
    //     }

    //     ServerCommunication serverCommMock = Mockito.mock(ServerCommunication.class);
    //     when(serverCommMock.getAllCourses("Bearer admin"))
    //         .thenReturn(courses);
    //     // when(serverCommMock.getUserId("Bearer student"))
    //     //     .thenReturn("student0");

    //     this.mockMvc.perform(
    //         post("/assignStudentsUntil")
    //         .content(this.gson.toJson("2020-02-03T13:00:00"))
    //         .contentType(APPLICATION_JSON)
    //         .header("Authorization", "Bearer admin")
    //     ).andExpect(status().isOk()).andDo(print());
    // }
}
