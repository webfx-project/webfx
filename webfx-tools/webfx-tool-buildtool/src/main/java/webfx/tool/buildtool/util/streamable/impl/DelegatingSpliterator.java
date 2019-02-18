package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
abstract class DelegatingSpliterator<T> implements Spliterator<T> {

    private Spliterator<T> delegate;

    DelegatingSpliterator(Spliterator<T> delegate) {
        this.delegate = delegate;
    }

    private void onDelegateFullyTraversed() { // Called when the delegate spliterator is finished (is fully traversed)
        delegate = null; // Forgetting the reference to it so it can be garbage collected to release memory usage
    }

    private boolean hasDelegateBeenFullyTraversed() {
        return delegate == null;
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        boolean processed;
        if (hasDelegateBeenFullyTraversed())
            processed = false;
        else {
            processed = delegate.tryAdvance(action);
            if (!processed)
                onDelegateFullyTraversed();
        }
        return processed;
    }

    @Override
    public Spliterator<T> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public long estimateSize() {
        return hasDelegateBeenFullyTraversed() ? 0 : delegate.estimateSize();
    }

    @Override
    public int characteristics() {
        return hasDelegateBeenFullyTraversed() ? 0 : delegate.characteristics();
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        if (!hasDelegateBeenFullyTraversed()) {
            delegate.forEachRemaining(action);
            onDelegateFullyTraversed();
        }
    }

    @Override
    public long getExactSizeIfKnown() {
        return hasDelegateBeenFullyTraversed() ? 0 : delegate.getExactSizeIfKnown();
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return !hasDelegateBeenFullyTraversed() && delegate.hasCharacteristics(characteristics);
    }

    @Override
    public Comparator<? super T> getComparator() {
        return hasDelegateBeenFullyTraversed() ? null : delegate.getComparator();
    }
}
