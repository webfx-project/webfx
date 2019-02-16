package webfx.tool.buildtool.util.streamable.impl;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
class FilteredSpliterator<T> extends DelegatingSpliterator<T> {

    private final Predicate<? super T> predicate;

    FilteredSpliterator(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        super(spliterator);
        this.predicate = predicate;
    }

    private Consumer<? super T> filteredAction(Consumer<? super T> action) {
        return t -> {
            if (predicate.test(t))
                action.accept(t);
        };
    }

    @Override
    public boolean tryAdvance(Consumer<? super T> action) {
        return super.tryAdvance(filteredAction(action));
    }

    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        super.forEachRemaining(filteredAction(action));
    }

    @Override
    public int characteristics() {
        return super.characteristics() & ~SIZED;
    }

    @Override
    public long getExactSizeIfKnown() {
        if (super.getExactSizeIfKnown() == 0)
            return 0;
        return -1;
    }

    @Override
    public boolean hasCharacteristics(int characteristics) {
        return (characteristics & SIZED) == 0 && super.hasCharacteristics(characteristics);
    }
}
