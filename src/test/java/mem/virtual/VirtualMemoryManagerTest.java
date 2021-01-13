package mem.virtual;

import org.junit.Test;

import static org.junit.Assert.*;

public class VirtualMemoryManagerTest {

    @Test
    public void manageTest() {
        VirtualMemoryManager vmm = VirtualMemoryManagerImpl.builder()
                .build();

        vmm.manage();
    }
}