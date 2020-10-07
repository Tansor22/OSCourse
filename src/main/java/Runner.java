import batch.TaskProcessor;
import batch.tasks.DurationWrapper;
import batch.tasks.Task;
import rich_text.*;

import java.time.Duration;

public class Runner {
    public static void main(String[] args) {
        TaskProcessor.builder()
                .task(Task.builder()
                        .part(Task.Part.defaultPart(DurationWrapper.millis(500)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(300)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(200)))
                        .build())
                .task(Task.builder()
                        .part(Task.Part.defaultPart(DurationWrapper.millis(500)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(300)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(200)))
                        .build())
                .task(Task.builder()
                        .part(Task.Part.defaultPart(DurationWrapper.millis(500)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(300)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(200)))
                        .build())
                .task(Task.builder()
                        .decoration(RichTextConfig.builder()
                                .color(Color.BLACK)
                                //.background(Background.WHITE)
                                .decoration(Decoration.UNDERLINE)
                                .build())
                        .part(Task.Part.defaultPart(DurationWrapper.millis(500)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(300)))
                        .part(Task.Part.defaultPart(DurationWrapper.millis(200)))
                        .build())
                .build().processTasks();
    }
}
