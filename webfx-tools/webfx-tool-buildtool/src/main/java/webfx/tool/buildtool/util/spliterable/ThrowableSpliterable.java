package webfx.tool.buildtool.util.spliterable;

import java.util.Spliterator;
import java.util.Spliterators;

/**
 * @author Bruno Salmon
 */
@FunctionalInterface
public interface ThrowableSpliterable<T> {

    Spliterator<T> spliterator() throws Exception;

    default Spliterable<T> toSpliterable() {
        return () -> {
            try {
                return ThrowableSpliterable.this.spliterator();
            } catch (Exception e) {
                e.printStackTrace();
                return Spliterators.emptySpliterator();
            }
        };
    }

}
