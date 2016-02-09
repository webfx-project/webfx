package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;
import naga.core.type.PrimType;
import naga.core.type.Type;
import naga.core.type.Types;
import naga.core.util.Booleans;
import naga.core.util.Numbers;
import naga.core.util.Strings;

/**
 * @author Bruno Salmon
 */
public abstract class ArithmeticExpression extends BinaryExpression {

    public ArithmeticExpression(Expression left, String separator, Expression right, int precedenceLevel) {
        super(left, separator, right, precedenceLevel);
    }

    @Override
    public Object evaluate(Object leftValue, Object rightValue) {
        Type type = left != null ? getType() : Types.guessType(leftValue); // left may be null for generic MULTIPLIER and DIVIDER
        PrimType primType = Types.getPrimType(type);
        if (primType != null)
            switch (primType) {
                case BOOLEAN: return evaluateBoolean(Booleans.booleanValue(leftValue), Booleans.booleanValue(rightValue));
                case INTEGER: return evaluateInteger(Numbers.intValue(leftValue), Numbers.intValue(rightValue));
                case LONG:    return evaluateLong(Numbers.longValue(leftValue), Numbers.longValue(rightValue));
                case FLOAT:   return evaluateFloat(Numbers.floatValue(leftValue), Numbers.floatValue(rightValue));
                case DOUBLE:  return evaluateDouble(Numbers.doubleValue(leftValue), Numbers.doubleValue(rightValue));
                case STRING:  return evaluateString(Strings.stringValue(leftValue), Strings.stringValue(rightValue));
            }
        return evaluateObject(leftValue, rightValue);
    }

    abstract int evaluateInteger(int a, int b);
    abstract long evaluateLong(long a, long b);
    abstract float evaluateFloat(float a, float b);
    abstract double evaluateDouble(double a, double b);
    abstract boolean evaluateBoolean(boolean a, boolean b);
    String evaluateString(String a, String b) { return (String) evaluateObject(a, b);}
    abstract Object evaluateObject(Object a, Object b);
}
