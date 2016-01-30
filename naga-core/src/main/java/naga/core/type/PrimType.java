package naga.core.type;

import naga.core.util.Objects;

import java.util.Date;

/*
 * @author Bruno Salmon
 */

public enum PrimType implements Type {
    STRING      (String.class),
    BOOLEAN     (Boolean.class),
    // Numbers
    BYTE        (Byte.class),
    SHORT       (Short.class),
    INTEGER     (Integer.class),
    LONG        (Long.class),
    FLOAT       (Float.class),
    DOUBLE      (Double.class),
    // Date
    DATE        (Date.class);

    private final Class javaClass;

    PrimType(Class javaClass) {
        this.javaClass = javaClass;
    }

    public boolean isString() {
        return this == STRING;
    }

    public boolean isBoolean() {
        return this == BOOLEAN;
    }

    public boolean isDate() {
        return this == DATE;
    }

    public boolean isNumber() {
        //return Number.class.isAssignableFrom(javaClass); // compiles with TeaVM but not GWT
        return this == BYTE || this == SHORT || this == INTEGER || this == LONG || this == FLOAT || this == DOUBLE;
    }

    // Static help methods

    public static PrimType fromObject(Object value) {
        if (value instanceof String)
            return STRING;
        if (value instanceof Boolean)
            return BOOLEAN;
        if (value instanceof Number) {
            if (value instanceof Byte)
                return BYTE;
            if (value instanceof Short)
                return SHORT;
            if (value instanceof Integer)
                return INTEGER;
            if (value instanceof Long)
                return LONG;
            if (value instanceof Float)
                return FLOAT;
            if (value instanceof Double)
                return DOUBLE;
        }
        if (value instanceof Date)
            return DATE;
        return null;
    }

    public static boolean areEquivalent(Object o1, Object o2) {
        if (o1 == o2)
            return true;
        if (o1 == null || o2 == null)
            return false;
        if (o1.equals(o2))
            return true;
        boolean o1IsNumber = o1 instanceof Number;
        boolean o2IsNumber = o2 instanceof Number;
        if (o1IsNumber && o2IsNumber)
            return areNumberEquivalent((Number) o1, (Number) o2);
        return Objects.areEquals(o1.toString(), o2.toString());
    }

    public static PrimType getHighestType(PrimType left, PrimType right) {
        return left.ordinal() >= right.ordinal() ? left : right;
    }

    public static boolean areNumberEquivalent(Number n1, Number n2) {
        switch (getHighestType(fromObject(n1), fromObject(n2))) {
            case BYTE :    return n1.byteValue() == n2.byteValue();
            case SHORT :   return n1.shortValue() == n2.shortValue();
            case INTEGER : return n1.intValue() == n2.intValue();
            case LONG :    return n1.longValue() == n2.longValue();
            case FLOAT :   return n1.floatValue() == n2.floatValue();
            default :      return n1.doubleValue() == n2.doubleValue();
        }
    }

    public static boolean isLessThan(Number n1, Number n2) {
        switch (getHighestType(fromObject(n1), fromObject(n2))) {
            case BYTE :    return n1.byteValue() < n2.byteValue();
            case SHORT :   return n1.shortValue() < n2.shortValue();
            case INTEGER : return n1.intValue() < n2.intValue();
            case LONG :    return n1.longValue() < n2.longValue();
            case FLOAT :   return n1.floatValue() < n2.floatValue();
            default :      return n1.doubleValue() < n2.doubleValue();
        }
    }

    public static boolean isGreaterThan(Number n1, Number n2) {
        switch (getHighestType(fromObject(n1), fromObject(n2))) {
            case BYTE :    return n1.byteValue() > n2.byteValue();
            case SHORT :   return n1.shortValue() > n2.shortValue();
            case INTEGER : return n1.intValue() > n2.intValue();
            case LONG :    return n1.longValue() > n2.longValue();
            case FLOAT :   return n1.floatValue() > n2.floatValue();
            default :      return n1.doubleValue() > n2.doubleValue();
        }
    }

    public static Number negate(Number n) {
        switch (fromObject(n)) {
            case BYTE :    return -n.byteValue();
            case SHORT :   return -n.shortValue();
            case INTEGER : return -n.intValue();
            case LONG :    return -n.longValue();
            case FLOAT :   return -n.floatValue();
            default :      return -n.doubleValue();
        }
    }

}
