package naga.framework.expression.terms;

import naga.framework.expression.Expression;
import naga.framework.expression.lci.DataReader;
import naga.commons.type.PrimType;
import naga.commons.type.Type;
import naga.commons.type.Types;
import naga.commons.util.Booleans;
import naga.commons.util.Numbers;
import naga.commons.util.Strings;

import java.util.Date;

/**
 * @author Bruno Salmon
 */
public abstract class PrimitiveBinaryBooleanExpression<T> extends BinaryBooleanExpression<T> {

    public PrimitiveBinaryBooleanExpression(Expression left, String separator, Expression right, int precedenceLevel) {
        super(left, separator, right, precedenceLevel);
    }

    @Override
    public boolean evaluateCondition(Object leftValue, Object rightValue, DataReader<T> dataReader) {
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
                    return evaluateInteger(Numbers.intValue(leftValue), Numbers.intValue(rightValue));
                case LONG:
                    return evaluateLong(Numbers.longValue(leftValue), Numbers.longValue(rightValue));
                case FLOAT:
                    return evaluateFloat(Numbers.floatValue(leftValue), Numbers.floatValue(rightValue));
                case DOUBLE:
                    return evaluateDouble(Numbers.doubleValue(leftValue), Numbers.doubleValue(rightValue));
                case STRING:
                    return evaluateString(Strings.stringValue(leftValue), Strings.stringValue(rightValue));
                case DATE:
                    return evaluateDate((Date) leftValue, (Date) rightValue);
            }
        }
        return evaluateObject(leftValue, rightValue);
    }

    abstract boolean evaluateInteger(int a, int b);
    abstract boolean evaluateLong(long a, long b);
    abstract boolean evaluateFloat(float a, float b);
    abstract boolean evaluateDouble(double a, double b);
    abstract boolean evaluateBoolean(boolean a, boolean b);
    boolean evaluateString(String a, String b) { return evaluateObject(a, b);}
    boolean evaluateDate(Date a, Date b) { return evaluateLong(a.getTime(), b.getTime());}
    abstract boolean evaluateObject(Object a, Object b);


}
