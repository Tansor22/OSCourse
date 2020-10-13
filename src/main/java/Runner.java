import batch.BatchProcessor;
import batch.tasks.DurationWrapper;
import batch.tasks.Task;
import rich_text.Color;
import rich_text.RichTextConfig;
import round_robin.RoundRobinProcessor;
import round_robin.tasks.RoundRobinTask;

import static batch.tasks.Task.Operation.*;

public class Runner {
    public static void main(String[] args) {
        RoundRobinProcessor.builder()
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
                .timeQuantum(DurationWrapper.seconds(3))
                .build().processTasks();
    }
}
