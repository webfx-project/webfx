package webfx.tool.buildtool.util.reusablestream.impl;

import webfx.tool.buildtool.util.reusablestream.Spliterable;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
final class TakeWhileOperator<T> extends SpliteratorTransformOperator<T, T> {

    private final Predicate<? super T> predicate;

    TakeWhileOperator(Spliterable<T> operandSpliterable, Predicate<? super T> predicate) {
        super(operandSpliterable);
        this.predicate = predicate;
    }

    @Override
    TakeWhileOperation<T> newOperation() {
        return new TakeWhileOperation<>();
    }

    final class TakeWhileOperation<_T extends T> extends SpliteratorTransformOperation<_T, _T> {

        private _T rejectedElement;

        @Override
        Consumer<? super _T> createMappedAction(Consumer<? super _T> action) {
            return t -> {
                if (predicate.test(t))
                    action.accept(t);
                else {
                    Spliterator<_T> operandSpliterator = getOperandSpliterator();
                    if (operandSpliterator instanceof PushBackSpliterator)
                        ((PushBackSpliterator<T>) operandSpliterator).pushBackElement(rejectedElement = t);
                }
            };
        }

        @Override
        public boolean tryAdvance(Consumer<? super _T> action) {
            if (rejectedElement != null)
                onOperandSpliteratorFullyTraversed();
            else if (!super.tryAdvance(action))
                return false;
            else if (rejectedElement != null)
                onOperandSpliteratorFullyTraversed();
            return rejectedElement == null;
        }

        @Override
        public void forEachRemaining(Consumer<? super _T> action) {
            if (!hasOperandSpliteratorBeenFullyTraversed()) {
                //noinspection StatementWithEmptyBody
                while (tryAdvance(action)) ;
                onOperandSpliteratorFullyTraversed();
            }
        }
    }
}
