package batch;

import batch.tasks.DurationWrapper;
import batch.tasks.Task;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Test
    public void taskPartsTest() {
        Task task = Task.builder()
                .part(Task.Part.defaultPart(DurationWrapper.seconds(2)))
                .part(Task.Part.defaultPart(DurationWrapper.seconds(3)))
                .part(Task.Part.defaultPart(DurationWrapper.seconds(2)))
                .build();

        assertEquals(DurationWrapper.seconds(2), task.getParts().get(0).getTime());
        assertEquals(DurationWrapper.seconds(3), task.getParts().get(1).getTime());
        assertEquals(DurationWrapper.seconds(2), task.getParts().get(2).getTime());
    }

    @Test
    public void taskPerformTest() {
        Task task = Task.builder()
                .part(Task.Part.defaultPart(DurationWrapper.seconds(2)))
                .part(Task.Part.defaultPart(DurationWrapper.seconds(3)))
                .part(Task.Part.defaultPart(DurationWrapper.seconds(2)))
                .build();
    }
}