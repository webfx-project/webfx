package naga.framework.expression.terms.function.java;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.framework.expression.terms.Plus;
import naga.framework.expression.terms.function.SqlAggregateFunction;

/**
 * @author Bruno Salmon
 */
public class Sum<T> extends SqlAggregateFunction<T> {

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
