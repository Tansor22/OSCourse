package batch;

import batch.tasks.Task;
import org.junit.Test;

import static org.junit.Assert.*;

public class BatchProcessorTest {

    @Test
    public void generateIndexesTest() {
        BatchProcessor batchProcessor = BatchProcessor.builder()
                .task(Task.builder().build())
                .task(Task.builder().build())
                .task(Task.builder().build())
                .task(Task.builder().build())
                .build();
        int excludeIndex = 2;
        int[] indexes = batchProcessor.generateIndexesExclude(batchProcessor.getTasks().size(), excludeIndex);
        assertArrayEquals(new int[] {
                0, 1, 3
        }, indexes);
    }

    @Test
    public void selectSingleTaskTest() {
        BatchProcessor batchProcessor = BatchProcessor.builder()
                .task(Task.builder().build())
                .build();
        Task task = batchProcessor.selectTask(batchProcessor.getTasks());
        assertEquals("The task hasn't been selected", batchProcessor.getTasks().get(0), task);
        assertEquals("Current task index has been changed", 0, batchProcessor.getCurTaskIndex());
    }
    @Test
    public void selectMultipleTasksTest() {
        BatchProcessor batchProcessor = BatchProcessor.builder()
                .task(Task.builder().build())
                .task(Task.builder().build())
                .build();
        int initIndex = batchProcessor.getCurTaskIndex();
        Task firstTask = batchProcessor.selectTask(batchProcessor.getTasks());
        int firstTaskIndex = batchProcessor.getCurTaskIndex();
        assertNotEquals("Init index and first selected index are the same", firstTaskIndex, initIndex);
        Task secondTask = batchProcessor.selectTask(batchProcessor.getTasks());
        int secondTaskIndex = batchProcessor.getCurTaskIndex();
        assertNotSame("The same task was selected", firstTask, secondTask);
        assertNotEquals("The same indexes of the tasks", firstTaskIndex, secondTaskIndex);
        assertEquals("Init index and second index are closed", secondTaskIndex, initIndex);
    }
}