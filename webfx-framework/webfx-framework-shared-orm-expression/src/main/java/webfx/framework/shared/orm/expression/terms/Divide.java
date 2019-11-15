package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class Divide<T> extends PrimitiveBinaryExpression<T> {

    public Divide(Expression<T> left, Expression<T> right) {
        super(left, "/", right, 7);
    }

    @Override
    public int evaluateInteger(int a, int b) {
        return a / b;
    }

    @Override
    public long evaluateLong(long a, long b) {
        return a / b;
    }

    @Override
    public float evaluateFloat(float a, float b) {
        return a / b;
    }

    @Override
    public double evaluateDouble(double a, double b) {
        return a / b;
    }

    @Override
    public boolean evaluateBoolean(boolean a, boolean b) {
        return a;
    }

    @Override
    public Object evaluateObject(Object a, Object b) {
        if (a == null)
            return null;
        /* Unsupported by Codenameone
        if (a instanceof BigInteger) {
            BigInteger bia = (BigInteger) a;
            BigInteger bib;
            if (b instanceof BigInteger)
                bib = (BigInteger) b;
            else if (b instanceof Number)
                bib = BigInteger.valueOf(((Number) b).longValue());
            else
                bib = new BigInteger(b.toString());
            return bia.divide(bib);
        }
        if (a instanceof BigDecimal) {
            BigDecimal bda = (BigDecimal) a;
            BigDecimal bdb;
            if (b instanceof BigDecimal)
                bdb = (BigDecimal) b;
            else
                bdb = new BigDecimal(b.toString());
            return bda.divide(bdb, BigDecimal.ROUND_UP);
        }*/
        throw new IllegalArgumentException("Unsupported numeric type " + a.getClass().getName());
    }
}
