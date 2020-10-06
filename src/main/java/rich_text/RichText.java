package rich_text;

import lombok.Builder;

@Builder
public class RichText {
    private final Color color;
    private final Decoration decoration;
    private final String text;

    @Override
    public String toString() {
        return color.getEscapeCode() + decoration.getEscapeCode() + text + Color.RESET.getEscapeCode();
    }
}
