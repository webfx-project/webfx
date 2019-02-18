package webfx.tool.buildtool.util.streamable.impl;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
abstract class DelegatingSpliterator<T> implements Spliterator<T> {

    private Spliterator<T> delegate;
    private final boolean unsized; // true to force characteristics to unsized (ex: FilteredSpliterator)

    DelegatingSpliterator(Spliterator<T> delegate) {
        this(delegate, false);
    }

    DelegatingSpliterator(Spliterator<T> delegate, boolean unsized) {
        this.delegate = delegate;
        this.unsized = unsized;
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
    public void forEachRemaining(Consumer<? super T> action) {
        if (!hasDelegateBeenFullyTraversed()) {
            delegate.forEachRemaining(action);
            onDelegateFullyTraversed();
        }
    }

    @Override
    public int characteristics() {
        return hasDelegateBeenFullyTraversed() ? 0 : characteristicsConsideringUnsized(delegate.characteristics());
    }

    private int characteristicsConsideringUnsized(int characteristics) {
        return unsized ? characteristics & ~SIZED : characteristics;
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return !hasDelegateBeenFullyTraversed() && (!unsized || (characteristics & SIZED) == 0) && delegate.hasCharacteristics(characteristics);
    }

    @Override
    public long estimateSize() {
        return hasDelegateBeenFullyTraversed() ? 0 : sizeConsideringUnsized(delegate.estimateSize());
    }

    private long sizeConsideringUnsized(long size) {
        return unsized && size != 0 ? -1 : size;
    }

    @Override
    public long getExactSizeIfKnown() {
        return hasDelegateBeenFullyTraversed() ? 0 : sizeConsideringUnsized(delegate.getExactSizeIfKnown());
    }

    @Override
    public Spliterator<T> trySplit() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comparator<? super T> getComparator() {
        return hasDelegateBeenFullyTraversed() ? null : delegate.getComparator();
    }
}
