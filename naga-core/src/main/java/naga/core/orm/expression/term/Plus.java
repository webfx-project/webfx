package naga.core.orm.expression.term;

import naga.core.orm.expression.Expression;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public class Plus extends ArithmeticExpression {

    public Plus(Expression left, Expression right) {
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
        }
        if (a instanceof Date && b instanceof Number)
            return new Date(((Date) a).getTime() + ((Number) b).longValue());
        Logger.getLogger("expression").warning("Unsupported numeric type " + a.getClass().getName());
        return null; //MismatchErrorValue.singleton;
    }

}
