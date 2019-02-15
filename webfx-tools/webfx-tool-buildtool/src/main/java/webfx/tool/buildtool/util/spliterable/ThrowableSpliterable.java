package webfx.tool.buildtool.util.spliterable;

import java.util.Spliterator;

/**
 * @author Bruno Salmon
 */
public interface ThrowableSpliterable<T> {

    Spliterator<T> spliterator() throws Exception;
}
