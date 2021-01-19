package op29sem58.authentication;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    transient AuthenticationController authenticationController;

    @Test
    public void isStudentTest() {
        assertTrue(authenticationController.isStudent());
    }

    @Test
    public void isTeacherTest() {
        assertTrue(authenticationController.isTeacher());
    }

    @Test
    public void isAdminTest() {
        assertTrue(authenticationController.isAdmin());
    }
}
