package priority_scheme;

import org.junit.Test;

import java.util.Objects;

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

}