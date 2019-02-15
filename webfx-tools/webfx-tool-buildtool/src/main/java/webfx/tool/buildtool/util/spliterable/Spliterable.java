package webfx.tool.buildtool.util.spliterable;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Bruno Salmon
 */

@FunctionalInterface
public interface Spliterable<T> extends ThrowableSpliterable<T> {

    Spliterator<T> spliterator();

    default Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

}
