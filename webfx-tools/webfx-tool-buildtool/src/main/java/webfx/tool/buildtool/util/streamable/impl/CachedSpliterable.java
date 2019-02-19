package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
final class CachedSpliterable<T> implements Spliterable<T> {

    private Spliterable<T> spliterable;
    private final List<T> cache = new ArrayList<>();

    CachedSpliterable(Spliterable<T> spliterable) {
        this.spliterable = spliterable;
    }

    @Override
    public Spliterator<T> spliterator() {
        return new ActionMapperDelegatingSpliterator<>(spliterable == null ? null : spliterable.spliterator(), action ->
                t -> {
                    cache.add(t);
                    action.accept(t);
                }) {

            private int nextCacheIndex;

            @Override
            protected void onDelegateFullyTraversed() {
                super.onDelegateFullyTraversed();
                spliterable = null;
            }

            private boolean tryAdvanceCache(Consumer<? super T> action) {
                if (nextCacheIndex >= cache.size())
                    return false;
                //System.err.print('|');
                action.accept(cache.get(nextCacheIndex++));
                return true;
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (tryAdvanceCache(action))
                    return true;
                boolean processed = super.tryAdvance(action);
                updateIndexWhenEndOfCacheIsReached(); // Necessary to update the cache index in case the last call added an element
                return processed;
            }

            private void updateIndexWhenEndOfCacheIsReached() {
                nextCacheIndex = cache.size();
            }

            @Override
            public void forEachRemaining(Consumer<? super T> action) {
                //noinspection StatementWithEmptyBody
                while (tryAdvanceCache(action));
                super.forEachRemaining(action);
                updateIndexWhenEndOfCacheIsReached();
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
        };
    }
}
