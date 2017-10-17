package naga.util.numbers.providers;

import naga.util.numbers.spi.NumbersProvider;

/**
 * /**
 * NumbersProvider that works with platforms not based on Java ME CLDC (Connected Limited Device Configuration) and which
 * therefore can use the java.lang.Number class.

 * @author Bruno Salmon
 */
public class StandardPlatformNumbers implements NumbersProvider {

    @Override
    public boolean isNumber(Object value) {
        return value instanceof Number;
    }

    @Override
    public Integer asInteger(Object value) {
        return value == null ? null : value instanceof Integer ? (Integer) value : value instanceof Number ? ((Number) value).intValue() : null;
    }

    @Override
    public Long asLong(Object value) {
        return value == null ? null : value instanceof Long ? (Long) value : value instanceof Number ? ((Number) value).longValue() : null;
    }

    @Override
    public Float asFloat(Object value) {
        return value == null ? null : value instanceof Float ? (Float) value : value instanceof Number ? ((Number) value).floatValue() : null;
    }

    @Override
    public Double asDouble(Object value) {
        return value == null ? null : value instanceof Double ? (Double) value : value instanceof Number ? ((Number) value).doubleValue() : null;
    }

    @Override
    public Byte asByte(Object value) {
        return value == null ? null : value instanceof Byte ? (Byte) value : value instanceof Number ? ((Number) value).byteValue() : null;
    }

    @Override
    public Short asShort(Object value) {
        return value == null ? null : value instanceof Short ? (Short) value : value instanceof Number ? ((Number) value).shortValue() : null;
    }
}
