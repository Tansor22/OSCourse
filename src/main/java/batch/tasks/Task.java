package batch.tasks;

import lombok.*;
import rich_text.RichTextConfig;

import java.util.List;

@Builder
@Getter
public class Task {
    @Singular(value = "part")
    List<Part> parts;
    @Builder.Default
    int curPartIndex = 0;
    RichTextConfig decoration;

    public boolean isDone() {
        // all parts were performed
        return curPartIndex >= parts.size();
    }

    public boolean proceed() {
        curPartIndex++;
        return isDone();
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Part {
        String description;
        DurationWrapper time;

        public static Part defaultPart(DurationWrapper time) {
            return Part.builder()
                    .time(time)
                    .description("I am a chunk of bigger task, I take " + time.toString() + "...")
                    .build();
        }
    }
}
