package rich_text;

public enum Decoration implements AnsiEscapeCode{

    BOLD("\u001b[1m"),
    UNDERLINE("\u001b[4m"),
    REVERSED("\u001b[7m");
    private final String ansi;

    Decoration(String ansi) {
        this.ansi = ansi;
    }

    @Override
    public String getEscapeCode() {
        return ansi;
    }
}
