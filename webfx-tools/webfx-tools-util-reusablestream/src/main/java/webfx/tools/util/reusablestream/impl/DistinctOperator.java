package webfx.tools.util.reusablestream.impl;

import webfx.tools.util.reusablestream.Spliterable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;

/**
 * @author Bruno Salmon
 */
final class DistinctOperator<T> extends FilterOperator<T> {

    DistinctOperator(Spliterable<T> wrappedSpliterable) {
        super(wrappedSpliterable, null);
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
