package batch;

import org.junit.Test;
import tasks.DurationWrapper;
import tasks.Task;

import static org.junit.Assert.assertEquals;

public class TaskTest {

    @Test
    public void taskPartsTest() {
        Task task = Task.builder()
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(3)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .build();

        assertEquals(DurationWrapper.seconds(2), task.getOperations().get(0).getTime());
        assertEquals(DurationWrapper.seconds(3), task.getOperations().get(1).getTime());
        assertEquals(DurationWrapper.seconds(2), task.getOperations().get(2).getTime());
    }
}