package round_robin.tasks;

import org.junit.Test;
import tasks.DurationWrapper;
import tasks.Task;

import static org.junit.Assert.assertEquals;

public class RoundRobinTaskTest {

    @Test
    public void getBurstTimeSimpleTest() {
        RoundRobinTask roundRobinTask = RoundRobinTask.builder()
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(3)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .build().init();
        assertEquals("7000 milliseconds", DurationWrapper.millis(roundRobinTask.getTurnAroundTime()).toString());
    }

    @Test
    public void getBurstTimeComplexTest() {
        RoundRobinTask roundRobinTask = RoundRobinTask.builder()
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.millis(300)))
                .operation(Task.Operation.defaultOperation(DurationWrapper.seconds(2)))
                .build().init();
        DurationWrapper result = DurationWrapper.millis(roundRobinTask.getBurstTime());
        assertEquals(4300, result.toMillis());
    }
}