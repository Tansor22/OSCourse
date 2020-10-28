package tasks;

import lombok.*;
import lombok.experimental.SuperBuilder;
import rich_text.RichTextConfig;

import java.util.List;

@SuperBuilder
@Getter
@NoArgsConstructor
@ToString
public class Task {
    String name;
    @Singular(value = "operation")
    List<Operation> operations;
    @Builder.Default
    int curOpIndex = 0;
    RichTextConfig decoration;

    public boolean isDone() {
        // all parts were performed
        return curOpIndex >= operations.size();
    }

    public boolean proceed() {
        curOpIndex++;
        return isDone();
    }
    public DurationWrapper getTimeTotal() {
        return operations.stream()
                .map(Task.Operation::getTime)
                .reduce(DurationWrapper::plus)
                .orElse(DurationWrapper.millis(0));
    }
    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Operation {
        String name;
        String operationDescription;
        String interruptionDescription;
        DurationWrapper time;

        public static Operation defaultOperation(DurationWrapper time) {
            return Operation.builder()
                    .name("Some calculations...")
                    .time(time)
                    .interruptionDescription("Waiting for a wonder..")
                    .operationDescription("I am a chunk of bigger task, I take " + time.toString() + "...")
                    .build();
        }

        public static Operation networkOperation(String partDescription, DurationWrapper time) {
            return Operation.builder()
                    .name("Network operation")
                    .time(time)
                    .interruptionDescription("Waiting for a remote host...")
                    .operationDescription(partDescription)
                    .build();
        }

        public static Operation calculationOperation(String partDescription, DurationWrapper time) {
            return Operation.builder()
                    .name("Calculation operation")
                    .time(time)
                    .interruptionDescription("Requesting for extra data...")
                    .operationDescription(partDescription)
                    .build();
        }
        public static Operation guiOperation(String partDescription, DurationWrapper time) {
            return Operation.builder()
                    .name("GUI operation")
                    .time(time)
                    .interruptionDescription("Waiting for an user input...")
                    .operationDescription(partDescription)
                    .build();
        }
        public static Operation cleanUpOperation(String partDescription, DurationWrapper time) {
            return Operation.builder()
                    .name("Clean up operation")
                    .time(time)
                    .interruptionDescription("App's resources disposed")
                    .operationDescription(partDescription)
                    .build();
        }
    }
}
