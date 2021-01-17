package mem.physical;

import mem.virtual.Page;
import rich_text.Background;
import rich_text.Color;
import rich_text.RichConsole;
import rich_text.RichTextConfig;

import java.util.List;

public interface MemoryPrinter {
    void print(final List<MemoryChunk> memory);

    default void printVirtual(final List<Page> virtualMemory) {
        for (Page page : virtualMemory) {
            RichTextConfig rtc = RichTextConfig.builder()
                    .color(Color.BLACK)
                    .background(page.isOccupied() ? Background.RED : Background.GREEN)
                    .newLine(false)
                    .build();
            RichConsole.print(rtc, " %s |", page.address());
        }
        RichConsole.newLine();
    }
}
