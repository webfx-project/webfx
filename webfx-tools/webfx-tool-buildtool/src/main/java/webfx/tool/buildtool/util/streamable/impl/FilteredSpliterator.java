package webfx.tool.buildtool.util.streamable.impl;

import java.util.Spliterator;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
class FilteredSpliterator<T> extends ActionMapperDelegatingSpliterator<T> {

    FilteredSpliterator(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        super(spliterator, action ->
                t -> {
                    if (predicate.test(t))
                        action.accept(t);
                }, true);
    }
}
