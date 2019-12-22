package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;
import webfx.platform.shared.util.Numbers;

/**
 * @author Bruno Salmon
 */
public final class Multiply<T> extends PrimitiveBinaryExpression<T> {

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
        /* Unsupported by Codenameone (based on CLDC)
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
        }*/
        throw new IllegalArgumentException("Unsupported numeric type " + a.getClass().getName());
    }

    public boolean isShortcutValue(Object value) {
        return Numbers.isZero(value);
    }

}
