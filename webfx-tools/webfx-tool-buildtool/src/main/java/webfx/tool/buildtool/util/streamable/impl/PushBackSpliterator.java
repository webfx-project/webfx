package webfx.tool.buildtool.util.streamable.impl;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
final class PushBackSpliterator<T> implements WrappingSpliterator<T> {

    private final Spliterator<T> wrappedSpliterator;
    private Deque<T> pushBackDeque;

    PushBackSpliterator(Spliterator<T> wrappedSpliterator) {
        this.wrappedSpliterator = wrappedSpliterator;
    }

    @Override
    public Spliterator<T> getWrappedSpliterator() {
        return wrappedSpliterator;
    }

    void pushBackElement(T element) {
        if (pushBackDeque == null)
            pushBackDeque = new ArrayDeque<>();
        pushBackDeque.add(element);
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return tryAdvancePushBack(action) || getWrappedSpliterator().tryAdvance(action);
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        //noinspection StatementWithEmptyBody
        while (tryAdvancePushBack(action));
        getWrappedSpliterator().forEachRemaining(action);
    }

    private boolean tryAdvancePushBack(Consumer<? super T> action) {
        if (pushBackDeque == null)
            return false;
        action.accept(pushBackDeque.removeLast());
        if (pushBackDeque.isEmpty())
            pushBackDeque = null;
        return true;
    }

    @Override
    public long estimateSize() {
        return augmentedSize(getWrappedSpliterator().estimateSize());
    }

    @Override
    public long getExactSizeIfKnown() {
        return augmentedSize(getWrappedSpliterator().getExactSizeIfKnown());
    }

    private long augmentedSize(long size) {
        return size == -1 ? -1 : size + (pushBackDeque == null ? 0 : pushBackDeque.size());
    }
}
