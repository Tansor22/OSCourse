package mem.shared;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class Utils {
    public String[] LABELS = new String[]{
            "Browser Tab", "Bitcoin Mining", "GUI"
    };

    public boolean roll(float probability) {
        return Math.random() < probability;
    }

    public <T> T choice(List<T> options) {
        return options.get((int) (Math.random() * options.size()));
    }
    public <T> T choice(T[] options) {
        return options[(int) (Math.random() * options.length)];
    }
}
