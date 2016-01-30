package naga.core.jsoncodec;

/*
 * @author Bruno Salmon
 */

public abstract class AbstractJsonCodec<T> implements JsonCodec<T> {

    private final String codecID;

    public AbstractJsonCodec(Class<? extends T> javaClass, String codecID) {
        this.codecID = codecID;
        JsonCodecManager.registerJsonCodec(javaClass, this);
    }

    @Override
    public String getCodecID() {
        return codecID;
    }

}
