package batch.tasks;

import org.junit.Test;

import static org.junit.Assert.*;

public class DurationWrapperTest {

    @Test
    public void millisTest() {
        DurationWrapper dw1 = DurationWrapper.millis(5);
        DurationWrapper dw2 = DurationWrapper.millis(1);
        assertEquals("5 milliseconds", dw1.toString());
        assertEquals("1 millisecond", dw2.toString());
    }

    @Test
    public void secondsTest() {

        DurationWrapper dw1 = DurationWrapper.seconds(5);
        DurationWrapper dw2 = DurationWrapper.seconds(1);
        assertEquals("5 seconds", dw1.toString());
        assertEquals("1 second", dw2.toString());
    }
}