import batch.TaskProcessor;
import batch.tasks.Task;
import rich_text.Color;
import rich_text.Decoration;
import rich_text.RichConsole;

import java.time.Duration;

public class Runner {
    public static void main(String[] args) {
        TaskProcessor.builder()
                .task(Task.builder()
                        .part(Task.Part.defaultPart(Duration.ofMillis(500)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(300)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(200)))
                        .build())
                .task(Task.builder()
                        .part(Task.Part.defaultPart(Duration.ofMillis(500)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(300)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(200)))
                        .build())
                .task(Task.builder()
                        .part(Task.Part.defaultPart(Duration.ofMillis(500)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(300)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(200)))
                        .build())
                .task(Task.builder()
                        .part(Task.Part.defaultPart(Duration.ofMillis(500)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(300)))
                        .part(Task.Part.defaultPart(Duration.ofMillis(200)))
                        .build())
                .build().processTasks();
    }
}
