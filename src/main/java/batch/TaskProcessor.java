package batch;

import batch.tasks.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import rich_text.RichConsole;
import rich_text.RichTextConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Builder
@Getter
public class TaskProcessor {
    @Singular(value = "task")
    List<Task> tasks;
    @Builder.Default
    int curTaskIndex = BatchConstants.PLAIN_RND_SELECTION;

    public void processTasks() {
        List<Task> mutableTasks = new ArrayList<>(tasks);
        while (!mutableTasks.isEmpty()) {
            // getting task
            Task task = selectTask(mutableTasks);
            Task.Operation operation = task.getOperations().get(task.getCurOpIndex());
            // performing task part
            RichConsole.print("Switched to task '" + task.getName() + "': '" + operation.getName() + "' - " + operation.getOperationDescription(), task.getDecoration());
            perform(operation);
            // task part performed
            RichConsole.print("'" + operation.getName() + "' of '" + task.getName() + "' is done!", task.getDecoration());
            if (task.proceed()) {
                RichConsole.print("'" + task.getName() + "' is done!", task.getDecoration());
                postTaskProcessed(mutableTasks);
            } else {
                RichConsole.print("Interruption occurred: " + operation.getInterruptionDescription(), task.getDecoration());
            }
        }
        RichConsole.print("Tasks performed - " + tasks.stream()
                .map(Task::getName)
                .collect(Collectors.joining(", ")), RichTextConfig.builder().build());
    }

    private void postTaskProcessed(List<Task> tasks) {
        tasks.remove(curTaskIndex);
        curTaskIndex = BatchConstants.PLAIN_RND_SELECTION;
    }

    private static void perform(Task.Operation operation) {
        try {
            Thread.sleep(operation.getTime().toMillis());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean noSpecialSelectionRequired() {
        return curTaskIndex == BatchConstants.PLAIN_RND_SELECTION;
    }

    /**
     * @return
     */
    public Task selectTask(List<Task> tasks) {
        Random r = new Random(System.currentTimeMillis());
        if (noSpecialSelectionRequired()) {
            curTaskIndex = r.nextInt(tasks.size());
        } else {
            int[] indexes = generateIndexesExclude(tasks.size(), curTaskIndex);
            curTaskIndex = indexes[r.nextInt(indexes.length)];
        }
        return tasks.get(curTaskIndex);
    }

    /**
     * @param indexToExclude
     * @return
     */
    public int[] generateIndexesExclude(int threshold, int indexToExclude) {
        int[] indexes = new int[threshold - 1];
        for (int i = 0, j = 0; i < threshold; i++) {
            if (i != indexToExclude) {
                indexes[j++] = i;
            }
        }
        return indexes;
    }
}
