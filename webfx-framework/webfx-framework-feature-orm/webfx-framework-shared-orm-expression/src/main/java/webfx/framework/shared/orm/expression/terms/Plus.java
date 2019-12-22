package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

/**
 * @author Bruno Salmon
 */
public final class Plus<T> extends PrimitiveBinaryExpression<T> {

    public Plus(Expression<T> left, Expression<T> right) {
        super(left, "+", right, 6);
    }

    public int evaluateInteger(int a, int b) {
        return a + b;
    }

    public long evaluateLong(long a, long b) {
        return a + b;
    }

    public float evaluateFloat(float a, float b) {
        return a + b;
    }

    public double evaluateDouble(double a, double b) {
        return a + b;
    }

    public boolean evaluateBoolean(boolean a, boolean b) {
        return a || b;
    }

    public Object evaluateObject(Object a, Object b) {
        if (a == null /* || a instanceof UnknownValue */)
            return b;
        if (b == null /* || b instanceof UnknownValue */)
            return a;
        if (a instanceof String)
            return ((String) a) + b; // Recycler.concat(a, b);
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
            return bia.add(bib);
        }
        if (a instanceof BigDecimal) {
            BigDecimal bda = (BigDecimal) a;
            BigDecimal bdb;
            if (b instanceof BigDecimal)
                bdb = (BigDecimal) b;
            else
                bdb = new BigDecimal(b.toString());
            return bda.add(bdb);
        }*/
        throw new IllegalArgumentException("Unsupported numeric type " + a.getClass().getName());
    }

}
