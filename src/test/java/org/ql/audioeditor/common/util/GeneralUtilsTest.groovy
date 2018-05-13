package org.ql.audioeditor.common.util

class GeneralUtilsTest extends GroovyTestCase {
    void testGetFileExtension() {
        File f = new File("")
        assertEquals("",
                GeneralUtils.getFileExtension(f))

        f = new File("abc")
        assertEquals("abc",
                GeneralUtils.getFileExtension(f))

        f = new File("abc.abd")
        assertEquals("abd",
                GeneralUtils.getFileExtension(f))

        f = new File("dra.abc.abd")
        assertEquals("abd",
                GeneralUtils.getFileExtension(f))

        f = new File("C:/dra.abc.abd")
        assertEquals("abd",
                GeneralUtils.getFileExtension(f))

        f = new File("C:\\dra.abc.abd")
        assertEquals("abd",
                GeneralUtils.getFileExtension(f))
    }

    void testGetDir() {
        assertEquals("",
                GeneralUtils.getDir(""))
        assertEquals("C:\\",
                GeneralUtils.getDir("C:\\dra.abc.abd"))
        assertEquals("C:\\",
                GeneralUtils.getDir("C:/dra.abc.abd"))
    }

    void testConvertToNumber() {
        assertEquals(0,
                GeneralUtils.convertToNumber(""))
        assertEquals(0,
                GeneralUtils.convertToNumber("qwertz"))
        assertEquals(0,
                GeneralUtils.convertToNumber("qwertz3"))
        assertEquals(0,
                GeneralUtils.convertToNumber("0"))
        assertEquals(10,
                GeneralUtils.convertToNumber("10"))
        assertEquals(0,
                GeneralUtils.convertToNumber("2^10"))
        assertEquals(10000,
                GeneralUtils.convertToNumber("10000"))
    }
}
