package op29sem58.authentication.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import op29sem58.authentication.roles.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class UserServiceTest {

    @TestConfiguration
    static class LoginRegisterServiceImplTestContextConfiguration {
        @Bean
        public UserService loginRegisterService() {
            return new UserService();
        }
    }

    @Autowired
    transient UserService userService;

    @MockBean
    transient UserRepository userRepository;

    @Test
    void getAllUsers() {
        Role r1 = new Role(1, "Student");
        User test = new User("user", "123", r1);
        User test2 = new User("student", "1234", r1);
        List<User> allUsers = Arrays.asList(test, test2);
        Mockito.when(userRepository.findAll())
                .thenReturn(allUsers);
        Iterable<User> found = userService.getAllUsers();
        Iterator<User> itr = found.iterator();
        assertThat(itr.next()).isEqualTo(test);
        assertThat(itr.next()).isEqualTo(test2);
    }

    @Test
    void getUser() {
        Role r1 = new Role(1, "Student");
        User test = new User("user", "123", r1);
        when(userRepository.findById(test.getNetid()))
                .thenReturn(java.util.Optional.of(test));
        User found = userService.getUser(test.getNetid()).orElse(null);
        assertThat(found.getNetid())
                .isEqualTo(test.getNetid());
    }

    @Test
    void modifyUser() {
        Role r1 = new Role(1, "Student");
        User test = new User("user", "1234", r1);
        when(userRepository.existsById(test.getNetid())).thenReturn(true);
        when(userRepository.findById(test.getNetid()))
                .thenReturn(java.util.Optional.of(test));
        String result = userService.modifyUser(test);
        assertThat(result).isEqualTo("User Updated");
    }
}