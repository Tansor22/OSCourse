package rich_text;

import batch.BatchConstants;

import java.util.Optional;

public class RichConsole {

    private final static String RESET_ANSI = "\u001b[0m";

    public static void print(String message, RichTextConfig config) {
        StringBuilder target = new StringBuilder();
        Optional.ofNullable(config)
                .ifPresent(self ->
                        target
                                // color
                                .append(Optional.ofNullable(self.getColor())
                                        .map(Color::getEscapeCode)
                                        .orElse(BatchConstants.EMPTY_STRING))
                                // decoration
                                .append(Optional.ofNullable(self.getDecoration())
                                        .map(Decoration::getEscapeCode)
                                        .orElse(BatchConstants.EMPTY_STRING))
                                // background
                                .append(Optional.ofNullable(self.getBackground())
                                        .map(Background::getEscapeCode)
                                        .orElse(BatchConstants.EMPTY_STRING))
                );
        target.append(message)
                .append(RESET_ANSI);
        System.out.println(target.toString());
    }
}
