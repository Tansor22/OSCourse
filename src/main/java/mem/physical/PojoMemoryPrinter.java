package mem.physical;

import rich_text.Color;
import rich_text.RichConsole;
import rich_text.RichTextConfig;

import java.util.List;

public class PojoMemoryPrinter implements MemoryPrinter {
    @Override
    public void print(List<MemoryChunk> memory) {
        for (MemoryChunk chunk : memory) {
            RichTextConfig rtc = RichTextConfig.builder()
                    .color(chunk.isFree() ? Color.GREEN : Color.RED)
                    .newLine(false)
                    .build();
            RichConsole.print(String.format("{%s - size: %s, timeRemain: %s} ",
                    chunk.label(), chunk.size(), chunk.timeRemain()), rtc);
            RichConsole.newLine();
        }
    }
}
