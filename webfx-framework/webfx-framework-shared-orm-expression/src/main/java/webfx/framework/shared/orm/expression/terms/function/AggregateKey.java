package webfx.framework.shared.orm.expression.terms.function;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class AggregateKey<T> {

    private final int rowNumber;
    private final List<T> aggregates = new ArrayList<>();

    public AggregateKey(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public List<T> getAggregates() {
        return aggregates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AggregateKey<?> aggregateKey = (AggregateKey<?>) o;

        return rowNumber == aggregateKey.rowNumber;
    }

    @Override
    public int hashCode() {
        return rowNumber;
    }
}
