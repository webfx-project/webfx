package webfx.tool.buildtool.util.reusablestream.impl;

import webfx.tool.buildtool.util.reusablestream.Spliterable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
class FilterOperator<T> extends SpliteratorTransformOperator<T, T> {

    private final Predicate<? super T> predicate;

    FilterOperator(Spliterable<T> operandSpliterable, Predicate<? super T> predicate) {
        super(operandSpliterable, true);
        this.predicate = predicate;
    }

    @Override
    FilterOperation<T> newOperation() {
        return new FilterOperation<>();
    }

    final class FilterOperation<_T extends T> extends SpliteratorTransformOperation<_T, _T> {

        private boolean lastTestResult;
        private final Predicate<? super _T> predicate;

        FilterOperation() {
            this(FilterOperator.this.predicate);
        }

        FilterOperation(Predicate<? super _T> predicate) {
            this.predicate = predicate;
        }

        @Override
        Consumer<? super _T> createMappedAction(Consumer<? super _T> action) {
            return t -> {
                if (lastTestResult = predicate.test(t))
                    action.accept(t);
            };
        }

        @Override
        public boolean tryAdvance(Consumer<? super _T> action) {
            while (super.tryAdvance(action)) {
                if (lastTestResult)
                    return true;
            }
            return false;
        }
    }
}
