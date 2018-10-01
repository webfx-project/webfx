package webfx.framework.expression.terms.function.java;

import webfx.fxkits.extra.type.PrimType;
import webfx.platform.shared.util.Strings;
import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;
import webfx.framework.expression.terms.ExpressionArray;
import webfx.framework.expression.terms.function.SqlAggregateFunction;

/**
 * @author Bruno Salmon
 */
public final class StringAgg<T> extends SqlAggregateFunction<T> {

    public StringAgg() {
        super("string_agg", null, null, PrimType.STRING, true);
    }

    @Override
    public Object evaluateOnAggregates(T referrer, Object[] aggregates, Expression<T> operand, DataReader<T> dataReader) {
        String delimiter = ",";
        Expression<T> stringOperand = operand;
        if (operand instanceof ExpressionArray) {
            ExpressionArray<T> array = (ExpressionArray<T>) operand;
            stringOperand = array.getExpressions()[0];
            Expression<T> delimiterOperand = array.getExpressions()[1];
            delimiter = Strings.toSafeString(delimiterOperand.evaluate(referrer, dataReader));
        }
        StringBuilder sb = new StringBuilder();
        for (Object aggregate : aggregates) {
            Object value = stringOperand.evaluate((T) aggregate, dataReader);
            if (value != null) {
                if (sb.length() > 0)
                    sb.append(delimiter);
                sb.append(value);
            }
        }
        return sb.toString();
    }
}
