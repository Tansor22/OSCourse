package rom;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class MemoryConsumerTest {

    @Test
    public void memoryConsumerTest() {
        MemoryConsumer mc = PlainMemoryConsumer.builder()
                .memory(getInitialMemory())
                .build();

        mc.consume();
    }

    public List<MemoryChunk> getInitialMemory() {
        return List.of(
                MemoryChunk.builder()
                        .label("free")
                        .size(100)
                        .build()
        );
    }
}