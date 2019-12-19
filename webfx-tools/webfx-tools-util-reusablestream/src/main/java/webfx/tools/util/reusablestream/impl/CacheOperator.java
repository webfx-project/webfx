package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
final class CacheOperator<T> extends Operator<T, T> {

    private final List<T> cache = new ArrayList<>();
    private Spliterator<T> oneSinglePassWrappedSpliterator;

    CacheOperator(Spliterable<T> wrappedSpliterable) {
        super(wrappedSpliterable);
    }

    @Override
    CacheOperation<T> newOperation() {
        if (oneSinglePassWrappedSpliterator == null && wrappedSpliterable != null)
            oneSinglePassWrappedSpliterator = wrappedSpliterable.spliterator();
        wrappedSpliterable = null;
        return new CacheOperation<>();
    }

    final class CacheOperation<_T extends T> extends Operation<_T, _T> {

        private int nextCacheIndex;

        @Override
        public Spliterator<_T> getWrappedSpliterator() {
            return (Spliterator<_T>) oneSinglePassWrappedSpliterator;
        }

        @Override
        Consumer<? super _T> createWrappedAction(Consumer<? super _T> action) {
            return t -> {
                cache.add(t);
                action.accept(t);
            };
        }

        @Override
        public boolean tryAdvance(Consumer<? super _T> action) {
            if (tryAdvanceCache(action))
                return true;
            boolean processed = super.tryAdvance(action);
            updateIndexWhenEndOfCacheIsReached(); // Necessary to update the cache index in case the last call added an element
            return processed;
        }

        @Override
        public void forEachRemaining(Consumer<? super _T> action) {
            //noinspection StatementWithEmptyBody
            while (tryAdvanceCache(action));
            super.forEachRemaining(action);
            updateIndexWhenEndOfCacheIsReached();
        }

        private boolean tryAdvanceCache(Consumer<? super _T> action) {
            if (nextCacheIndex >= cache.size())
                return false;
            //System.err.print('|');
            action.accept((_T) cache.get(nextCacheIndex++));
            return true;
        }

        private void updateIndexWhenEndOfCacheIsReached() {
            nextCacheIndex = cache.size();
        }

        @Override
        public long getExactSizeIfKnown() {
            return cacheAugmentedSize(super.getExactSizeIfKnown());
        }

        @Override
        public long estimateSize() {
            return cacheAugmentedSize(super.estimateSize());
        }

        private long cacheAugmentedSize(long size) {
            return size == -1 ? -1 : size + cache.size();
        }

        @Override
        protected void onWrappedSpliteratorFullyTraversed() {
            oneSinglePassWrappedSpliterator = null;
        }

        @Override
        boolean hasWrappedSpliteratorBeenFullyTraversed() {
            return oneSinglePassWrappedSpliterator == null;
        }
    }
}
