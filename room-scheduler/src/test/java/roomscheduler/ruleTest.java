package roomscheduler;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import roomscheduler.entities.IdNamePair;
import roomscheduler.entities.Rule;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ruleTest {

    @Test
    void constructorTest() {
        Rule rs = new Rule("rule1", "rule");
        assertEquals(rs.getName(), "rule1");
        assertEquals(rs.getValue(), "rule");
        assertNull(rs.getIdrules());
    }

    @Test
    void toStringTest() {
        Rule rs = new Rule("rule1", "rule");
        assertEquals(rs.toString(), "Rule{idrules=null, name='rule1', value='rule'}");
    }

    @Test
    void equalsTest() {
        Rule rs = new Rule("rule1", "rule");
        Rule rs2 = new Rule("rule1", "rule");
        Rule rs3 = new Rule("rule2", "rule");
        Rule rs4 = new Rule("rule1", "wrongrule");
        assertEquals(rs, rs2);
        assertNotEquals(rs, rs3);
        assertNotEquals(rs, rs4);
    }

    @Test
    public void testHashCode() {
        Rule rs = new Rule("rule1", "rule");
        Rule rs2 = new Rule("rule1", "rule");
        assertNotSame(rs, rs2);
        assertEquals(rs.hashCode(), rs2.hashCode());
    }
}
