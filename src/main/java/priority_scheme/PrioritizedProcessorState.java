package priority_scheme;

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

    public PrioritizedProcessorState(DurationWrapper initialTimeQuantum) {
        _deque.push(initialTimeQuantum);
    }

    public DurationWrapper getCurrentTimeQuantum() {
        if (_deque.size() == 1) {
            discoverTask();
            return _deque.peek();
        } else {
            return _deque.poll();
        }
    }

    public void handlePostProceed(PrioritizedTask task, Task.Operation operation, TaskQueue queue) {
        boolean isNotAllTimeSpent = operation.getRemainedBurstTime() < 0;
        if (isNotAllTimeSpent) {
            _deque.push(DurationWrapper.millis(Math.abs(operation.getRemainedBurstTime())));
        }
        if (!task.isDone()) {
            if (isNotAllTimeSpent) {
                coverTask(task, queue);
            } else {
                queue.add(task);
            }
        }
    }

    /**
     * Shadows task, so the task won't be auto selected by queue
     *
     * @param task  Task to be shadowed.
     * @param queue Queue to hold the task.
     */
    private void coverTask(PrioritizedTask task, TaskQueue queue) {
        discoverTask();
        coveredTask = task;
        priority = task.getPriority();
        // IMPORTANT! priority shouldn't be modified after adding to queue
        coveredTask.setPriority(Priority.NONE);
        queue.add(coveredTask);
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
