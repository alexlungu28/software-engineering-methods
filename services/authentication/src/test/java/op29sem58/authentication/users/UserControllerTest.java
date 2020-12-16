package op29sem58.authentication.users;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import op29sem58.authentication.AuthenticationService;
import op29sem58.authentication.roles.Role;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@WithMockUser(roles = "ADMIN")
@SpringBootTest(classes = AuthenticationService.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
class UserControllerTest {

    transient Role r1 = new Role(1, "Student");

    @Autowired
    transient MockMvc mvc;

    @MockBean
    transient UserService userService;

    @After
    public void reset_mocks() {
        Mockito.reset(mvc);
    }

    @Test
    public void getAllUsers()
            throws Exception {
        User test = new User("user", "123", r1);
        User test2 = new User("user2", "abcd", r1);
        Iterable<User> allUsers = Arrays.asList(test, test2);

        given(userService.getAllUsers()).willReturn(allUsers);
        mvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].netid").value(test.getNetid()))
                .andExpect(jsonPath("$[1].netid").value(test2.getNetid()));

    }

    @Test
    void getUser()
            throws Exception {
        User test = new User("user", "123", r1);

        given(userService.getUser(test.getNetid())).willReturn(java.util.Optional.of(test));
        mvc.perform(get("/user/" + test.getNetid())
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("utf-8"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.netid").value(test.getNetid()));
    }
}