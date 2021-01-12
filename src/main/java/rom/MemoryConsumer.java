package rom;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuperBuilder
@Getter
@Accessors(fluent = true, prefix = "_")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class MemoryConsumer implements Consumer<List<MemoryChunk>> {
    List<MemoryChunk> _memory;
    @Builder.Default
    int _taskLimit = 50;

    public final void consume() {
        accept(new ArrayList<>(_memory));
    }
}
