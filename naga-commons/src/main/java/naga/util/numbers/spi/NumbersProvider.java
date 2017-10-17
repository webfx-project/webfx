package naga.util.numbers.spi;

/**
 * @author Bruno Salmon
 */
public interface NumbersProvider {

    boolean isNumber(Object value);

    Integer asInteger(Object value);

    Long asLong(Object value);

    Float asFloat(Object value);

    Double asDouble(Object value);

    Byte asByte(Object value);

    Short asShort(Object value);
}
