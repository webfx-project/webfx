package webfx.framework.expression.terms;

import webfx.framework.expression.Expression;
import webfx.framework.expression.lci.DataReader;
import webfx.fxkits.extra.type.PrimType;
import webfx.fxkits.extra.type.Type;

/**
 * @author Bruno Salmon
 */
public abstract class BinaryBooleanExpression<T> extends BinaryExpression<T> {

    protected BinaryBooleanExpression(Expression<T> left, String separator, Expression<T> right, int precedenceLevel) {
        super(left, separator, right, precedenceLevel);
    }

    @Override
    public Type getType() {
        return PrimType.BOOLEAN;
    }

    @Override
    public Object evaluate(Object leftValue, Object rightValue, DataReader<T> dataReader) {
        return evaluateCondition(leftValue, rightValue, dataReader);
    }

    public abstract Boolean evaluateCondition(Object a, Object b, DataReader<T> dataReader);

}
