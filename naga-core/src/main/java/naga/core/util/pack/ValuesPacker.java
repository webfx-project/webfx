package naga.core.util.pack;

import java.util.Iterator;

/**
 * @author Bruno Salmon
 */
public interface ValuesPacker {

    void pushValue(Object value);

    Iterator packedValues();

}
