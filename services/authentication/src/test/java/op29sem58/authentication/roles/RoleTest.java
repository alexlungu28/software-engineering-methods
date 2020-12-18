package op29sem58.authentication.roles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Objects;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoleTest {
    transient Role role = new Role(1, "Student");

    @Test
    public void constructorTest() {
        assertNotNull(role);
    }

    @Test
    public void getIdTest() {
        assertEquals(1, role.getId());
    }

    @Test
    public void setIdTest() {
        role.setId(3);
        assertEquals(3, role.getId());
    }

    @Test
    public void getNameTest1() {
        assertEquals("ROLE_STUDENT", role.getName());
    }

    @Test
    public void getNameTest2() {
        Role role2 = new Role(2, "Teacher");
        assertEquals("ROLE_TEACHER", role2.getName());
    }

    @Test
    public void getNameTest3() {
        Role role2 = new Role(3, "Admin");
        assertEquals("ROLE_ADMIN", role2.getName());
    }

    @Test
    public void equalsTest() {
        Role role2 = new Role(1, "Student");
        assertEquals(role, role2);
    }

    @Test
    public void equalsTest2() {
        Role role2 = new Role(3, "Admin");
        assertNotEquals(role, role2);
    }

    @Test
    public void equalsTest3() {
        assertEquals(role, role);
    }

    @Test
    public void equalsTest4() {
        assertNotEquals(role, new Object());
    }

    @Test
    public void hashCodeTest() {
        int hash = Objects.hash(1, "Student");
        assertEquals(hash, role.hashCode());
    }

    @Test
    public void toStringTest() {
        String str = "Role{id=1, name='Student'}";
        assertEquals(str, role.toString());
    }
}
