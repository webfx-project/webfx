package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
class FilterOperator<T> extends Operator<T, T> {

    private final Predicate<? super T> predicate;

    FilterOperator(Spliterable<T> wrappedSpliterable, Predicate<? super T> predicate) {
        super(wrappedSpliterable, true);
        this.predicate = predicate;
    }

    @Override
    FilterOperation<T> newOperation() {
        return new FilterOperation<>();
    }

    final class FilterOperation<_T extends T> extends Operation<_T, _T> {

        private boolean lastTestResult;
        private final Predicate<? super _T> predicate;

        FilterOperation() {
            this(FilterOperator.this.predicate);
        }

        FilterOperation(Predicate<? super _T> predicate) {
            this.predicate = predicate;
        }

        @Override
        Consumer<? super _T> createWrappedAction(Consumer<? super _T> action) {
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
