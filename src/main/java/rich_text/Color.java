package rich_text;

public enum Color implements AnsiEscapeCode {
    BLACK("\u001b[30m"),
    RED("\u001b[31m"),
    GREEN("\u001b[32m"),
    YELLOW("\u001b[33m"),
    BLUE("\u001b[34m"),
    MAGENTA("\u001b[35m"),
    CYAN("\u001b[36m"),
    WHITE("\u001b[37m");
    private final String ansi;

    Color(String ansi) {
        this.ansi = ansi;
    }

    @Override
    public String getEscapeCode() {
        return ansi;
    }
}
