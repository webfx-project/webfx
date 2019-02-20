package webfx.tool.buildtool.util.reusablestream.impl;

import webfx.tool.buildtool.util.reusablestream.Spliterable;

import java.util.Spliterator;

/**
 * @author Bruno Salmon
 */
abstract class Operator<T, R> implements Spliterable<R> {

    Spliterable<T> operandSpliterable;

    Operator(Spliterable<T> operandSpliterable) {
        this.operandSpliterable = operandSpliterable;
    }

    @Override
    public Spliterator<R> spliterator() {
        return newOperation().spliterator();
    }

    abstract Operation<T, R> newOperation();

    abstract class Operation <_T extends T, _R extends R> implements Spliterable<_R> {

        private Spliterator<_T> operandSpliterator;

        Operation() {
            getOperandSpliterator();
        }

        Spliterator<_T> getOperandSpliterator() {
            if (operandSpliterator == null && operandSpliterable != null)
                operandSpliterator = (Spliterator<_T>) operandSpliterable.spliterator();
            return operandSpliterator;
        }

        @Override
        public Spliterator<_R> spliterator() {
            return getResultSpliterator();
        }

        Spliterator<_R> getResultSpliterator() { // Default implementation = no transformation (requires _T = _R)
            return (Spliterator<_R>) getOperandSpliterator();
        }

        void onOperandSpliteratorFullyTraversed() { // Can be called once the provided spliterator has been fully traversed
            operandSpliterator = null; // Forgetting the reference to it so it can be garbage collected to release memory usage
        }

        boolean hasOperandSpliteratorBeenFullyTraversed() {
            return operandSpliterator == null;
        }
    }
}
