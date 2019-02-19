package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
final class TakeWhileOperator<T> extends SpliteratorTransformOperator<T, T> {

    private final Predicate<? super T> predicate;
    private static Object lastFailedElement;

    TakeWhileOperator(Spliterable<T> operandSpliterable, Predicate<? super T> predicate) {
        super(operandSpliterable);
        this.predicate = predicate;
    }

    @Override
    TakeWhileOperation<T> newOperation() {
        return new TakeWhileOperation<>();
    }

    final class TakeWhileOperation<_T extends T> extends SpliteratorTransformOperation<_T, _T> {

        private boolean lastTestResult = true;

        @Override
        Consumer<? super _T> createMappedAction(Consumer<? super _T> action) {
            return t -> {
                lastFailedElement = null;
                if (lastTestResult &= predicate.test(t))
                    action.accept(t);
                else
                    lastFailedElement = t;
            };
        }

        @Override
        public boolean tryAdvance(Consumer<? super _T> action) {
            if (lastFailedElement != null) {
                getMappedAction(action).accept((_T) lastFailedElement);
                return lastTestResult;
            }
            return super.tryAdvance(action) && lastTestResult;
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
