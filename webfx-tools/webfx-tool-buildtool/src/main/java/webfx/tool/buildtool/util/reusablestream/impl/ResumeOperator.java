package webfx.tool.buildtool.util.reusablestream.impl;

import webfx.tool.buildtool.util.reusablestream.Spliterable;

import java.util.Spliterator;

/**
 * @author Bruno Salmon
 */
final class ResumeOperator<T> extends Operator<T, T> {

    private Spliterator<T> oneUseOperandSpliterator;

    ResumeOperator(Spliterable<T> operandSpliterable) {
        super(operandSpliterable);
    }

    @Override
    public Spliterator<T> spliterator() {
        if (oneUseOperandSpliterator == null)
            oneUseOperandSpliterator = new PushBackSpliterator<>(operandSpliterable.spliterator());
        return oneUseOperandSpliterator;
    }

    @Override
    Operation<T, T> newOperation() {
        return null;
    }

}
