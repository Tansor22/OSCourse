package round_robin;

import lombok.experimental.SuperBuilder;
import rich_text.Decoration;
import rich_text.RichConsole;
import rich_text.RichTextConfig;
import round_robin.tasks.RoundRobinTask;
import shared.QuantizedProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;


@SuperBuilder
public class RoundRobinProcessor extends QuantizedProcessor<RoundRobinTask> {
    @Override
    public void processTasks() {
        // initializing data required
        List<RoundRobinTask> roundRobinTasks = new ArrayList<>(tasks);
        long timeQuantumMillis = timeQuantum.toMillis();
        // time for current task
        long currentTime = 0;

        // processing
        while (true) {
            boolean done = true;
            for (RoundRobinTask currentTask : roundRobinTasks) {
                long curRemBurstTime = currentTask.getRemainBurstTime();
                if (curRemBurstTime > 0) {
                    // A task that should be processed has found
                    done = false;
                    if (curRemBurstTime > timeQuantum.toMillis()) {
                        currentTime += timeQuantum.toMillis();

                        RichConsole.print(
                                "'" + currentTask.getName() + "' is given a time quantum (" + timeQuantum.toString() + "). Process the task...",
                                currentTask.getDecoration());

                        // correct 'remainBurstTime' of current task
                        curRemBurstTime -= timeQuantumMillis;

                        RichConsole.print(
                                "'" + currentTask.getName() + "'s' time is due, " + curRemBurstTime + " ms of burst time remains...",
                                currentTask.getDecoration());

                        currentTask.setRemainBurstTime(curRemBurstTime);
                    } else {
                        // grab remain burst time
                        currentTime += curRemBurstTime;

                        RichConsole.print(
                                "'" + currentTask.getName() + "' needs " + curRemBurstTime +" ms, it's  equal or less than time quantum (" + timeQuantum.toString() + "). Process the task...",
                                currentTask.getDecoration());

                        // waiting time is current time minus time
                        currentTask.setWaitingTime(currentTime - currentTask.getBurstTime());

                        RichConsole.print(
                                "'" + currentTask.getName() + "' is fully processed! (" + currentTask.getBurstTime() + " ms total)",
                                currentTask.getDecoration());

                        // mark the task as processed
                        currentTask.setRemainBurstTime(0);
                    }
                }
            }
            if (done) break;
        }

        // output stats
        RichConsole.print("Tasks performed:\n", null);
        for (RoundRobinTask roundRobinTask : roundRobinTasks) {
            RichConsole.print(roundRobinTask.getName() +
                            ":\n\t Burst time (ms): " + roundRobinTask.getBurstTime() +
                            "\n\t Waiting time (ms): " + roundRobinTask.getWaitingTime() +
                            "\n\t Turn around time (ms): " + roundRobinTask.getTurnAroundTime(),
                    roundRobinTask.getDecoration());
        }
        LongSummaryStatistics waitingTimeStat = roundRobinTasks.stream()
                .mapToLong(RoundRobinTask::getWaitingTime)
                .summaryStatistics();
        LongSummaryStatistics turnAroundStat = roundRobinTasks.stream()
                .mapToLong(RoundRobinTask::getTurnAroundTime)
                .summaryStatistics();
        RichConsole.print("Statistic:\n\t Average waiting time (ms): " + Math.round(waitingTimeStat.getAverage()) +
                        "\n\t Max waiting time (ms): " + Math.round(waitingTimeStat.getMax()) +
                        "\n\t Min waiting time (ms): " + Math.round(waitingTimeStat.getMin()) +
                        "\n\t Average turn around time (ms): " + Math.round(turnAroundStat.getAverage()) +
                        "\n\t Max turn around time (ms): " + Math.round(turnAroundStat.getMax()) +
                        "\n\t Min turn around time (ms): " + Math.round(turnAroundStat.getMin()),
                RichTextConfig.builder()
                        .decoration(Decoration.UNDERLINE)
                        .build());

    }
}
