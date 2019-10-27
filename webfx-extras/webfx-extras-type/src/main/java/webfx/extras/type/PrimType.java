package webfx.extras.type;

import java.util.Date;

/**
 * @author Bruno Salmon
 */

public enum PrimType implements Type {
    STRING,
    BOOLEAN,
    // Numbers
    BYTE,
    SHORT,
    INTEGER,
    LONG,
    FLOAT,
    DOUBLE,
    // Date
    DATE;

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
        if (value instanceof Integer)
            return INTEGER;
        if (value instanceof Long)
            return LONG;
        if (value instanceof Date)
            return DATE;
        if (value instanceof Float)
            return FLOAT;
        if (value instanceof Double)
            return DOUBLE;
        if (value instanceof Byte)
            return BYTE;
        if (value instanceof Short)
            return SHORT;
        return null;
    }

    public static PrimType getHighestType(PrimType left, PrimType right) {
        return left.ordinal() >= right.ordinal() ? left : right;
    }

}
