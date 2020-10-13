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
    @Test
    public void additionSimpleTest() {
        DurationWrapper dw1 = DurationWrapper.seconds(5);
        DurationWrapper dw2 = DurationWrapper.millis(500);
        DurationWrapper result = dw1.plus(dw2);
        assertEquals("5500 milliseconds", result.toString());
    }
    @Test
    public void additionComplexTest() {
        DurationWrapper dw1 = DurationWrapper.seconds(5);
        DurationWrapper dw2 = DurationWrapper.millis(500);
        DurationWrapper result = dw2.plus(dw1);
        assertEquals(5500, result.toMillis());
    }
    @Test
    public void subtractionSimpleTest() {
        DurationWrapper dw1 = DurationWrapper.seconds(5);
        DurationWrapper dw2 = DurationWrapper.millis(500);
        DurationWrapper result = dw1.minus(dw2);
        assertEquals("4500 milliseconds", result.toString());
    }
    @Test
    public void subtractionComplexTest() {
        DurationWrapper dw1 = DurationWrapper.seconds(5);
        DurationWrapper dw2 = DurationWrapper.millis(500);
        DurationWrapper result = dw2.minus(dw1);
        assertEquals(-4500, result.toMillis());
    }
    @Test
    public void convertTest() {
        DurationWrapper dw1 = DurationWrapper.seconds(5);
        DurationWrapper dw2 = DurationWrapper.millis(500);
        // converts '5 seconds' to millis
        DurationWrapper result = dw1.convert(dw2);
        assertEquals("5000 milliseconds", result.toString());
    }
}