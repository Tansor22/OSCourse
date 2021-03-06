package mem.virtual;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@Accessors(fluent = true, prefix = "_")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Process {
    String _label;
    int _lifetime;
    List<OwnedPageInfo> _ownedPagesInfo;

    @Builder
    @Getter
    @Setter
    @EqualsAndHashCode
    @Accessors(fluent = true, prefix = "_")
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class OwnedPageInfo {
        boolean _isReadOnly;
        boolean _isChanged;
    }
}
