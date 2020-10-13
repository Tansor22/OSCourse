package round_robin.tasks;

import batch.tasks.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoundRobinTask {
    Task task;
    long burstTime;
    long remainBurstTime;
    long waitingTime;
    long turnAroundTime;

    @Builder
    public RoundRobinTask(Task task) {
        this.task = task;
        this.burstTime = task.getTimeTotal().toMillis();
        this.remainBurstTime = this.burstTime;
    }

    public long getTurnAroundTime() {
        return burstTime + waitingTime;
    }
}
