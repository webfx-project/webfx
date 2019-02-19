package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
final class CacheOperator<T> extends SpliteratorTransformOperator<T, T> {

    private final List<T> cache = new ArrayList<>();
    private Spliterator<T> oneUseOperandSpliterator;

    CacheOperator(Spliterable<T> operandSpliterable) {
        super(operandSpliterable);
    }

    @Override
    CacheOperation<T> newOperation() {
        if (oneUseOperandSpliterator == null && operandSpliterable != null)
            oneUseOperandSpliterator = operandSpliterable.spliterator();
        operandSpliterable = null;
        return new CacheOperation<>();
    }

    final class CacheOperation<_T extends T> extends SpliteratorTransformOperation<_T, _T> {

        private int nextCacheIndex;

        @Override
        Consumer<? super _T> createMappedAction(Consumer<? super _T> action) {
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
        Spliterator<_T> getOperandSpliterator() {
            return (Spliterator<_T>) oneUseOperandSpliterator;
        }

        @Override
        protected void onOperandSpliteratorFullyTraversed() {
            oneUseOperandSpliterator = null;
        }

        @Override
        boolean hasOperandSpliteratorBeenFullyTraversed() {
            return oneUseOperandSpliterator == null;
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
    }
}
