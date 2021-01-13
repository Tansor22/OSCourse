package mem.shared;

import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true, prefix = "_")
public class IntRange {
    int _origin;
    int _bound;

    public int getRandom() {
        return _origin + (int) (Math.random() * (_bound - _origin));
    }
}
