package webfx.tool.buildhelper.util.spliterable.operable.impl;

import webfx.tool.buildhelper.util.spliterable.Spliterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
final class CachedSpliterable<T> implements Spliterable<T> {

    private final Spliterator<T> spliterator;
    private final List<T> cache = new ArrayList<>();

    CachedSpliterable(Spliterator<T> spliterator) {
        this.spliterator = spliterator;
    }

    @Override
    public Spliterator<T> buildSpliterator() {
        return new DelegatingSpliterator<>(spliterator) {

            private int cacheIndex;

            private boolean tryAdvanceCache(Consumer<? super T> action) {
                if (cacheIndex >= cache.size())
                    return false;
                //System.err.print('|');
                action.accept(cache.get(cacheIndex++));
                return true;
            }

            private Consumer<T> cachingAction(Consumer<? super T> action) {
                return t -> {
                    cache.add(t);
                    action.accept(t);
                };
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return tryAdvanceCache(action) || super.tryAdvance(cachingAction(action));
            }

            @Override
            public void forEachRemaining(Consumer<? super T> action) {
                //noinspection StatementWithEmptyBody
                while (tryAdvanceCache(action));
                super.forEachRemaining(cachingAction(action));
            }

            @Override
            public long getExactSizeIfKnown() {
                long exactSize = spliterator.getExactSizeIfKnown();
                if (exactSize != -1)
                    exactSize += cache.size();
                return exactSize;
            }

            @Override
            public long estimateSize() {
                return cache.size() + super.estimateSize();
            }
        };
    }
}
