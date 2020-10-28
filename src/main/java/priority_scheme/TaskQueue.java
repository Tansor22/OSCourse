package priority_scheme;

import lombok.Getter;

import java.util.*;

@Getter
public class TaskQueue extends AbstractQueue<PrioritizedTask> {
    Queue<PrioritizedTask> queue;

    public TaskQueue(List<PrioritizedTask> tasks) {
        this();
        queue.addAll(tasks);
    }

    public TaskQueue() {
        queue = new PriorityQueue<>(
                        Comparator.comparingInt((PrioritizedTask task) -> task.getPriority().getIntValue()).reversed()
                );
    }

    @Override
    public Iterator<PrioritizedTask> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }

    @Override
    public boolean offer(PrioritizedTask prioritizedTask) {
        return queue.offer(prioritizedTask);
    }

    @Override
    public PrioritizedTask poll() {
        return queue.poll();
    }

    @Override
    public PrioritizedTask peek() {
        return queue.peek();
    }
}
