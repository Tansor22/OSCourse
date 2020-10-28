package priority_scheme;

import lombok.experimental.SuperBuilder;
import rich_text.RichConsole;
import shared.QuantizedProcessor;

import java.util.List;
import java.util.Objects;

@SuperBuilder
public class PrioritizedProcessor extends QuantizedProcessor<PrioritizedTask> {

    @Override
    public void processTasks() {
        // initializing data required
        List<PrioritizedTask> prioritizedTasks = tasks;
        long timeQuantumMillis = timeQuantum.toMillis();
        // time for current task
        long currentTime = 0;
        TaskQueue queue = new TaskQueue(prioritizedTasks);
        PrioritizedTask task;
        while (Objects.nonNull(task = queue.poll())) {
            // time of current op
            long curRemBurstTime = task.getOperations().get(task.getCurOpIndex()).getTime().toMillis();
            if (curRemBurstTime > 0) {
                if (curRemBurstTime > timeQuantum.toMillis()) {
                    currentTime += timeQuantum.toMillis();
                    // background task, should decrease priority

                    RichConsole.print(
                            "'" + getNameAndPriority(task) + "' is given a time quantum (" + timeQuantum.toString() + "). Process the task...",
                            task.getDecoration());
                    if (!task.proceed()) {
                        // task not done
                        task.setPriority(task.getPriority().prev());
                        queue.add(task);
                    }
                    // correct 'remainBurstTime' of current task
                    curRemBurstTime -= timeQuantumMillis;

                    RichConsole.print(
                            "'" + getNameAndPriority(task) + "'s' time is due, " + curRemBurstTime + " ms of burst time remains...",
                            task.getDecoration());

                    task.setRemainBurstTime(curRemBurstTime);
                    // push back
                    queue.add(task);
                } else {
                    // interactive task, should increase priority
                    if (!task.proceed()) {
                        // task not done
                        task.setPriority(task.getPriority().next());
                        queue.add(task);
                    }
                    // grab remain burst time
                    currentTime += curRemBurstTime;

                    RichConsole.print(
                            "'" + getNameAndPriority(task) + "' needs " + curRemBurstTime + " ms, it's  equal or less than time quantum (" + timeQuantum.toString() + "). Process the task...",
                            task.getDecoration());

                    // waiting time is current time minus time
                    task.setWaitingTime(currentTime - task.getBurstTime());

                    RichConsole.print(
                            "'" + task.getName() + "' is fully processed! (" + task.getBurstTime() + " ms total)",
                            task.getDecoration());

                    // mark the task as processed
                    task.setRemainBurstTime(0);
                }
            }
        }
    }

    private String getNameAndPriority(PrioritizedTask task) {
        return String.format("%s (%s priority)", task.getName(), task.getPriority());
    }
}
