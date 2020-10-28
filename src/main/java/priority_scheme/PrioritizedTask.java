package priority_scheme;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import round_robin.tasks.RoundRobinTask;

@Getter
@Setter
@SuperBuilder
public class PrioritizedTask extends RoundRobinTask {
    Priority priority;

    @Override
    public String toString() {
        return "PrioritizedTask{" +
                "name=" +  getName() +", "+
                "priority=" + priority +
                '}';
    }
}
