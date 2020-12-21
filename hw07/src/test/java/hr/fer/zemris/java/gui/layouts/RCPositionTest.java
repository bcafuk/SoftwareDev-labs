package hr.fer.zemris.java.gui.layouts;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RCPositionTest {
    @Test
    void testParse() {
        RCPosition rcp1 = RCPosition.parse("0,0");
        assertEquals(0, rcp1.row);
        assertEquals(0, rcp1.column);

        RCPosition rcp2 = RCPosition.parse("1,2");
        assertEquals(1, rcp2.row);
        assertEquals(2, rcp2.column);

        RCPosition rcp3 = RCPosition.parse("-7,+8");
        assertEquals(-7, rcp3.row);
        assertEquals(+8, rcp3.column);

        RCPosition rcp4 = RCPosition.parse("5896,1569");
        assertEquals(5896, rcp4.row);
        assertEquals(1569, rcp4.column);

        RCPosition rcp5 = RCPosition.parse("-15,17");
        assertEquals(-15, rcp5.row);
        assertEquals(+17, rcp5.column);

        RCPosition rcp6 = RCPosition.parse("15,-17");
        assertEquals(+15, rcp6.row);
        assertEquals(-17, rcp6.column);

        RCPosition rcp7 = RCPosition.parse("007,000");
        assertEquals(7, rcp7.row);
        assertEquals(0, rcp7.column);

        RCPosition rcp8 = RCPosition.parse("-007,-000");
        assertEquals(-7, rcp8.row);
        assertEquals(-0, rcp8.column);
    }

    @Test
    void testParseThrowsForNull() {
        assertThrows(NullPointerException.class, () -> RCPosition.parse(null));
    }

    @Test
    void testParseThrowsForInvalidFormat() {
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse(""));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("0"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("0,"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse(",0"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse(" 0,0"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("1.0,1.0"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("a,0"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("=0,0"));
        assertThrows(IllegalArgumentException.class, () -> RCPosition.parse("+a,0"));
    }
}
