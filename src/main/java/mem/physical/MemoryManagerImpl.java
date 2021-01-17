package mem.physical;

import lombok.experimental.SuperBuilder;
import mem.shared.Utils;
import rich_text.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@SuperBuilder
public class MemoryManagerImpl extends MemoryManager {
    List<MemoryChunk> _taskQueue;

    @Override
    public void accept(List<MemoryChunk> memory) {
        _taskQueue = new ArrayList<>();
        int taskCount = 0;
        int timer = 0;
        boolean isMemoryDisposed = false;
        while (taskCount <= taskLimit()) {
            timer++;
            if (isMemoryDisposed && defragmentationEnabled()) {
                // performing defragmentation
                RichConsole.print("\tMemory state before defragmentation: ", RichTextConfig.metaMessageStyle());
                new PictureMemoryPrinter().print(memory);

                int totalFreeMemorySize = memory.stream()
                        .filter(MemoryChunk::isFree)
                        .mapToInt(MemoryChunk::size)
                        .reduce(Integer::sum)
                        .orElse(0);

                memory.removeIf(MemoryChunk::isFree);
                memory.add(MemoryChunk.builder()
                        .label("free")
                        .timeRemain(0)
                        .size(totalFreeMemorySize)
                        .build());
                RichConsole.print("\tMemory state after defragmentation: ", RichTextConfig.metaMessageStyle());
                new PictureMemoryPrinter().print(memory);
            }
            boolean isTaskAdded = false;
            // roll for adding, by default consider no new task
            String newTaskAppearingStatus = "There is no task appeared...";
            if (Math.random() <= taskAppearingPercentage()) {
                taskCount++;
                final MemoryChunk newChunk = getRandomMemoryChunk(taskCount);
                newTaskAppearingStatus = "New task for processing appeared - " + newChunk.toString();
                if (_taskQueue.isEmpty()) {
                    // try to add
                    isTaskAdded = tryAddTask(newChunk, memory);
                }
                if (!isTaskAdded) {
                    // add to queue
                    newTaskAppearingStatus += ". Not enough memory to process it, putting it to task queue...";
                    _taskQueue.add(newChunk);
                }
            }
            RichConsole.print(newTaskAppearingStatus, RichTextConfig.metaMessageStyle());
            while (!_taskQueue.isEmpty()) {
                RichConsole.print("Trying to get task from queue...", RichTextConfig.metaMessageStyle());
                MemoryChunk chunkFromQueue = _taskQueue.get(0);
                isTaskAdded = tryAddTask(chunkFromQueue, memory);


                // by default consider there is not enough memory
                String taskAddingFromQueueStatus = "Not enough memory for processing task from queue - " + chunkFromQueue.toString();
                if (isTaskAdded) {
                    _taskQueue.remove(chunkFromQueue);
                    taskAddingFromQueueStatus = "Task from queue was successfully added - " + chunkFromQueue.toString();
                }
                RichConsole.print(taskAddingFromQueueStatus, RichTextConfig.metaMessageStyle());

                // always at the end
                if (!isTaskAdded) {
                    break;
                }
            }
            isMemoryDisposed = workPerformed(memory);
            // tracking
            RichConsole.print(RichTextConfig.metaMessageStyle(), "Time tick: %s, queue size: %s", timer, _taskQueue.size());

            // chunks as data structures
            new PojoMemoryPrinter().print(memory);
            // chunks as picture
            new PictureMemoryPrinter().print(memory);
            RichConsole.print("\n_________________________________", null);
        }
    }

    private boolean workPerformed(List<MemoryChunk> memory) {
        boolean isMemoryDisposed = false;
        for (MemoryChunk chunk : memory) {
            // find suitable chunks and mark them as 'free'
            if (!chunk.isFree()) {
                chunk.timeRemain(chunk.timeRemain() - 1);
                if (chunk.timeRemain() <= 0) {
                    chunk.timeRemain(0);
                    chunk.label("free");
                    isMemoryDisposed = true;
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
        return isMemoryDisposed;
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
        String chunkLabel = String.format("%s:%s", Utils.choice(Utils.LABELS), id);
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
