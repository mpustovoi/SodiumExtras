package toni.sodiumextras.foundation.fps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FpsHistory {
    private static final int LIMIT = 60;

    private final List<Integer> list = new ArrayList<>();

    public void add(int fps) {
        this.list.add(fps);

        if (this.list.size() > LIMIT) {
            this.list.remove(0);
        }
    }

    public int getMinimum() {
        if (this.list.size() + 1 < LIMIT) {
            return getAverage();
        }

        return Collections.min(this.list);
    }

    public int getMaximum() {
        return Collections.max(this.list);
    }

    public int getAverage() {
        int sum = 0;
        for (int fps : this.list) {
            sum += fps;
        }
        return sum / this.list.size();
    }
}