package round_robin.tasks;

import batch.tasks.DurationWrapper;
import batch.tasks.Task;
import org.junit.Test;

import static org.junit.Assert.*;

public class RoundRobinTaskTest {

    @Test
    public void getBurstTimeSimpleTest() {
        Task task = Task.builder()
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(3)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .build();
        RoundRobinTask roundRobinTask = RoundRobinTask.builder()
                .task(task)
                .build();
        DurationWrapper timeQuantum = DurationWrapper.seconds(0);
        DurationWrapper result = roundRobinTask.getBurstTime(timeQuantum);
        assertEquals("7 seconds", result.toString());
    }

    @Test
    public void getBurstTimeComplexTest() {
        Task task = Task.builder()
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.millis(300)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .build();
        RoundRobinTask roundRobinTask = RoundRobinTask.builder()
                .task(task)
                .build();
        DurationWrapper timeQuantum = DurationWrapper.seconds(0);
        DurationWrapper result = roundRobinTask.getBurstTime(timeQuantum);
        assertEquals(4300, result.toMillis());
    }
}