package webfx.tool.buildtool.util.streamable.impl;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
class FilteredSpliterator<T> extends ActionMapperDelegatingSpliterator<T> {

    private static boolean lastTestResult;

    FilteredSpliterator(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        super(spliterator, action ->
                t -> {
                    if (lastTestResult = predicate.test(t))
                        action.accept(t);
                }, true);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        while (super.tryAdvance(action)) {
            if (lastTestResult)
                return true;
        }
        return false;
    }
}
