package mem.virtual;

import lombok.experimental.SuperBuilder;
import mem.physical.PictureMemoryPrinter;
import mem.shared.PrettyTable;
import mem.shared.Utils;
import rich_text.RichConsole;
import rich_text.RichTextConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuperBuilder
public class VirtualMemoryManagerImpl extends VirtualMemoryManager {
    int _processesCount;
    int _timer;

    @Override
    public void accept(List<Process> processes) {
        List<Page> memoryPages = getMemoryPages();
        List<ProcessTable> processTables = new ArrayList<>();
        // lifecycle
        while (_timer++ < lifetime()) {
            tryAddNewProcess(memoryPages, processTables, processes);

            // print memory
            RichConsole.print(RichTextConfig.metaMessageStyle(), "Time tick: %s", _timer);
            new PictureMemoryPrinter().printVirtual(memoryPages);
            if (!processes.isEmpty()) {
                Process activeProcess = Utils.choice(processes);
                RichConsole.print(RichTextConfig.metaMessageStyle(),
                        "Active process: %s", activeProcess.label());
                Process.OwnedPageInfo activePage = Utils.choice(activeProcess.ownedPagesInfo());
                int indexOfActivePage = activeProcess.ownedPagesInfo().indexOf(activePage);
                RichConsole.print(RichTextConfig.metaMessageStyle(),
                        "Active page: %s", indexOfActivePage);
                processTables.stream()
                        .filter(processTable -> processTable.label().equals(activeProcess.label()))
                        .forEach(processTable -> {
                            Page.Info activePageInfo = processTable.pagesInfo().get(indexOfActivePage);
                            if (!activePageInfo.isShown()) {
                                RichConsole.print(RichTextConfig.metaMessageStyle(),
                                        "Page isn't loaded, try to search it in swap file");
                                addPage(memoryPages, activePageInfo, processTables);
                            }
                            if (!activePageInfo.isReadOnly() && Utils.roll(.5f)) {
                                activePageInfo.isChanged(true);
                            }
                        });
                activeProcess.lifetime(activeProcess.lifetime() - 1);
                if (activeProcess.lifetime() < 0) {
                    for (int i = 0; i < processTables.size(); i++) {
                        ProcessTable processTable = processTables.get(i);
                        if (processTable.label().equals(activeProcess.label())) {
                            for (Page.Info pageInfo : processTable.pagesInfo()) {
                                for (Page memoryPage : memoryPages) {
                                    if (pageInfo.address() == memoryPage.address()) {
                                        memoryPage.isOccupied(false);
                                    }
                                }
                            }
                            processTables.remove(processTable);
                        }
                    }
                    processes.remove(activeProcess);
                }

            }

            // print pages info
            for (int i = 0; i < processTables.size(); i++) {
                ProcessTable table = processTables.get(i);
                PrettyTable prettyTable = new PrettyTable("No.", "isReadOnly", "isChanged", "isShown", "Address");
                RichConsole.print(RichTextConfig.metaMessageStyle(),
                        "Process: %s", table.label());
                RichConsole.print(RichTextConfig.metaMessageStyle(),
                        "Time remain: %s", processes.get(i).lifetime());
                for (int j = 0; j < table.pagesInfo().size(); j++) {
                    Page.Info info = table.pagesInfo().get(j);
                    prettyTable.addRow(j, info.isReadOnly(), info.isChanged(), info.isShown(), info.address());
                }
                System.out.println(prettyTable.toString());
            }
        }
    }

