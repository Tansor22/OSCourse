package rom;

import lombok.experimental.SuperBuilder;
import rich_text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuperBuilder
public class PlainMemoryConsumer extends MemoryConsumer {
    List<MemoryChunk> _taskQueue;

    @Override
    public void accept(List<MemoryChunk> memory) {
        _taskQueue = new ArrayList<>();
        int taskCount = 0;
        int timer = 0;
        float addingPercentage = .4f;
        boolean isMemoryDisposed = true;
        while (taskCount <= taskLimit()) {
            timer++;

            boolean isTaskAdded = false;
            // roll for adding, by default consider no new task
            String newTaskAppearingStatus = "There is no task appeared...";
            if (Math.random() <= addingPercentage) {
                taskCount++;
                final MemoryChunk newChunk = getRandomMemoryChunk(taskCount);
                newTaskAppearingStatus = "New task for processing appeared - " + newChunk.toString();
                if (_taskQueue.isEmpty()) {
                    // try to add
                    isTaskAdded = tryAddTask(newChunk, memory);
                }
                if (!isTaskAdded) {
                    // add to queue
                    newTaskAppearingStatus += ". Not enough memory to process it, putting it ti task queue...";
                    _taskQueue.add(newChunk);
                }
            }
            RichConsole.print(newTaskAppearingStatus, RichTextConfig.builder()
                    .decoration(Decoration.UNDERLINE)
                    .build());
            while (!_taskQueue.isEmpty()) {
                RichConsole.print("Trying to get task from queue...", RichTextConfig.builder()
                        .decoration(Decoration.UNDERLINE)
                        .build());
                MemoryChunk chunkFromQueue = _taskQueue.get(0);
                isTaskAdded = tryAddTask(chunkFromQueue, memory);

                if (isMemoryDisposed) {
                    isMemoryDisposed = false;
                    // defragmentation
                }
                // by default consider there is not enough memory
                String taskAddingFromQueueStatus = "Not enough memory for processing task from queue - " + chunkFromQueue.toString();
                if (isTaskAdded) {
                    _taskQueue.remove(chunkFromQueue);
                    taskAddingFromQueueStatus = "Task from queue was successfully added - " + chunkFromQueue.toString();
                }
                RichConsole.print(taskAddingFromQueueStatus, RichTextConfig.builder()
                        .decoration(Decoration.UNDERLINE)
                        .build());

                // always at the end
                if (!isTaskAdded) {
                    break;
                }
            }
            cleanUp(memory);
            // output ...
            RichConsole.print(RichTextConfig.builder()
                    .decoration(Decoration.UNDERLINE)
                    .build(), "Time tick: %s, queue size: %s", timer, _taskQueue.size());

            // chunks as data structures
            for (MemoryChunk chunk : memory) {
                RichTextConfig rtc = RichTextConfig.builder()
                        .color(chunk.isFree() ? Color.GREEN : Color.RED)
                        .newLine(false)
                        .build();
                RichConsole.print(chunk.toString() + " ", rtc);
            }

            RichConsole.print("\n_________________________________", null);
            // chunks as stars
            for (MemoryChunk chunk : memory) {
                RichTextConfig rtc = RichTextConfig.builder()
                        .color(Color.BLACK)
                        .background(chunk.isFree() ? Background.GREEN : Background.RED)
                        .newLine(false)
                        .build();
                String chunkAsStars = "X".repeat(Math.max(0, (int) Math.ceil(chunk.size() / 10d)));
                RichConsole.print(chunkAsStars, rtc);
                RichConsole.print("", null);
            }
            RichConsole.print("\n_________________________________", null);
        }
    }

    private void cleanUp(List<MemoryChunk> memory) {
        for (MemoryChunk chunk : memory) {
            // find suitable chunks and mark them as 'free'
            if (!chunk.isFree()) {
                chunk.timeRemain(chunk.timeRemain() - 1);
                if (chunk.timeRemain() <= 0) {
                    chunk.timeRemain(0);
                    chunk.label("free");
                }
            }
        }

        // join neighboring chunks marked as 'free'
        for (int i = 0; i < memory.size() - 1; i++) {
            MemoryChunk current = memory.get(i);
            MemoryChunk next = memory.get(i + 1);
            if (current.isFree() && next.isFree()) {
                current.size(current.size() + next.size());
                memory.remove(next);
            }
        }
    }


    private boolean tryAddTask(final MemoryChunk chunk, final List<MemoryChunk> memory) {
        // find free memory
        final Optional<MemoryChunk> freeMemory = memory.stream()
                .filter(inMemoryChunk -> inMemoryChunk.isFree()
                        && inMemoryChunk.size() >= chunk.size()).findFirst();
        // process it
        freeMemory.ifPresent(self -> {
            final int position = memory.indexOf(self);
            if (self.size() > chunk.size()) {
                self.size(self.size() - chunk.size());
            } else {
                memory.remove(self);
            }
            memory.add(position, chunk);
        });
        return freeMemory.isPresent();
    }

    private MemoryChunk getRandomMemoryChunk(int id) {
        // auxiliary vars
        Random r = new Random(System.currentTimeMillis());
        String chunkLabel = String.format("%s:%s", MemoryChunk.LABELS[r.nextInt(MemoryChunk.LABELS.length)], id);
        int[] ints = r.ints(5, 30)
                .limit(2)
                .toArray();
        int chunkSize = ints[0];
        int chunkTime = ints[1];
        // output
        return MemoryChunk.builder()
                .size(chunkSize)
                .timeRemain(chunkTime)
                .label(chunkLabel)
                .build();
    }
}
