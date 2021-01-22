package op29sem58.authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import op29sem58.authentication.users.User;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest
public class AuthenticationControllerTest {

    @Autowired
    transient AuthenticationController authenticationController;

    @MockBean
    private transient AuthenticationManager authenticationManager;

    @MockBean
    private transient MyUserDetailsService userDetailsService;

    @MockBean
    private transient JwtUtil jwtTokenUtil;

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

    @Test
    public void loginTest() {
        User user = new User("user", "1234");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getNetid(), "" + user.getPassword().hashCode())))
                .thenReturn(new TestingAuthenticationToken(new Object(), new Object()));
        UserDetails userDetails = new MyUserDetails();
        Mockito.when(userDetailsService.loadUserByUsername(user.getNetid()))
                .thenReturn(userDetails);
        Mockito.when(jwtTokenUtil.generateToken(userDetails)).thenReturn("token");
        assertEquals("token", authenticationController.createAuthenticationToken(user));
    }

    @Test
    public void loginTestFail() {
        User user = new User("user", "1234");
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getNetid(), "" + user.getPassword().hashCode())))
                .thenThrow(new BadCredentialsException(""));
        assertEquals("Incorrect username or password",
                authenticationController.createAuthenticationToken(user));
    }
}
