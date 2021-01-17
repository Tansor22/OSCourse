package mem.virtual;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@Accessors(fluent = true, prefix = "_")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Page {
    int _address;
    boolean _isOccupied;
    int _addingTime;

    @Builder
    @Getter
    @Setter
    @EqualsAndHashCode
    @Accessors(fluent = true, prefix = "_")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class Info {
        boolean _isReadOnly;
        boolean _isChanged;
        boolean _isShown;
        int _address;
    }
}
