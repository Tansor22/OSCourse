package rich_text;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RichTextConfig {
    private final Color color;
    private final Decoration decoration;
    private final Background background;
}
