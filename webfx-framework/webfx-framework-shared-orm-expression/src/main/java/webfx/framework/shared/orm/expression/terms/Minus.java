package webfx.framework.shared.orm.expression.terms;

import webfx.framework.shared.orm.expression.Expression;

import java.util.Date;

/**
 * @author Bruno Salmon
 */
public final class Minus<T> extends PrimitiveBinaryExpression<T> {

    public Minus(Expression<T> left, Expression<T> right) {
        super(left, "-", right, 6);
    }

    public int evaluateInteger(int a, int b) {
        return a - b;
    }

    public long evaluateLong(long a, long b) {
        return a - b;
    }

    public float evaluateFloat(float a, float b) {
        return a - b;
    }

    public double evaluateDouble(double a, double b) {
        return a - b;
    }

    public boolean evaluateBoolean(boolean a, boolean b) {
        return a != b;
    }

    public Object evaluateObject(Object a, Object b) {
        if (a == null && b == null)
            return null;
        //if (a == null || b == null)
        //    return MismatchErrorValue.singleton;
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
            return bia.subtract(bib);
        }
        if (a instanceof BigDecimal) {
            BigDecimal bda = (BigDecimal) a;
            BigDecimal bdb;
            if (b instanceof BigDecimal)
                bdb = (BigDecimal) b;
            else
                bdb = new BigDecimal(b.toString());
            return bda.subtract(bdb);
        }*/
        if (a instanceof Date && b instanceof Date)
            return ((Date) a).getTime() - ((Date) b).getTime();
        throw new IllegalArgumentException("Unsupported numeric type " + a.getClass().getName());
    }

}
