package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
abstract class Operator<T, R> implements Spliterable<R> {

    Spliterable<T> wrappedSpliterable;
    private final boolean unsized; // true to force characteristics to unsized (ex: FilterOperator)

    Operator(Spliterable<T> wrappedSpliterable) {
        this(wrappedSpliterable, false);
    }

    Operator(Spliterable<T> wrappedSpliterable, boolean unsized) {
        this.wrappedSpliterable = wrappedSpliterable;
        this.unsized = unsized;
    }

    @Override
    public Spliterator<R> spliterator() {
        return newOperation();
    }

    abstract Operation<T, R> newOperation();

    class Operation<_T extends T, _R extends R> extends PushBackSpliterator<_T, _R> {

        private Consumer<? super _R> lastAction;
        private Consumer<? super _T> lastWrappedAction; // mapped action cache (to prevent recreating it on each getMappedAction() call)
        private Spliterator<_T> wrappedSpliterator;

        Operation() {
            getWrappedSpliterator();
        }

        public Spliterator<_T> getWrappedSpliterator() {
            if (wrappedSpliterator == null && wrappedSpliterable != null)
                wrappedSpliterator = (Spliterator<_T>) wrappedSpliterable.spliterator();
            return wrappedSpliterator;
        }

        public Consumer<? super _T> getWrappedAction(Consumer<? super _R> action) {
            return action == lastAction ? lastWrappedAction : (lastWrappedAction = createWrappedAction(lastAction = action));
        }

        Consumer<? super _T> createWrappedAction(Consumer<? super _R> action) {
            return (Consumer<? super _T>) action;
        }

        @Override
        public boolean tryAdvance(Consumer<? super _R> action) {
            boolean processed;
            if (hasWrappedSpliteratorBeenFullyTraversed())
                processed = false;
            else {
                processed = super.tryAdvance(action);
                if (!processed)
                    onWrappedSpliteratorFullyTraversed();
            }
            return processed;
        }

        @Override
        public void forEachRemaining(Consumer<? super _R> action) {
            if (!hasWrappedSpliteratorBeenFullyTraversed()) {
                super.forEachRemaining(action);
                onWrappedSpliteratorFullyTraversed();
            }
        }

        @Override
        public int characteristics() {
            return hasWrappedSpliteratorBeenFullyTraversed() ? 0 : characteristicsConsideringUnsized(getWrappedSpliterator().characteristics());
        }

        private int characteristicsConsideringUnsized(int characteristics) {
            return unsized ? characteristics & ~SIZED : characteristics;
        }

        @Override
        public boolean hasCharacteristics(int characteristics) {
            return !hasWrappedSpliteratorBeenFullyTraversed() && (!unsized || (characteristics & SIZED) == 0) && getWrappedSpliterator().hasCharacteristics(characteristics);
        }

        @Override
        long wrappedSize(long size) {
            long wrappedSize = super.wrappedSize(size);
            return unsized && wrappedSize != 0 ? -1 : wrappedSize;
        }

        void onWrappedSpliteratorFullyTraversed() { // Can be called once the wrapped spliterator has been fully traversed
            wrappedSpliterator = null; // Forgetting the reference to it so it can be garbage collected to release memory usage
        }

        boolean hasWrappedSpliteratorBeenFullyTraversed() {
            return wrappedSpliterator == null;
        }
    }
}
