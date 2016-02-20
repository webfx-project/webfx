package naga.core.util.pack;

import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public interface ValuesUnpacker {

    int unpackedSize();

    Iterator unpackedValues();

}
