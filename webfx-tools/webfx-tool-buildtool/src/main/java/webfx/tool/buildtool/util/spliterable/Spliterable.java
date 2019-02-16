package webfx.tool.buildtool.util.spliterable;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author Bruno Salmon
 */
@FunctionalInterface
public interface Spliterable<T> extends ThrowableSpliterable<T>, Iterable<T> {

    Spliterator<T> spliterator();

    @Override
    default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }

    default Spliterable<T> toSpliterable() {
        return this;
    }
}
