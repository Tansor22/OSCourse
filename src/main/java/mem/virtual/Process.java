package mem.virtual;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Builder
@Getter
@Setter
@EqualsAndHashCode
@Accessors(fluent = true, prefix = "_")
public class Process {
}
