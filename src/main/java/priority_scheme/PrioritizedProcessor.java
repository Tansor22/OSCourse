package priority_scheme;

import lombok.experimental.SuperBuilder;
import shared.QuantizedProcessor;
import tasks.DurationWrapper;
import tasks.Task;

import java.util.Objects;

import static rich_text.RichConsole.print;

@SuperBuilder
public class PrioritizedProcessor extends QuantizedProcessor<PrioritizedTask> {

    @Override
    public void processTasks() {
        // functional element of priority scheme
        PrioritizedProcessorState state = new PrioritizedProcessorState(initTimeQuantum, new TaskQueue(tasks));
        // total time of work
        long currentTime = 0;
        PrioritizedTask task;
        while (Objects.nonNull(task = state.getTaskQueue().poll())) {
            // current time
            DurationWrapper timeQuantum = state.getCurrentTimeQuantum();
            long timeQuantumMillis = timeQuantum.getMillis();
            // current operation time
            Task.Operation operation = task.getCurrentOperation();
            long curRemBurstTime = operation.getRemainedBurstTime();
            boolean isTimeQuantumSpent = curRemBurstTime >= timeQuantumMillis;
            if (curRemBurstTime > timeQuantumMillis) {
                currentTime += timeQuantumMillis;

                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " is given a time quantum (%s). Process the task's operation...", timeQuantum.toString());

                if (!task.proceed(timeQuantumMillis)) {
                    // task not done
                    // background task, should decrease priority
                    task.setPriority(task.getPriority().prev());
                }

                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " interrupted, %d ms of burst time remained...", operation.getRemainedBurstTime());

            } else {
                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " needs %d ms, it's less or equals than time quantum (%s). Process the operation...",
                        operation.getRemainedBurstTime(), timeQuantum.toString());

                currentTime += curRemBurstTime;

                if (!task.proceed(timeQuantumMillis)) {
                    task.setPriority(task.getPriority().next());
                }


                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " is fully processed! ( %s ms total)", operation.getBurstTime());

            }
            state.handlePostProceed(isTimeQuantumSpent, task, operation);
        }
    }

    private String getTaskTag(PrioritizedTask task, Task.Operation operation, PrioritizedProcessorState state) {
        return String.format("'%s - %s priority : %s - %d remained burst time (ms)'",
                task.getName(),
                state.coveredTask == task ? state.priority : task.getPriority(),
                operation.getName(),
                operation.getRemainedBurstTime() < 0 ? 0 : operation.getRemainedBurstTime()
        );
    }
}
