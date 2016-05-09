package naga.core.composite.codec;

import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;

/*
 * @author Bruno Salmon
 */

public interface CompositeCodec<T> {

    String getCodecId();

    void encodeToComposite(T javaObject, WritableCompositeObject co);

    T decodeFromComposite(CompositeObject co);

}
