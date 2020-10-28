package shared;

import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import rich_text.*;
import tasks.Task;

import java.util.List;

@SuperBuilder
public abstract class TaskProcessor<T extends Task> {
    @Singular(value = "task")
    @Getter
    protected List<T> tasks;

    public abstract void processTasks();

    public void processTasksTraceable() {
        RichTextConfig richTextConfig = RichTextConfig.builder()
                .background(Background.BLACK)
                .color(Color.WHITE)
                .decoration(Decoration.BOLD)
                .build();
        String processorName = getClass().getSimpleName();
        RichConsole.print(richTextConfig, "%s has started task processing...", processorName);
        processTasks();
        RichConsole.print(richTextConfig, "%s has finished task processing...", processorName);
    }
}
