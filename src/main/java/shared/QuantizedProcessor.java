package shared;

import lombok.experimental.SuperBuilder;
import tasks.DurationWrapper;
import tasks.Task;

@SuperBuilder
public abstract class QuantizedProcessor<T extends Task> extends TaskProcessor<T> {
    protected DurationWrapper initTimeQuantum;
}
