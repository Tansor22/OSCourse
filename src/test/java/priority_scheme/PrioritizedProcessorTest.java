package priority_scheme;

import org.junit.Test;
import rich_text.Color;
import rich_text.RichTextConfig;
import tasks.DurationWrapper;

import static priority_scheme.Priority.*;
import static tasks.Task.Operation.*;

public class PrioritizedProcessorTest {

    @Test
    public void processTasks() {
        PrioritizedProcessor.builder()
                .task(PrioritizedTask.builder()
                        .priority(HIGH)
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
                .task(PrioritizedTask.builder()
                        .priority(NORMAL)
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
                .task(PrioritizedTask.builder()
                        .priority(LOW)
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
                .initTimeQuantum(DurationWrapper.millis(300))
                .build().processTasksTraceable();
    }
}