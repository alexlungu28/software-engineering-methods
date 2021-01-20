package op29sem58.student.communication.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RoleTest {

    @Test
    public void roleTest() {
        Role role = Role.Admin;
        assertEquals("Admin", role.getName());
    }
}
