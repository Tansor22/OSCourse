package priority_scheme;

import lombok.experimental.SuperBuilder;
import rich_text.RichConsole;
import shared.QuantizedProcessor;
import tasks.DurationWrapper;
import tasks.Task;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Stack;

@SuperBuilder
public class PrioritizedProcessor extends QuantizedProcessor<PrioritizedTask> {

    @Override
    public void processTasks() {
        Stack<DurationWrapper> tqStack = new Stack<>();
        Queue<RestoringConfig> ptStack = new LinkedList<>();
        tqStack.push(timeQuantum);
        // time for current task
        long currentTime = 0;
        TaskQueue queue = new TaskQueue(tasks);
        PrioritizedTask task;
        while (Objects.nonNull(task = queue.poll())) {
            Task.Operation operation = task.getCurrentOperation();
            // time of current operation
            long curRemBurstTime = operation.getRemainedBurstTime();

            DurationWrapper timeQuantum;
            if (tqStack.size() == 1) {
                timeQuantum = tqStack.peek();
                for (RestoringConfig restoringConfig : ptStack) {
                    restoringConfig.restorePriority();
                }
            } else {
                timeQuantum = tqStack.pop();
                // should restore priority for all tasks except last pushed
                for (int i = 0; i < ptStack.size() - 1; i++) {
                    ptStack.poll().restorePriority();
                }
            }
            long timeQuantumMillis = timeQuantum.toMillis();

            if (curRemBurstTime > 0) {
                if (curRemBurstTime > timeQuantumMillis) {
                    currentTime += timeQuantumMillis;
                    // background task, should decrease priority

                    RichConsole.print(task.getDecoration(),
                            getTaskTag(task, operation) + " is given a time quantum (%s). Process the task's operation...", timeQuantum.toString());

                    if (!task.proceed(timeQuantumMillis)) {
                        // task not done
                        task.setPriority(task.getPriority().prev());
                        queue.add(task);
                    }

                    RichConsole.print(task.getDecoration(),
                            getTaskTag(task, operation) + " interrupted, %d ms of burst time remained...", operation.getRemainedBurstTime());

                } else {
                    RichConsole.print(task.getDecoration(),
                            getTaskTag(task, operation) + " needs %d ms, it's less or equals than time quantum (%s). Process the operation...",
                            operation.getRemainedBurstTime(), timeQuantum.toString());

                    if (operation.getRemainedBurstTime() < 0) {
                        // current time quantum = time quantum - remained burst time
                        tqStack.push(DurationWrapper.millis(Math.abs(operation.getRemainedBurstTime())));
                    }

                    // interactive task, should increase priority
                    if (!task.proceed(timeQuantumMillis)) {
                        // task not done
                        task.setPriority(task.getPriority().next());
                        if (operation.getRemainedBurstTime() < 0) {
                            // pushes to queue of tasks demanding restoring
                            ptStack.add(new RestoringConfig(task.getPriority(), task));
                            // the task won't be auto selected by queue
                            // IMPORTANT! priority shouldn't be modified after adding to queue
                            task.setPriority(Priority.NONE);
                        }
                        queue.add(task);
                    }

                    RichConsole.print(task.getDecoration(),
                            getTaskTag(task, operation) + " is fully processed! ( %s ms total)", operation.getBurstTime());

                }
            }
        }
    }

    private String getTaskTag(PrioritizedTask task, Task.Operation operation) {
        return String.format("'%s - %s priority : %s - %d remained burst time (ms)'",
                task.getName(), task.getPriority(), operation.getName(),
                operation.getRemainedBurstTime() < 0 ? 0 : operation.getRemainedBurstTime()
        );
    }
}
