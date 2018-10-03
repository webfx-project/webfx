package webfx.framework.shared.expression.terms.function.java;

import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.lci.DataReader;
import webfx.framework.shared.expression.terms.Plus;
import webfx.framework.shared.expression.terms.function.SqlAggregateFunction;

/**
 * @author Bruno Salmon
 */
public final class Sum<T> extends SqlAggregateFunction<T> {

    public Sum() {
        super("sum", null, null, null, true);
    }

    @Override
    public Object evaluateOnAggregates(T referrer, Object[] aggregates, Expression<T> operand, DataReader<T> dataReader) {
        Object result = null;
        Plus<T> plus = new Plus<>(operand, operand);
        for (Object aggregate : aggregates)
            result = plus.evaluate(result, operand.evaluate((T) aggregate, dataReader), dataReader);
        return result;
    }
}
