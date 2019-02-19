package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.Comparator;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
abstract class SpliteratorTransformOperator<T, R> extends Operator<T, R> {

    private final boolean unsized; // true to force characteristics to unsized (ex: FilterOperator)

    SpliteratorTransformOperator(Spliterable<T> operandSpliterable) {
        this(operandSpliterable, false);
    }

    SpliteratorTransformOperator(Spliterable<T> operandSpliterable, boolean unsized) {
        super(operandSpliterable);
        this.unsized = unsized;
    }

    @Override
    abstract SpliteratorTransformOperation<T, R> newOperation();

    abstract class SpliteratorTransformOperation<_T extends T, _R extends R> extends Operation<_T, _R> implements Spliterator<_R> {

        private Consumer<? super _R> lastAction;
        private Consumer<? super _T> lastMappedAction; // mapped action cache (to prevent recreating it on each getMappedAction() call)

        @Override
        Spliterator<_R> getResultSpliterator() {
            return this;
        }

        @Override
        public boolean tryAdvance(Consumer<? super _R> action) {
            boolean processed;
            if (hasOperandSpliteratorBeenFullyTraversed())
                processed = false;
            else {
                processed = getOperandSpliterator().tryAdvance(getMappedAction(action));
                if (!processed)
                    onOperandSpliteratorFullyTraversed();
            }
            return processed;
        }

        @Override
        public void forEachRemaining(Consumer<? super _R> action) {
            if (!hasOperandSpliteratorBeenFullyTraversed()) {
                getOperandSpliterator().forEachRemaining(getMappedAction(action));
                onOperandSpliteratorFullyTraversed();
            }
        }

        Consumer<? super _T> getMappedAction(Consumer<? super _R> action) {
            return action == lastAction ? lastMappedAction : (lastMappedAction = createMappedAction(lastAction = action));
        }

        Consumer<? super _T> createMappedAction(Consumer<? super _R> action) {
            return (Consumer<? super _T>) action;
        }

        @Override
        public int characteristics() {
            return hasOperandSpliteratorBeenFullyTraversed() ? 0 : characteristicsConsideringUnsized(getOperandSpliterator().characteristics());
        }

        private int characteristicsConsideringUnsized(int characteristics) {
            return unsized ? characteristics & ~SIZED : characteristics;
        }

        @Override
        public boolean hasCharacteristics(int characteristics) {
            return !hasOperandSpliteratorBeenFullyTraversed() && (!unsized || (characteristics & SIZED) == 0) && getOperandSpliterator().hasCharacteristics(characteristics);
        }

        @Override
        public long estimateSize() {
            return hasOperandSpliteratorBeenFullyTraversed() ? 0 : sizeConsideringUnsized(getOperandSpliterator().estimateSize());
        }

        private long sizeConsideringUnsized(long size) {
            return unsized && size != 0 ? -1 : size;
        }

        @Override
        public long getExactSizeIfKnown() {
            return hasOperandSpliteratorBeenFullyTraversed() ? 0 : sizeConsideringUnsized(getOperandSpliterator().getExactSizeIfKnown());
        }

        @Override
        public Spliterator<_R> trySplit() {
            throw new UnsupportedOperationException();
        }

        @Override
        public Comparator<? super _R> getComparator() {
            return hasOperandSpliteratorBeenFullyTraversed() ? null : (Comparator<? super _R>) getOperandSpliterator().getComparator();
        }
    }
}
