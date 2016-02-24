package naga.core.util.compression.values;

/**
 * @author Bruno Salmon
 */
public interface ValuesCompressor {

    Object[] packValues(Object[] values);

    Object[] unpackValues(Object[] packedValues);

}
