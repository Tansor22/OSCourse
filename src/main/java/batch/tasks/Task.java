package batch.tasks;

import lombok.*;

import java.time.Duration;
import java.util.List;

@Builder
@Getter
@Data
public class Task {
    @Singular(value = "part")
    List<Part> parts;
    @Builder.Default
    int currentTaskIndex = 0;

    // lombok has no post construct feature
    private Task(List<Part> parts, int currentTaskIndex) {
        this.parts = parts;
        this.currentTaskIndex = currentTaskIndex;
        init();
    }

    void init() {

    }

    public boolean isDone() {
        // all parts were performed
        return currentTaskIndex >= parts.size();
    }

    public boolean proceed() {
        System.out.println("Part number #" + currentTaskIndex++ + "is done!");
        return isDone();
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Part {
        String description;
        Duration time;

        public static Part defaultPart(Duration time) {
            return Part.builder()
                    .time(time)
                    .description("I am a chunk of bigger task, I take " + time.toString() + " time units...")
                    .build();
        }
    }
}
