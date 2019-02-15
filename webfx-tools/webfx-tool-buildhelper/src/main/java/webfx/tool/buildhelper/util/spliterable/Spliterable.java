package webfx.tool.buildhelper.util.spliterable;

import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * @author Bruno Salmon
 */

@FunctionalInterface
public interface Spliterable<T> {

    Spliterator<T> buildSpliterator();

    default Stream<T> buildStream() {
        return StreamSupport.stream(buildSpliterator(), false);
    }

}
