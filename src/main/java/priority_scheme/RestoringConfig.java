package priority_scheme;

import lombok.AllArgsConstructor;
import rich_text.RichConsole;

@AllArgsConstructor
public class RestoringConfig {
    Priority priority;
    PrioritizedTask task;

    public void restorePriority() {
        task.setPriority(priority);
        RichConsole.print(task.getDecoration(), "Priority of '%s' has been restored to %s",
                task.getName(), priority);
    }
}
