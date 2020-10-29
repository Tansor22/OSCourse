package round_robin;

import org.junit.Test;
import rich_text.Color;
import rich_text.RichTextConfig;
import round_robin.tasks.RoundRobinTask;
import tasks.DurationWrapper;

import static tasks.Task.Operation.*;

public class RoundRobinProcessorTest {

    @Test
    public void processTasks() {
        RoundRobinProcessor.builder()
                .task(RoundRobinTask.builder()
                        .name("Browser Google Tab")
                        .decoration(RichTextConfig.builder()
                                .color(Color.YELLOW)
                                // add decorations here ...
                                .build())
                        .operation(guiOperation("Showing main app's form", DurationWrapper.seconds(1)))
                        .operation(networkOperation("Requesting www.google.com", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Preparing response", DurationWrapper.millis(700)))
                        .operation(cleanUpOperation("Disposing socket connection", DurationWrapper.millis(300)))
                        .build()
                        .init())
                .task(RoundRobinTask.builder()
                        .name("Browser Facebook Tab")
                        .decoration(RichTextConfig.builder()
                                .color(Color.BLUE)
                                // add decorations here ...
                                .build())
                        .operation(networkOperation("Requesting www.facebook.com", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Preparing response", DurationWrapper.millis(500)))
                        .operation(cleanUpOperation("Disposing socket connection", DurationWrapper.millis(500)))
                        .build()
                        .init())
                .task(RoundRobinTask.builder()
                        .name("Bitcoin Mining")
                        .decoration(RichTextConfig.builder()
                                .color(Color.RED)
                                // add decorations here ...
                                .build())
                        .operation(calculationOperation("Hash finding...", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Hash finding, second try...", DurationWrapper.seconds(2)))
                        .operation(calculationOperation("Hash finding, third try...", DurationWrapper.millis(500)))
                        .operation(cleanUpOperation("Disposing some memory...", DurationWrapper.millis(500)))
                        .build()
                        .init())
                .initTimeQuantum(DurationWrapper.seconds(3))
                .build().processTasksTraceable();
    }
}