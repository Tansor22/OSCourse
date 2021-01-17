package mem;

import mem.physical.MemoryChunk;
import mem.physical.MemoryManager;
import mem.physical.MemoryManagerImpl;
import org.junit.Test;

import java.util.ArrayList;
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
    @Test
    public void s() {
        List<String> s = new ArrayList<>();
        s.add("1");
        s.add("2");
        s.add("3");
        s.add("5");
        for (int i = 0; i < s.size(); i++) {
            String sa = s.get(i);
            if (sa.equals("2"))  {
                s.remove(sa);
            }
        }
    }
}