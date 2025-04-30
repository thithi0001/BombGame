package AI.pathFinding;

import java.awt.Point;
import java.util.Objects;

public class Pair {

    Point from;
    Point to;

    public Pair(Point from, Point to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair other) {
            return from.equals(other.from) &&
                    to.equals(other.to);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }

    @Override
    public String toString() {
        return "{" + from + ", " + to + "}";
    }
}
