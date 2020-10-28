package priority_scheme;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;
import static priority_scheme.Priority.*;

public class TaskQueueTest {

    @Test
    public void queueTest() {
        TaskQueue queue = new TaskQueue();
        for (Priority priority : Priority.values()) {
            PrioritizedTask task = PrioritizedTask.builder()
                    .priority(priority)
                    .name("Some task with priority " + priority.name())
                    .build();
            queue.add(task);
        }
        Priority priority = Priority.HIGH;
        PrioritizedTask anotherTask = PrioritizedTask.builder()
                .priority(priority)
                .name("Another task with priority " + priority.name())
                .build();
        queue.add(anotherTask);
        for (int i = 0; i < 3; i++) {
            anotherTask = PrioritizedTask.builder()
                    .priority(priority)
                    .name("Another " + i + " task with priority " + priority.name())
                    .build();
            queue.add(anotherTask);
        }
        PrioritizedTask task;
        while (Objects.nonNull(task = queue.poll())) {
            System.out.println("Processing - " + task.getName());
            if (task.priority == Priority.HIGH) {
                task.priority = Priority.LOW;
                queue.add(task);
            }
        }
    }

    @Test
    public void nonePriorityTest() {
        TaskQueue queue = new TaskQueue();
        for (Priority priority : VALUES) {
            queue.add(PrioritizedTask.builder()
                    .priority(priority)
                    .name(priority + " priority task")
                    .build()
                    .init());
        }
        Priority[] expectedOrdering =
                new Priority[]{HIGH, NORMAL, LOW, NONE};

        assertArrayEquals("Queue isn't sorted correctly.",
                queue.getQueue().stream()
                        .map(PrioritizedTask::getPriority)
                        .toArray(Priority[]::new), expectedOrdering);

        // shouldn't modify priority of element, after it has been added to queue
        PrioritizedTask task = queue.poll();
        queue.add(task);
        assertNotNull("Queue doesn't contain elements.", task);
        task.setPriority(NONE);

        assertNotEquals("Queue ordering wasn't broken.",
                queue.getQueue().stream()
                        .map(PrioritizedTask::getPriority)
                        .toArray(Priority[]::new), expectedOrdering);
    }
}