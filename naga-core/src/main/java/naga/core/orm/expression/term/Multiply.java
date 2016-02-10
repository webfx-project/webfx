package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public class Multiply<T> extends ArithmeticExpression<T> {

    public Multiply(Expression<T> left, Expression<T> right) {
        super(left, "*", right, 7);
    }

    @Override
    public int evaluateInteger(int a, int b) {
        return a * b;
    }

    @Override
    public long evaluateLong(long a, long b) {
        return a * b;
    }

    @Override
    public float evaluateFloat(float a, float b) {
        return a * b;
    }

    @Override
    public double evaluateDouble(double a, double b) {
        return a * b;
    }

    @Override
    public boolean evaluateBoolean(boolean a, boolean b) {
        return a && b;
    }

    @Override
    public Object evaluateObject(Object a, Object b) {
        if (a == null || b == null)
            return null;
        if (a instanceof BigInteger) {
            BigInteger bia = (BigInteger) a;
            BigInteger bib;
            if (b instanceof BigInteger)
                bib = (BigInteger) b;
            else if (b instanceof Number)
                bib = BigInteger.valueOf(((Number) b).longValue());
            else
                bib = new BigInteger(b.toString());
            return bia.multiply(bib);
        }
        if (a instanceof BigDecimal) {
            BigDecimal bda = (BigDecimal) a;
            BigDecimal bdb;
            if (b instanceof BigDecimal)
                bdb = (BigDecimal) b;
            else
                bdb = new BigDecimal(b.toString());
            return bda.multiply(bdb);
        }
        Logger.getLogger("expression").warning("Unsupported numeric type " + a.getClass().getName());
        return null; //MismatchErrorValue.singleton;
    }

    public boolean isShortcutValue(Object value) {
        return value instanceof Number && ((Number) value).longValue() == 0;
    }

}
