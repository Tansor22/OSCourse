package rom;

import org.junit.Test;

import java.util.List;

public class MemoryConsumerTest {

    @Test
    public void memoryConsumerWithDefragmentationTest() {
        MemoryConsumer mc = MemoryConsumerImpl.builder()
                .memory(getInitialMemory())
                .build();

        mc.consume();
    }

    @Test
    public void memoryConsumerWithoutDefragmentationTest() {
        MemoryConsumer mc = MemoryConsumerImpl.builder()
                .memory(getInitialMemory())
                .defragmentationEnabled(false)
                .build();

        mc.consume();
    }

    public List<MemoryChunk> getInitialMemory() {
        return List.of(
                MemoryChunk.builder()
                        .label("free")
                        .size(1_000)
                        .build()
        );
    }
}