package org.ql.audioeditor.common.util

class TimeUtilsTest extends GroovyTestCase {
    void testFormatDuration() {
        assertEquals("00:00:30",
                TimeUtils.formatDuration(30))

        assertEquals("00:05:00",
                TimeUtils.formatDuration(300))

        assertEquals("00:50:00",
                TimeUtils.formatDuration(3000))

        assertEquals("08:20:00",
                TimeUtils.formatDuration(30000))

        assertEquals("83:20:00",
                TimeUtils.formatDuration(300000))

        assertEquals("833:20:00",
                TimeUtils.formatDuration(3000000))

        assertEquals("-833:20:00",
                TimeUtils.formatDuration(-3000000))
    }

    void testFramesToSeconds() {
        assertEquals(1,
                TimeUtils.framesToSeconds(44100, 44100))

        assertEquals(0,
                TimeUtils.framesToSeconds(44100, 44100.5))

        assertEquals(2,
                TimeUtils.framesToSeconds(44100, 22050))

        String message = shouldFail {
            TimeUtils.framesToSeconds(44100, 0)
        }
        assert message == "Frequency cannot be null"
    }

    void testSecondsToFrames() {
        assertEquals(44100,
                TimeUtils.secondsToFrames(1, 44100))

        assertEquals(0,
                TimeUtils.secondsToFrames(0, 44100.5))

        assertEquals(44100,
                TimeUtils.secondsToFrames(2, 22050))

        assertEquals(44100,
                TimeUtils.secondsToFrames(2, 22050.3))

        String message = shouldFail {
            TimeUtils.secondsToFrames(44100, 0)
        }
        assert message == "Frequency cannot be null"
    }
}
