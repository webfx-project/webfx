package naga.platform.compression.values;

/**
 * @author Bruno Salmon
 */
public interface ValuesCompressor {

    Object[] compress(Object[] values);

    Object[] uncompress(Object[] compressedValues);

}
