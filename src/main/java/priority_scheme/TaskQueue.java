package priority_scheme;

import lombok.Getter;

import java.util.*;

@Getter
public class TaskQueue extends AbstractQueue<PrioritizedTask> {
    Queue<PrioritizedTask> _queue;

    public TaskQueue(List<PrioritizedTask> tasks) {
        this();
        _queue.addAll(tasks);
    }

    public TaskQueue() {
        _queue = new PriorityQueue<>(
                        Comparator.comparingInt((PrioritizedTask task) -> task.getPriority().getIntValue()).reversed()
                );
    }

    @Override
    public Iterator<PrioritizedTask> iterator() {
        return _queue.iterator();
    }

    @Override
    public int size() {
        return _queue.size();
    }

    @Override
    public boolean offer(PrioritizedTask prioritizedTask) {
        return _queue.offer(prioritizedTask);
    }

    @Override
    public PrioritizedTask poll() {
        return _queue.poll();
    }

    @Override
    public PrioritizedTask peek() {
        return _queue.peek();
    }
}
