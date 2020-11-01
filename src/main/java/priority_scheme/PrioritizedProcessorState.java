package priority_scheme;

import lombok.Getter;
import rich_text.RichConsole;
import tasks.DurationWrapper;
import tasks.Task;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class PrioritizedProcessorState {
    // delegated deque, always holds from 1 to 2 elements
    private final Deque<DurationWrapper> _deque = new ArrayDeque<>(2);
    Priority priority;
    PrioritizedTask coveredTask;
    @Getter
    TaskQueue taskQueue;

    public PrioritizedProcessorState(DurationWrapper initialTimeQuantum, TaskQueue taskQueue) {
        _deque.push(initialTimeQuantum);
        this.taskQueue = taskQueue;
    }

    public DurationWrapper getCurrentTimeQuantum() {
        discoverTask();
        if (_deque.size() == 1) {
            return _deque.peek();
        } else {
            return _deque.poll();
        }
    }

    public void handlePostProceed(boolean isTimeQuantumSpent, PrioritizedTask task, Task.Operation operation) {
        if (!isTimeQuantumSpent) {
            _deque.push(DurationWrapper.millis(Math.abs(operation.getRemainedBurstTime())));
        }
        if (!task.isDone()) {
            coverTask(task);
        }
    }

    /**
     * Shadows task, so the task won't be auto selected by taskQueue
     *
     * @param task  Task to be shadowed.
     */
    private void coverTask(PrioritizedTask task) {
        discoverTask();
        coveredTask = task;
        priority = task.getPriority();
        // IMPORTANT! priority shouldn't be modified after adding to taskQueue
        coveredTask.setPriority(Priority.NONE);
        taskQueue.add(coveredTask);
        RichConsole.print(coveredTask.getDecoration(), " '%s' has been covered from task processor",
                coveredTask.getName(), priority);
    }

    /**
     * Reveals task held, give its priority back to the task.
     */
    private void discoverTask() {
        if (Objects.nonNull(coveredTask)) {
            // restore task priority
            coveredTask.setPriority(priority);
            RichConsole.print(coveredTask.getDecoration(), "Priority of '%s' has been restored to %s",
                    coveredTask.getName(), priority);
            coveredTask = null;
            priority = null;
        }
    }
}
