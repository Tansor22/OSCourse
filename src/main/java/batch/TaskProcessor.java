package batch;

import batch.tasks.Task;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
            Task.Part currentPart = task.getParts().get(task.getCurrentTaskIndex());
            // performing task part
            System.out.println("Task number #" + curTaskIndex + ": " + currentPart.getDescription());
            try {
                Thread.sleep(currentPart.getTime().toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // task part performed
            if (task.proceed()) {
                System.out.println("Task number #" + curTaskIndex + " is done!");
                mutableTasks.remove(curTaskIndex);
                curTaskIndex = BatchConstants.PLAIN_RND_SELECTION;
                System.out.println("Switching context...");
            } else {
                System.out.println("Interruption occurred..");
            }
        }
    }

    private boolean noSpecialSelectionRequired () {
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
