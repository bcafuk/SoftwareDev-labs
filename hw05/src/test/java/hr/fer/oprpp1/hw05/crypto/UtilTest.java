package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UtilTest {
    @Test
    public void testHexToByteWithEmpty() {
        assertArrayEquals(new byte[]{}, Util.hextobyte(""));
    }

    @Test
    public void testHexToByte() {
        assertArrayEquals(new byte[]{0}, Util.hextobyte("00"));
        assertArrayEquals(new byte[]{6}, Util.hextobyte("06"));
        assertArrayEquals(new byte[]{10}, Util.hextobyte("0a"));
        assertArrayEquals(new byte[]{127}, Util.hextobyte("7f"));
        assertArrayEquals(new byte[]{-128}, Util.hextobyte("80"));
        assertArrayEquals(new byte[]{-68}, Util.hextobyte("BC"));
        assertArrayEquals(new byte[]{-1}, Util.hextobyte("ff"));
        assertArrayEquals(new byte[]{-64, -1, -18}, Util.hextobyte("C0ffee"));
        assertArrayEquals(new byte[]{1, -2, 4, -8, 16, -32, 64, -128, 0}, Util.hextobyte("01FE04F810E0408000"));
    }

    @Test
    public void testHexToByteThrowsForNull() {
        assertThrows(NullPointerException.class, () -> Util.hextobyte(null));
    }

    @Test
    public void testHexToByteThrowsForOddString() {
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("1"));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("0dd"));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("123456789"));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("0123456789abcdef0"));
    }

    @Test
    public void testHexToByteThrowsForInvalidChars() {
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte(" "));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("_"));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("COFFEE"));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("0xDEADBEEF"));
        assertThrows(IllegalArgumentException.class, () -> Util.hextobyte("15 "));
    }

    @Test
    public void testByteToHexWithEmpty() {
        assertEquals("", Util.bytetohex(new byte[]{}));
    }

    @Test
    public void testByteToHex() {
        assertEquals("00", Util.bytetohex(new byte[]{0}));
        assertEquals("06", Util.bytetohex(new byte[]{6}));
        assertEquals("0a", Util.bytetohex(new byte[]{10}));
        assertEquals("7f", Util.bytetohex(new byte[]{127}));
        assertEquals("80", Util.bytetohex(new byte[]{-128}));
        assertEquals("bc", Util.bytetohex(new byte[]{-68}));
        assertEquals("ff", Util.bytetohex(new byte[]{-1}));
        assertEquals("c0ffee", Util.bytetohex(new byte[]{-64, -1, -18}));
        assertEquals("01fe04f810e0408000", Util.bytetohex(new byte[]{1, -2, 4, -8, 16, -32, 64, -128, 0}));
    }

    @Test
    public void testByteToHexWithNull() {
        assertThrows(NullPointerException.class, () -> Util.bytetohex(null));
    }
}
