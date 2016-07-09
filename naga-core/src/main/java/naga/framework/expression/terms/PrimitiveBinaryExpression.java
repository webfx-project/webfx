package naga.framework.expression.terms;

import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.commons.util.Booleans;
import naga.commons.util.Dates;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public abstract class PrimitiveBinaryExpression<T> extends BinaryExpression<T> {

    public PrimitiveBinaryExpression(Expression<T> left, String separator, Expression<T> right, int precedenceLevel) {
        super(left, separator, right, precedenceLevel);
    }

    @Override
    public Object evaluate(Object leftValue, Object rightValue, DataReader<T> dataReader) {
        Expression<T> left = getLeft();
        Type leftType = left != null ? left.getType() : Types.guessType(leftValue); // left may be null for generic MULTIPLIER and DIVIDER
        PrimType leftPrimType = Types.getPrimType(leftType);
        if (leftPrimType != null) {
            leftValue = dataReader.prepareValueBeforeTypeConversion(leftValue, leftPrimType);
            rightValue = dataReader.prepareValueBeforeTypeConversion(rightValue, leftPrimType);
            switch (leftPrimType) {
                case BOOLEAN:
                    return evaluateBoolean(Booleans.booleanValue(leftValue), Booleans.booleanValue(rightValue));
                case INTEGER:
                    return evaluateInteger(Numbers.toInteger(leftValue), Numbers.toInteger(rightValue));
                case LONG:
                    return evaluateLong(Numbers.toLong(leftValue), Numbers.toLong(rightValue));
                case FLOAT:
                    return evaluateFloat(Numbers.toFloat(leftValue), Numbers.toFloat(rightValue));
                case DOUBLE:
                    return evaluateDouble(Numbers.toDouble(leftValue), Numbers.toDouble(rightValue));
                case STRING:
                    return evaluateString(Strings.toString(leftValue), Strings.toString(rightValue));
                case DATE:
                    return evaluateDate(Dates.toInstant(leftValue), Dates.toInstant(rightValue));
            }
        }
        return evaluateObject(leftValue, rightValue);
    }

    protected Boolean evaluateBoolean(Boolean a, Boolean b) {
        return a == null | b == null ? null : evaluateBoolean(a.booleanValue(), b.booleanValue());
    }

    protected Integer evaluateInteger(Integer a, Integer b) {
        return a == null | b == null ? null : evaluateInteger(a.intValue(), b.intValue());
    }

    protected Long evaluateLong(Long a, Long b) {
        return a == null | b == null ? null : evaluateLong(a.longValue(), b.longValue());
    }

    protected Float evaluateFloat(Float a, Float b) {
        return a == null | b == null ? null : evaluateFloat(a.floatValue(), b.floatValue());
    }

    protected Double evaluateDouble(Float a, Float b) {
        return a == null | b == null ? null : evaluateDouble(a.doubleValue(), b.doubleValue());
    }

    protected String evaluateString(String a, String b) { return Strings.toString(evaluateObject(a, b));}

    protected Instant evaluateDate(Instant a, Instant b) { return Instant.ofEpochMilli(evaluateLong(a.toEpochMilli(), b.toEpochMilli()));}

    protected abstract boolean evaluateBoolean(boolean a, boolean b);
    protected abstract int evaluateInteger(int a, int b);
    protected abstract long evaluateLong(long a, long b);
    protected abstract float evaluateFloat(float a, float b);
    protected abstract double evaluateDouble(double a, double b);
    protected abstract Object evaluateObject(Object a, Object b);
}
