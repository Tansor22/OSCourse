package rom;

import org.junit.Test;

import java.util.List;

public class MemoryConsumerTest {

    @Test
    public void withDefragmentation() {
        MemoryConsumer mc = MemoryConsumerImpl.builder()
                .memory(getInitialMemory())
                .taskAppearingPercentage(.5f)
                .build();

        mc.consume();
    }

    @Test
    public void withoutDefragmentation() {
        MemoryConsumer mc = MemoryConsumerImpl.builder()
                .memory(getInitialMemory())
                .taskAppearingPercentage(.5f)
                .defragmentationEnabled(false)
                .build();

        mc.consume();
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