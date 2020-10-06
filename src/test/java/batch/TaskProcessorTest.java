package batch;

import batch.tasks.Task;
import org.junit.Test;

import static org.junit.Assert.*;

public class TaskProcessorTest {

    @Test
    public void generateIndexesTest() {
        TaskProcessor taskProcessor = TaskProcessor.builder()
                .task(Task.builder().build())
                .task(Task.builder().build())
                .task(Task.builder().build())
                .task(Task.builder().build())
                .build();
        int excludeIndex = 2;
        int[] indexes = taskProcessor.generateIndexesExclude(taskProcessor.getTasks().size(), excludeIndex);
        assertArrayEquals(new int[] {
                0, 1, 3
        }, indexes);
    }

    @Test
    public void selectSingleTaskTest() {
        TaskProcessor taskProcessor = TaskProcessor.builder()
                .task(Task.builder().build())
                .build();
        Task task = taskProcessor.selectTask(taskProcessor.getTasks());
        assertEquals("The task hasn't been selected", taskProcessor.getTasks().get(0), task);
        assertEquals("Current task index has been changed", 0, taskProcessor.getCurTaskIndex());
    }
    @Test
    public void selectMultipleTasksTest() {
        TaskProcessor taskProcessor = TaskProcessor.builder()
                .task(Task.builder().build())
                .task(Task.builder().build())
                .build();
        int initIndex = taskProcessor.getCurTaskIndex();
        Task firstTask = taskProcessor.selectTask(taskProcessor.getTasks());
        int firstTaskIndex = taskProcessor.getCurTaskIndex();
        assertNotEquals("Init index and first selected index are the same", firstTaskIndex, initIndex);
        Task secondTask = taskProcessor.selectTask(taskProcessor.getTasks());
        int secondTaskIndex = taskProcessor.getCurTaskIndex();
        assertNotSame("The same task was selected", firstTask, secondTask);
        assertNotEquals("The same indexes of the tasks", firstTaskIndex, secondTaskIndex);
        assertEquals("Init index and second index are closed", secondTaskIndex, initIndex);
    }
}