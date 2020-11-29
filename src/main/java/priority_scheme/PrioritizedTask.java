package priority_scheme;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import round_robin.tasks.RoundRobinTask;
import tasks.Task;

import java.util.function.ToLongFunction;

@Getter
@Setter
@SuperBuilder
public class PrioritizedTask extends RoundRobinTask {
    Priority priority;

    private long accumulateOperationsProperty(ToLongFunction<Task.Operation> operationMapper) {
        return getOperations().stream()
                .mapToLong(operationMapper)
                .sum();
    }

    @Override
    public long getWaitingTime() {
        return accumulateOperationsProperty(Task.Operation::getWaitingTime);
    }

    @Override
    public long getTurnAroundTime() {
        return getWaitingTime() + getBurstTime();
    }

    @Override
    public long getBurstTime() {
        return accumulateOperationsProperty(Task.Operation::getBurstTime);
    }

    @Override
    public String toString() {
        return "PrioritizedTask{" +
                "name=" + getName() + ", " +
                "priority=" + priority +
                '}';
    }
}
