package webfx.tool.buildtool.util.streamable.impl;

import webfx.tool.buildtool.util.streamable.Spliterable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
final class DistinctOperator<T> extends FilterOperator<T> {

    DistinctOperator(Spliterable<T> operandSpliterable) {
        super(operandSpliterable, null);
    }

    @Override
    FilterOperation<T> newOperation() {
        return new FilterOperation<T>(new Predicate<>() {

            private final Set<T> values = new HashSet<>();

            @Override
            public boolean test(T t) {
                return values.add(t);
            }
        });
    }
}
