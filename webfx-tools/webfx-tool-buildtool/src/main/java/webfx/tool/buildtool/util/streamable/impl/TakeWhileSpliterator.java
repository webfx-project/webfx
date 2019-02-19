package webfx.tool.buildtool.util.streamable.impl;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
class TakeWhileSpliterator<T> extends ActionMapperDelegatingSpliterator<T> {

    private static boolean lastTestResult;
    private static Object lastFailedElement;

    TakeWhileSpliterator(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        super(spliterator, action ->
                t -> {
                    lastFailedElement = null;
                    if (lastTestResult &= predicate.test(t))
                        action.accept(t);
                    else
                        lastFailedElement = t;
                }, true);
        lastTestResult = true;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        if (lastFailedElement != null) {
            getMappedAction(action).accept((T) lastFailedElement);
            return lastTestResult;
        }
        return super.tryAdvance(action) && lastTestResult;
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        if (!hasDelegateBeenFullyTraversed()) {
            while (tryAdvance(action));
            onDelegateFullyTraversed();
        }
    }
}
