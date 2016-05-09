package naga.core.jsoncodec;

import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;

/*
 * @author Bruno Salmon
 */

public interface JsonCodec<T> {

    String getCodecID();

    void encodeToJson(T javaObject, WritableCompositeObject json);

    T decodeFromJson(CompositeObject json);

}
