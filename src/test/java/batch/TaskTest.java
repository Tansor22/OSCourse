package batch;

import batch.tasks.Task;
import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Test
    public void taskPartsTest() {
        Task task = Task.builder()
                .part(Task.Part.defaultPart(Duration.ofSeconds(2)))
                .part(Task.Part.defaultPart(Duration.ofSeconds(3)))
                .part(Task.Part.defaultPart(Duration.ofSeconds(2)))
                .build();

        assertEquals(Duration.ofSeconds(2), task.getParts().get(0).getTime());
        assertEquals(Duration.ofSeconds(3), task.getParts().get(1).getTime());
        assertEquals(Duration.ofSeconds(2), task.getParts().get(2).getTime());
    }

    @Test
    public void taskPerformTest() {
        Task task = Task.builder()
                .part(Task.Part.defaultPart(Duration.ofSeconds(2)))
                .part(Task.Part.defaultPart(Duration.ofSeconds(3)))
                .part(Task.Part.defaultPart(Duration.ofSeconds(2)))
                .build();
    }
}