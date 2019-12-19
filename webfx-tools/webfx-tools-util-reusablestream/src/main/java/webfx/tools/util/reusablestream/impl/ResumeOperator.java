package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.Spliterator;

/**
 * @author Bruno Salmon
 */
final class ResumeOperator<T> extends Operator<T, T> {

    private Spliterator<T> singleOperation;

    ResumeOperator(Spliterable<T> wrappedSpliterable) {
        super(wrappedSpliterable);
    }

    @Override
    public Spliterator<T> spliterator() {
        if (singleOperation == null)
            singleOperation = newOperation();
        return singleOperation;
    }

    @Override
    ResumeOperation<T> newOperation() {
        return new ResumeOperation<>();
    }

    private final class ResumeOperation<_T extends T> extends Operation<_T, _T> {

        @Override
        void pushBackLastElement() {
            pushBackRequested = true;
        }
    }

}
