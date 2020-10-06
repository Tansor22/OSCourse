package rich_text;

public class RichConsole {

    public static void print(String message, Color color, Decoration decoration) {
        System.out.println(
                RichText.builder()
                        .color(color)
                        .decoration(decoration)
                        .text(message)
                        .build()
        );
    }
}
