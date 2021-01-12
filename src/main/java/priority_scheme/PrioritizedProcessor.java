package priority_scheme;

import lombok.experimental.SuperBuilder;
import rich_text.Decoration;
import rich_text.RichConsole;
import rich_text.RichTextConfig;
import shared.QuantizedProcessor;
import tasks.DurationWrapper;
import tasks.Task;

import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.function.ToLongFunction;

import static java.util.Objects.*;
import static rich_text.RichConsole.print;

@SuperBuilder
public class PrioritizedProcessor extends QuantizedProcessor<PrioritizedTask> {
    PrioritizedProcessorState state;

    @Override
    public void processTasks() {
        PrioritizedTask task;
        PrioritizedProcessorState state = isNull(this.state)
                ? new PrioritizedProcessorState(initTimeQuantum, new TaskQueue(tasks))
                : this.state;
        while (nonNull(task = state.getNextTask())) {
            // current time
            DurationWrapper timeQuantum = state.getCurrentTimeQuantum();
            long timeQuantumMillis = timeQuantum.getMillis();
            // current operation time
            Task.Operation operation = task.getCurrentOperation();
            long curRemBurstTime = operation.getRemainedBurstTime();
            boolean isTimeQuantumSpent = curRemBurstTime >= timeQuantumMillis;
            if (curRemBurstTime > timeQuantumMillis) {
                processingTime += timeQuantumMillis;

                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " is given a time quantum (%s). Process the task's operation...", timeQuantum.toString());

                if (!task.proceed(timeQuantumMillis)) {
                    // task not done
                    // background task, should decrease priority
                    task.setPriority(task.getPriority().prev());
                }

                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " interrupted, %d ms of burst time remained...", operation.getRemainedBurstTime());

            } else {
                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " needs %d ms, it's less or equals than time quantum (%s). Process the operation...",
                        operation.getRemainedBurstTime(), timeQuantum.toString());

                processingTime += curRemBurstTime;

                // IMPORTANT: before proceed
                // should correct total waiting time,
                // so it doesnt seem like operations waits since starting task processing
                long totalWaitingTime = processingTime - operation.getBurstTime();
                long prevOperationTurnAroundTime = task.getPreviousOperation()
                        .map(Task.Operation::getTurnAroundTime)
                        .orElse(0L);
                operation.setWaitingTime(totalWaitingTime - prevOperationTurnAroundTime);

                if (!task.proceed(timeQuantumMillis)) {
                    task.setPriority(task.getPriority().next());
                }

                print(task.getDecoration(),
                        getTaskTag(task, operation, state) + " is fully processed! ( %s ms total)", operation.getBurstTime());

            }
            state.handlePostProceed(isTimeQuantumSpent, task, operation);
        }
        //printStatistics();
    }

    private LongSummaryStatistics getStatistics(ToLongFunction<Task.Operation> operationMapper) {
        return tasks.stream()
                .flatMap(task -> task.getOperations().stream())
                .mapToLong(operationMapper)
                .summaryStatistics();
    }

    private void printStatistics() {
        RichConsole.print("Tasks performed:\n", null);
        for (PrioritizedTask task : tasks) {
            RichConsole.print(task.getDecoration(),
                    "'%s' task total : \n ", task.getName());
            for (Task.Operation operation : task.getOperations()) {
                RichConsole.print("\t" + operation.getName() +
                                ":\n\t\t Burst time (ms): " + operation.getBurstTime() +
                                "\n\t\t Waiting time (ms): " + operation.getWaitingTime() +
                                "\n\t\t Turn around time (ms): " + operation.getTurnAroundTime() + "\n",
                        task.getDecoration());
            }
        }
        LongSummaryStatistics waitingTimeStat = getStatistics(Task.Operation::getWaitingTime);
        LongSummaryStatistics turnAroundStat = getStatistics(Task.Operation::getTurnAroundTime);

        RichConsole.print("Statistics:\n\t Average waiting time (ms): " + Math.round(waitingTimeStat.getAverage()) +
                        "\n\t Max waiting time (ms): " + Math.round(waitingTimeStat.getMax()) +
                        "\n\t Min waiting time (ms): " + Math.round(waitingTimeStat.getMin()) +
                        "\n\t Average turn around time (ms): " + Math.round(turnAroundStat.getAverage()) +
                        "\n\t Max turn around time (ms): " + Math.round(turnAroundStat.getMax()) +
                        "\n\t Min turn around time (ms): " + Math.round(turnAroundStat.getMin()),
                RichTextConfig.builder()
                        .decoration(Decoration.UNDERLINE)
                        .build());
    }

    private String getTaskTag(PrioritizedTask task, Task.Operation operation, PrioritizedProcessorState state) {
        return String.format("'%s - %s priority : %s - %d remained burst time (ms)'",
                task.getName(),
                state.coveredTask == task ? state.priority : task.getPriority(),
                operation.getName(),
                operation.getRemainedBurstTime() < 0 ? 0 : operation.getRemainedBurstTime()
        );
    }
}
