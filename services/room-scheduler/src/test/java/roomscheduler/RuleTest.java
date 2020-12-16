package roomscheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.Rule;



@SpringBootTest
public class RuleTest {

    @Test
    void constructorTest() {
        Rule rs = new Rule("rule1", "v1");
        assertEquals(rs.getName(), "rule1");
        assertEquals(rs.getValue(), "v1");
        assertNull(rs.getIdrules());
    }

    @Test
    void toStringTest() {
        Rule rs = new Rule("rule2", "rule2");
        assertEquals(rs.toString(), "Rule{idrules=null, name='rule2', value='rule2'}");
    }

    @Test
    void equalsTest() {
        Rule rs = new Rule("rule3", "rule30");
        Rule rs2 = new Rule("rule3", "rule30");
        Rule rs3 = new Rule("rule20", "rule20");
        Rule rs4 = new Rule("rule15", "wrongrule");
        assertEquals(rs, rs2);
        assertNotEquals(rs, rs3);
        assertNotEquals(rs, rs4);
    }

    @Test
    public void testHashCode() {
        Rule rs = new Rule("r1", "v1");
        Rule rs2 = new Rule("r1", "v1");
        assertNotSame(rs, rs2);
        assertEquals(rs.hashCode(), rs2.hashCode());
    }
}
