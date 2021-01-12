package rom;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@Accessors(fluent = true, prefix = "_")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemoryChunk {
    String _label;
    int _size;
    int _timeRemain;

    public boolean isFree() {
        return "free".equalsIgnoreCase(_label);
    }

    static String[] LABELS = new String[]{
        "Browser Tab", "Bitcoin Mining", "GUI"
    };

    @Override
    public String toString() {
        return String.format("{%s - size: %s, timeRemain: %s}", _label, _size, _timeRemain);
    }
}
