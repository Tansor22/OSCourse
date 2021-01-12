package rom;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true, prefix = "_")
public class IntRange {
    int _origin;
    int _bound;
}
