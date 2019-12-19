package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Bruno Salmon
 */
class FlatMapOperator<T, R> extends Operator<T, R> {

    private final Function<? super T, ? extends Iterable<? extends R>> mapper;

    FlatMapOperator(Spliterable<T> deferredProvidedSpliteratorGetter, Function<? super T, ? extends Iterable<? extends R>> mapper) {
        super(deferredProvidedSpliteratorGetter, true);
        this.mapper = mapper;
    }

    @Override
    Operation<T,R> newOperation() {
        return new FlatMapOperation<>();
    }

    final class FlatMapOperation<_T extends T, _R extends R> extends Operation<_T, _R> {

        private Spliterator<_R> runningMappedSpliterator;

        @Override
        public boolean tryAdvance(Consumer<? super _R> action) {
            do {
                if (runningMappedSpliterator != null && runningMappedSpliterator.tryAdvance(action))
                    return true;
            } while (goToNextRunningMappedSpliterator());
            return false;
        }

        @Override
        public void forEachRemaining(Consumer<? super _R> action) {
            do {
                if (runningMappedSpliterator != null)
                    runningMappedSpliterator.forEachRemaining(action);
            } while (goToNextRunningMappedSpliterator());
        }

        private boolean goToNextRunningMappedSpliterator() {
            runningMappedSpliterator = null;
            getWrappedSpliterator().tryAdvance(this::setNextRunningMappedSpliterator);
            return runningMappedSpliterator != null;
        }

        private void setNextRunningMappedSpliterator(_T t) {
            runningMappedSpliterator = (Spliterator<_R>) mapper.apply(t).spliterator();
        }
    }
}
