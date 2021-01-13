package mem;

import mem.physical.MemoryChunk;
import mem.physical.MemoryManager;
import mem.physical.MemoryManagerImpl;
import org.junit.Test;

import java.util.List;

public class MemoryManagerTest {

    @Test
    public void withDefragmentation() {
        MemoryManager mc = MemoryManagerImpl.builder()
                .memory(getInitialMemory())
                .taskAppearingPercentage(.5f)
                .build();

        mc.manage();
    }

    @Test
    public void withoutDefragmentation() {
        MemoryManager mc = MemoryManagerImpl.builder()
                .memory(getInitialMemory())
                .taskAppearingPercentage(.5f)
                .defragmentationEnabled(false)
                .build();

        mc.manage();
    }

    public List<MemoryChunk> getInitialMemory() {
        return List.of(
                MemoryChunk.builder()
                        .label("free")
                        .size(500)
                        .build()
        );
    }
}