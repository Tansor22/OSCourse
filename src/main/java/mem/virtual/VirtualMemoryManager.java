package mem.virtual;

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
public abstract class VirtualMemoryManager implements Consumer<List<Process>> {
    @Builder.Default
    int _lifetime = 100;
    @Builder.Default
    int _pagesCount = 35;
    @Builder.Default
    int _processesLimit = 7;
    @Builder.Default
    float _processAppearingPercentage = .3f;
    @Builder.Default
    float _memoryOccupiedThreshold = .6f;
    @Builder.Default
    IntRange _processPagesCountRange = new IntRange(6, 13);
    @Builder.Default
    IntRange _processLifetimeRange = new IntRange(6, 13);



    public final void manage() {
        accept(new ArrayList<>());
    }
}
