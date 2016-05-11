package naga.core.json.codec;

/*
 * @author Bruno Salmon
 */

public abstract class AbstractJsonCodec<T> implements JsonCodec<T> {

    private final String codecId;

    public AbstractJsonCodec(Class<? extends T> javaClass, String codecId) {
        this.codecId = codecId;
        JsonCodecManager.registerJsonCodec(javaClass, this);
    }

    @Override
    public String getCodecId() {
        return codecId;
    }

}
