package priority_scheme;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@AllArgsConstructor
@Getter
public enum Priority {
    LOW(0),
    NORMAL(1),
    HIGH(2);
    private final int intValue;

    final static List<Priority> VALUES = List.of(values());
    final static int SIZE = VALUES.size();
    final static Random RANDOM = new Random(System.currentTimeMillis());

    public static Priority random() {
        return VALUES.get(RANDOM.nextInt(SIZE));
    }

    public static Priority valueOf(int intValue) {
        return switch (intValue) {
            case 0 -> LOW;
            case 1 -> NORMAL;
            default -> HIGH;
        };
    }
    public  Priority next() {
        return switch (this) {
            case LOW -> NORMAL;
            case NORMAL, HIGH -> HIGH;
        };
    }
    public  Priority prev() {
        return switch (this) {
            case LOW, NORMAL -> LOW;
            case HIGH -> NORMAL;
        };
    }
}
