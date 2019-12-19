package webfx.tools.util.reusablestream;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author Bruno Salmon
 */
@FunctionalInterface
public interface Spliterable<T> extends Iterable<T> {

    Spliterator<T> spliterator();

    @Override
    default Iterator<T> iterator() {
        return Spliterators.iterator(spliterator());
    }

}
