package webfx.util.numbers.providers;

import webfx.util.numbers.spi.NumbersProvider;

/**
 * NumbersProvider that works with Java ME CLDC (Connected Limited Device Configuration) which doesn't have the
 * java.lang.Number class.
 *
 * @author Bruno Salmon
 */
public final class CldcPlatformNumbers implements NumbersProvider {

    @Override
    public boolean isNumber(Object value) {
        return value != null && (
                value instanceof Integer ||
                value instanceof Long ||
                value instanceof Float ||
                value instanceof Double ||
                value instanceof Byte ||
                value instanceof Short);
    }

    @Override
    public Integer asInteger(Object value) {
        if (value == null)
            return null;
        if (value instanceof Integer)
            return (Integer) value;
        if (value instanceof Long)
            return ((Long) value).intValue();
        if (value instanceof Float)
            return ((Float) value).intValue();
        if (value instanceof Double)
            return ((Double) value).intValue();
        if (value instanceof Byte)
            return ((Byte) value).intValue();
        if (value instanceof Short)
            return ((Short) value).intValue();
        return null;
    }

    @Override
    public Long asLong(Object value) {
        if (value == null)
            return null;
        if (value instanceof Long)
            return (Long) value;
        if (value instanceof Integer)
            return ((Integer) value).longValue();
        if (value instanceof Float)
            return ((Float) value).longValue();
        if (value instanceof Double)
            return ((Double) value).longValue();
        if (value instanceof Byte)
            return ((Byte) value).longValue();
        if (value instanceof Short)
            return ((Short) value).longValue();
        return null;
    }

    @Override
    public Float asFloat(Object value) {
        if (value == null)
            return null;
        if (value instanceof Float)
            return (Float) value;
        if (value instanceof Integer)
            return ((Integer) value).floatValue();
        if (value instanceof Long)
            return ((Long) value).floatValue();
        if (value instanceof Double)
            return ((Double) value).floatValue();
        if (value instanceof Byte)
            return ((Byte) value).floatValue();
        if (value instanceof Short)
            return ((Short) value).floatValue();
        return null;
    }

    @Override
    public Double asDouble(Object value) {
        if (value == null)
            return null;
        if (value instanceof Double)
            return (Double) value;
        if (value instanceof Integer)
            return ((Integer) value).doubleValue();
        if (value instanceof Long)
            return ((Long) value).doubleValue();
        if (value instanceof Float)
            return ((Float) value).doubleValue();
        if (value instanceof Byte)
            return ((Byte) value).doubleValue();
        if (value instanceof Short)
            return ((Short) value).doubleValue();
        return null;
    }

    @Override
    public Byte asByte(Object value) {
        if (value == null)
            return null;
        if (value instanceof Byte)
            return (Byte) value;
        if (value instanceof Integer)
            return ((Integer) value).byteValue();
        if (value instanceof Long)
            return ((Long) value).byteValue();
        if (value instanceof Float)
            return ((Float) value).byteValue();
        if (value instanceof Double)
            return ((Double) value).byteValue();
        if (value instanceof Short)
            return ((Short) value).byteValue();
        return null;
    }

    @Override
    public Short asShort(Object value) {
        if (value == null)
            return null;
        if (value instanceof Short)
            return (Short) value;
        if (value instanceof Integer)
            return ((Integer) value).shortValue();
        if (value instanceof Long)
            return ((Long) value).shortValue();
        if (value instanceof Float)
            return ((Float) value).shortValue();
        if (value instanceof Double)
            return ((Double) value).shortValue();
        if (value instanceof Byte)
            return ((Byte) value).shortValue();
        return null;
    }
}
