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
public class ProcessTable {
    String _label;
    List<Page.Info> _pagesInfo;
}
