package mem.physical;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import mem.shared.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@SuperBuilder
@Getter
@Accessors(fluent = true, prefix = "_")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class MemoryManager implements Consumer<List<MemoryChunk>> {
    List<MemoryChunk> _memory;
    @Builder.Default
    int _taskLimit = 50;
    @Builder.Default
    float _taskAppearingPercentage = .2f;
    @Builder.Default
    IntRange _sizeRange = new IntRange(15, 175);
    @Builder.Default
    IntRange _timeRange = new IntRange(15, 175);
    @Builder.Default
    boolean _defragmentationEnabled = true;

    public final void manage() {
        accept(new ArrayList<>(_memory));
    }
}
