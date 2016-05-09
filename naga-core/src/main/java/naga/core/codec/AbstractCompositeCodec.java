package naga.core.codec;

/*
 * @author Bruno Salmon
 */

public abstract class AbstractCompositeCodec<T> implements CompositeCodec<T> {

    private final String codecId;

    public AbstractCompositeCodec(Class<? extends T> javaClass, String codecId) {
        this.codecId = codecId;
        CompositeCodecManager.registerCompositeCodec(javaClass, this);
    }

    @Override
    public String getCodecId() {
        return codecId;
    }

}
