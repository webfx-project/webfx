package webfx.tools.util.reusablestream.impl;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
abstract class PushBackSpliterator<T, R> implements WrappingSpliterator<T, R> {

    private T lastElement;
    boolean pushBackRequested;

    void pushBackLastElement() {
        Spliterator<T> wrappedSpliterator = getWrappedSpliterator();
        if (wrappedSpliterator instanceof PushBackSpliterator) {
            ((PushBackSpliterator) wrappedSpliterator).pushBackLastElement();
            return;
        }
        pushBackRequested = true;
    }

    private Consumer<? super T> getPushBackWrappedAction(Consumer<? super R> action) {
        return t -> getWrappedAction(action).accept(lastElement = t);
    }

    @Override
    public boolean tryAdvance(Consumer<? super R> action) {
        Consumer<? super T> wrappedAction = getPushBackWrappedAction(action);
        return tryAdvancePushBack(wrappedAction) || getWrappedSpliterator().tryAdvance(wrappedAction);
    }

    @Override
    public void forEachRemaining(Consumer<? super R> action) {
        Consumer<? super T> wrappedAction = getPushBackWrappedAction(action);
        //noinspection StatementWithEmptyBody
        while (tryAdvancePushBack(wrappedAction));
        getWrappedSpliterator().forEachRemaining(wrappedAction);
    }

    private boolean tryAdvancePushBack(Consumer<? super T> wrappedAction) {
        if (!pushBackRequested)
            return false;
        wrappedAction.accept(lastElement);
        pushBackRequested = false;
        return true;
    }

    @Override
    public long estimateSize() {
        Spliterator<T> wrappedSpliterator = getWrappedSpliterator();
        return wrappedSize(wrappedSpliterator == null ? 0 : wrappedSpliterator.estimateSize());
    }

    @Override
    public long getExactSizeIfKnown() {
        Spliterator<T> wrappedSpliterator = getWrappedSpliterator();
        return wrappedSize(wrappedSpliterator == null ? 0 : wrappedSpliterator.getExactSizeIfKnown());
    }

    long wrappedSize(long size) {
        return size == -1 ? -1 : size + (pushBackRequested ? 1 : 0);
    }
}