    private void tryAddNewProcess(List<Page> memory, List<ProcessTable> processTables, List<Process> processes) {
        if (processes.size() < processesLimit() && Utils.roll(processAppearingPercentage())) {
            // building and adding  new process
            int processLifetime = processLifetimeRange().getRandom();
            int pagesCount = processPagesCountRange().getRandom();
            List<Process.OwnedPageInfo> ownedPagesInfo = IntStream.range(0, pagesCount)
                    .mapToObj(ignored -> Process.OwnedPageInfo.builder()
                            .isReadOnly(Utils.roll(0.5f))
                            .isChanged(false)
                            .build())
                    .collect(Collectors.toList());
            Process newProcess = Process.builder()
                    .label(String.format("%s:%s", Utils.choice(Utils.LABELS), _processesCount))
                    .lifetime(processLifetime)
                    .ownedPagesInfo(ownedPagesInfo)
                    .build();
            processes.add(newProcess);
            _processesCount++;
            addProcess(newProcess, memory, processTables);
        }
    }

    private void addProcess(Process process, List<Page> memoryPages, List<ProcessTable> processTables) {
        ProcessTable pt = ProcessTable.builder()
                .label(process.label())
                .build();
        List<Page.Info> processPagesInfo = process.ownedPagesInfo().stream()
                .map(ownedProcess -> Page.Info.builder()
                        .isReadOnly(ownedProcess.isReadOnly())
                        .address(-1)
                        .build())
                .peek(pageInfo -> tryAddPage(memoryPages, pageInfo, processTables))
                .collect(Collectors.toList());

        pt.pagesInfo(processPagesInfo);
        processTables.add(pt);
    }

    /**
     * Adds page to memory with probability depending on percentage of memory occupied.
     *
     * @param memoryPages
     * @param pageInfo
     * @param processTables
     */
    private void tryAddPage(List<Page> memoryPages, Page.Info pageInfo, List<ProcessTable> processTables) {
        int occupiedPagesCount = (int) memoryPages.stream()
                .filter(Page::isOccupied)
                .count();
        double percentageMemoryOccupied = occupiedPagesCount / (double) memoryPages.size();
        double addingProbability = percentageMemoryOccupied > memoryOccupiedThreshold() ?
                1 - percentageMemoryOccupied : 1d;
        if (Math.random() < addingProbability) {
            addPage(memoryPages, pageInfo, processTables);
        }
    }

    private void addPage(List<Page> memoryPages, Page.Info pageInfo, List<ProcessTable> processTables) {
        Page freePage = null;
        while (Objects.isNull(freePage)) {
            freePage = memoryPages.stream()
                    .filter(page -> !page.isOccupied())
                    .findFirst()
                    .orElse(null);
            if (Objects.nonNull(freePage)) {
                // add page
                freePage.isOccupied(true);
                freePage.addingTime(_timer);
                pageInfo.isShown(true);
                pageInfo.address(freePage.address());
            } else {
                freeMemory(memoryPages, pageInfo, processTables);
            }
        }
    }

    private void freeMemory(List<Page> memoryPages, Page.Info pageInfo, List<ProcessTable> processTables) {
        int minTime = memoryPages.stream()
                .mapToInt(Page::addingTime)
                .min()
                .orElse(Integer.MAX_VALUE);

        memoryPages.stream()
                .filter(page -> page.addingTime() == minTime)
                .findFirst()
                .ifPresent(page -> {
                    page.addingTime(-1);
                    page.isOccupied(false);
                    RichConsole.print(RichTextConfig.metaMessageStyle(),
                            "Release occupied memory with address %s", page.address());
                    processTables.stream()
                            .flatMap(processTable -> processTable.pagesInfo().stream())
                            // = i ???
                            .filter(info -> info.address() == page.address())
                            .peek(info -> info.address(-1))
                            .filter(Page.Info::isChanged)
                            .forEach(info -> RichConsole.print(RichTextConfig.metaMessageStyle(),
                                    "Memory page is immutable, save it in swap file"));
                });
    }

    private List<Page> getMemoryPages() {
        return IntStream.range(0, pagesCount())
                .mapToObj(i -> Page.builder()
                        .address(i)
                        .isOccupied(false)
                        .addingTime(-1)
                        .build())
                .collect(Collectors.toList());
    }
}
