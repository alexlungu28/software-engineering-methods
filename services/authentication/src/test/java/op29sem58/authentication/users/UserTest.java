package op29sem58.authentication.users;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;
import op29sem58.authentication.roles.Role;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    transient String netid = "user";
    transient String password = "123";
    transient String roleName = "Student";
    transient User user = new User(netid, password, new Role(1, roleName));

    @Test
    public void constructorTest() {
        assertNotNull(user);
    }

    @Test
    public void constructorTest2() {
        User user2 = new User("netid", "pass");
        assertNotNull(user2);
    }

    @Test
    public void getNetIdTest() {
        assertEquals(netid, user.getNetid());
    }

    @Test
    public void setNetIdTest() {
        user.setNetid("admin");
        assertEquals("admin", user.getNetid());
    }

    @Test
    public void getPasswordTest() {
        assertEquals(password, user.getPassword());
    }

    @Test
    public void setPasswordTest() {
        user.setPassword("12345");
        assertEquals("12345", user.getPassword());
    }

    @Test
    public void getRoleTest() {
        Role role = new Role(1, roleName);
        assertEquals(role, user.getRole());
    }

    @Test
    public void setRoleTest() {
        Role role = new Role(3, "Admin");
        user.setRole(role);
        assertEquals(role, user.getRole());
    }

    @Test
    public void equalsTest() {
        User user2 = new User(netid, password, new Role(1, roleName));
        assertEquals(user, user2);
    }

    @Test
    public void equalsTest2() {
        User user2 = new User(netid, "1234", new Role(1, roleName));
        assertNotEquals(user, user2);
    }

    @Test
    public void equalsTest3() {
        assertEquals(user, user);
    }

    @Test
    public void equalsTest4() {
        assertNotEquals(user, new Object());
    }

    @Test
    public void hashCodeTest() {
        int hash = Objects.hash("user", "123", new Role(1, "Student"));
        assertEquals(hash, user.hashCode());
    }

    @Test
    public void toStringTest() {
        String str = "User{netID='user', password='123', role=Role{id=1, name='Student'}}";
        assertEquals(str, user.toString());
    }
}
