package rom;

import rich_text.Background;
import rich_text.Color;
import rich_text.RichConsole;
import rich_text.RichTextConfig;

import java.util.List;

public class PictureMemoryPrinter implements MemoryPrinter{
    @Override
    public void print(List<MemoryChunk> memory) {
        for (MemoryChunk chunk : memory) {
            RichTextConfig rtc = RichTextConfig.builder()
                    .color(Color.BLACK)
                    .background(chunk.isFree() ? Background.GREEN : Background.RED)
                    .newLine(false)
                    .build();
            String chunkAsStars = "X".repeat(Math.max(0, (int) Math.ceil(chunk.size() / 10d)));
            RichConsole.print(String.format("%s -> %s", chunkAsStars, chunk.size()), rtc);
            RichConsole.newLine();
        }
    }
}
