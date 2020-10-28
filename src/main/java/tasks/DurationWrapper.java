package tasks;

import batch.BatchConstants;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class DurationWrapper {
    Duration duration;
    TimeUnit timeUnit;

    public static DurationWrapper millis(long value) {
        return new DurationWrapper(Duration.ofMillis(value), TimeUnit.MILLISECONDS);
    }

    public static DurationWrapper seconds(long value) {
        return new DurationWrapper(Duration.ofSeconds(value), TimeUnit.SECONDS);
    }


    public long toMillis() {
        return TimeUnit.MILLISECONDS.convert(duration);
    }

    public DurationWrapper plus(DurationWrapper dw) {
        return new DurationWrapper(duration.plus(dw.duration), dw.timeUnit);
    }
    public DurationWrapper minus(DurationWrapper dw) {
        return new DurationWrapper(duration.minus(dw.duration), dw.timeUnit);
    }

    public DurationWrapper convert(DurationWrapper dw) {
        return new DurationWrapper(duration, dw.timeUnit);
    }

    @Override
    public String toString() {
        long value = timeUnit.convert(duration);
        String timeUnitWord = value > 1 ? timeUnit.name() : timeUnit.name().substring(0, timeUnit.name().length() - 1);
        return value + BatchConstants.SPACE + timeUnitWord.toLowerCase();
    }
}
