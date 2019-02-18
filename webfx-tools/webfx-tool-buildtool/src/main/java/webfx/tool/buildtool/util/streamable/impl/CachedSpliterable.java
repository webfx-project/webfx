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

    private final Spliterator<T> spliterator;
    private final List<T> cache = new ArrayList<>();

    CachedSpliterable(Spliterator<T> spliterator) {
        this.spliterator = spliterator;
    }

    @Override
    public Spliterator<T> spliterator() {
        return new ActionMapperDelegatingSpliterator<>(spliterator, action ->
                t -> {
                    cache.add(t);
                    action.accept(t);
                }) {

            private int cacheIndex;

            private boolean tryAdvanceCache(Consumer<? super T> action) {
                if (cacheIndex >= cache.size())
                    return false;
                //System.err.print('|');
                action.accept(cache.get(cacheIndex++));
                return true;
            }

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return tryAdvanceCache(action) || super.tryAdvance(action);
            }

            @Override
            public void forEachRemaining(Consumer<? super T> action) {
                //noinspection StatementWithEmptyBody
                while (tryAdvanceCache(action)) ;
                super.forEachRemaining(action);
            }

            @Override
            public long getExactSizeIfKnown() {
                return sizePlusCache(super.getExactSizeIfKnown());
            }

            @Override
            public long estimateSize() {
                return sizePlusCache(super.estimateSize());
            }

            private long sizePlusCache(long size) {
                return size == -1 ? -1 : size + cache.size();
            }
        };
    }
}
