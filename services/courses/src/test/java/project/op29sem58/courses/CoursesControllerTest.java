package project.op29sem58.courses;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import project.op29sem58.courses.communication.authorization.Authorization;
import project.op29sem58.courses.communication.authorization.Role;
import project.op29sem58.courses.database.entities.Course;

@SpringBootTest(classes = CourseService.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class CoursesControllerTest {

    @Autowired
    private transient MockMvc mockMvc;
    private transient CoursesController coursesController;
    private transient String createCourses = "/createCourse";
    final transient String authorization = "Authorization";
    final transient String bearer = "Bearer token";
    final transient Gson gson = new GsonBuilder().create();
    private static MockedStatic<Authorization> mockedAuth;

    /**
     * Before all mock Authorization.
     */
    @BeforeAll
    @SuppressWarnings("PMD")
    public static void mockAuthorization() {
        String br = "Bearer token";
        mockedAuth = Mockito.mockStatic(Authorization.class);
        mockedAuth.when(() -> Authorization.authorize(br, Role.Admin)).thenReturn(true);
        mockedAuth.when(() -> Authorization.authorize(br,  Role.Student)).thenReturn(true);
        mockedAuth.when(() -> Authorization.authorize(br, Role.Teacher)).thenReturn(true);
    }

    @AfterAll
    public static void closeMock() {
        mockedAuth.close();
    }

    @Test
    public void createCourseTest() throws Exception {

        String requestJson = gson.toJson(new Course("1", "ads", "Cse1305", 1));
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(createCourses)
                        .header(authorization, bearer)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
        )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.teacherNetId").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ads"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("Cse1305"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.yearOfStudy").value(1));
    }

}
