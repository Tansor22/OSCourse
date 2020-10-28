package batch;

import org.junit.Test;
import rich_text.Color;
import rich_text.RichTextConfig;
import tasks.DurationWrapper;
import tasks.Task;

import static org.junit.Assert.*;
import static tasks.Task.Operation.*;

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
        assertEquals("Init index doesn't indicate of plain random selection.", BatchConstants.PLAIN_RND_SELECTION, initIndex);
        int firstTaskIndex = batchProcessor.getCurTaskIndex();
        assertNotEquals("No task was chosen to process", firstTaskIndex, BatchConstants.PLAIN_RND_SELECTION);
        Task secondTask = batchProcessor.selectTask(batchProcessor.getTasks());
        int secondTaskIndex = batchProcessor.getCurTaskIndex();
        assertNotSame("The same task was selected", firstTask, secondTask);
        assertNotEquals("The same indexes of the tasks", firstTaskIndex, secondTaskIndex);
    }

    @Test
    public void processTasks() {
        BatchProcessor.builder()
                .task(Task.builder()
                        .name("Browser Google Tab")
                        .decoration(RichTextConfig.builder()
                                .color(Color.YELLOW)
                                // add decorations here ...
                                .build())
                        .operation(guiOperation("Showing main app's form", DurationWrapper.seconds(1)))
                        .operation(networkOperation("Requesting www.google.com", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Preparing response", DurationWrapper.millis(700)))
                        .operation(cleanUpOperation("Disposing socket connection", DurationWrapper.millis(300)))
                        .build())
                .task(Task.builder()
                        .name("Browser Facebook Tab")
                        .decoration(RichTextConfig.builder()
                                .color(Color.BLUE)
                                // add decorations here ...
                                .build())
                        .operation(networkOperation("Requesting www.facebook.com", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Preparing response", DurationWrapper.millis(500)))
                        .operation(cleanUpOperation("Disposing socket connection", DurationWrapper.millis(500)))
                        .build())
                .task(Task.builder()
                        .name("Bitcoin Mining")
                        .decoration(RichTextConfig.builder()
                                .color(Color.RED)
                                // add decorations here ...
                                .build())
                        .operation(calculationOperation("Hash finding...", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Hash finding, second try...", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Hash finding, third try...", DurationWrapper.millis(500)))
                        .operation(cleanUpOperation("Disposing some memory...", DurationWrapper.millis(500)))
                        .build())
                .build().processTasksTraceable();
    }
}