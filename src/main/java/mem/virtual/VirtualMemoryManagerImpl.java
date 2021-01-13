package mem.virtual;

import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
public class VirtualMemoryManagerImpl extends VirtualMemoryManager {
    @Override
    public void accept(List<Process> processes) {
        System.out.println("Managing " + processes);
    }
}
