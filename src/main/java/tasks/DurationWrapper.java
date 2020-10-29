package tasks;

import batch.BatchConstants;
import lombok.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE, toBuilder = true)
public class DurationWrapper implements Comparable<DurationWrapper>, Cloneable {
    Duration duration;
    TimeUnit timeUnit;
    @Getter
    long millis;

    public static DurationWrapper millis(long value) {
        return new DurationWrapper(Duration.ofMillis(value), TimeUnit.MILLISECONDS, value);
    }

    public static DurationWrapper seconds(long value) {
        Duration seconds = Duration.ofSeconds(value);
        return new DurationWrapper(seconds, TimeUnit.SECONDS, seconds.toMillis());
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public DurationWrapper clone() {
        return this.toBuilder().build();
    }

    public DurationWrapper plus(DurationWrapper dw) {
        return new DurationWrapper(duration.plus(dw.duration), dw.timeUnit, dw.millis);
    }

    public DurationWrapper minus(DurationWrapper dw) {
        return new DurationWrapper(duration.minus(dw.duration), dw.timeUnit, dw.millis);
    }

    public DurationWrapper convert(DurationWrapper dw) {
        return new DurationWrapper(duration, dw.timeUnit, dw.millis);
    }

    @Override
    public String toString() {
        long value = timeUnit.convert(duration);
        String timeUnitWord = value > 1 ? timeUnit.name() : timeUnit.name().substring(0, timeUnit.name().length() - 1);
        return value + BatchConstants.SPACE + timeUnitWord.toLowerCase();
    }

    @Override
    public int compareTo(DurationWrapper other) {
        return Long.compare(getMillis(), other.getMillis());
    }
}
