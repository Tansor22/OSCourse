package round_robin.tasks;

import batch.tasks.DurationWrapper;
import batch.tasks.Task;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;


public class RoundRobinTask {
    @Getter
    Task task;
    DurationWrapper burstTime;

    @Builder
    public RoundRobinTask(Task task) {
        this.task = task;
    }

    public DurationWrapper getBurstTime(DurationWrapper timeQuantum) {
        if (Objects.isNull(burstTime)) {
            DurationWrapper timeTotal = task.getOperations().stream()
                    .map(Task.Operation::getTime)
                    .reduce(DurationWrapper::plus)
                    .orElse(DurationWrapper.millis(0));
            burstTime = timeTotal.convert(timeQuantum);
        }
        return burstTime;
    }
}
