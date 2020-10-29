package round_robin.tasks;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import tasks.Task;

@Getter
@Setter
@ToString
@SuperBuilder
public class RoundRobinTask extends Task {
    long burstTime;
    long remainBurstTime;
    long waitingTime;
    long turnAroundTime;


    @SuppressWarnings("unchecked")
    public <T extends RoundRobinTask> T init() {
        this.burstTime = getTimeTotal().getMillis();
        this.remainBurstTime = this.burstTime;
        return (T) this;
    }

    public long getTurnAroundTime() {
        return burstTime + waitingTime;
    }
}
