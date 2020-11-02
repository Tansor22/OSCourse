package tasks;

import lombok.*;
import lombok.experimental.SuperBuilder;
import rich_text.RichTextConfig;

import java.util.List;
import java.util.Optional;

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

    public boolean proceed(long time) {
        if ((getCurrentOperation().remainedBurstTime -= time) <= 0) {
            // operation is done
            curOpIndex++;
        }
        return isDone();
    }

    public boolean proceed() {
        return proceed(getCurrentOperation().burstTime);
    }

    public Operation getCurrentOperation() {
        return operations.get(curOpIndex);
    }

    public Optional<Operation> getPreviousOperation() {
        return curOpIndex - 1 >= 0 ? Optional.of(operations.get(curOpIndex - 1)) : Optional.empty();
    }

    public DurationWrapper getTimeTotal() {
        return operations.stream()
                .map(Task.Operation::getTime)
                .reduce(DurationWrapper::plus)
                .orElse(DurationWrapper.millis(0));
    }

    @Getter
    public static class Operation {
        String name;
        String operationDescription;
        String interruptionDescription;
        DurationWrapper time;
        long burstTime;
        long turnAroundTime;
        @Setter
        long waitingTime;
        @Setter
        long remainedBurstTime;

        @Builder
        private Operation(String name, String operationDescription, String interruptionDescription, DurationWrapper time) {
            this.name = name;
            this.operationDescription = operationDescription;
            this.interruptionDescription = interruptionDescription;
            this.time = time;
            this.burstTime = time.getMillis();
            this.remainedBurstTime = burstTime;
        }

        public long getTurnAroundTime() {
            return burstTime + waitingTime;
        }

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
