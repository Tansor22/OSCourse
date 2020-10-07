package rich_text;

public enum Background implements AnsiEscapeCode{
    BLACK("\u001b[40m"),
    RED("\u001b[41m"),
    GREEN("\u001b[42m"),
    YELLOW("\u001b[43m"),
    BLUE("\u001b[44m"),
    MAGENTA("\u001b[45m"),
    CYAN("\u001b[46m"),
    WHITE("\u001b[47m");
    private final String ansi;

    Background(String ansi) {
        this.ansi = ansi;
    }

    @Override
    public String getEscapeCode() {
        return ansi;
    }
}
