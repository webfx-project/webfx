package naga.core.buscall;

import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;
import naga.core.codec.AbstractCompositeCodec;
import naga.core.codec.CompositeCodecManager;
import naga.core.util.async.AsyncResult;

/**
 * @author Bruno Salmon
 */
class SerializableAsyncResult<T> implements AsyncResult<T> {

    private final T result;
    private final Throwable cause;

    public static <T> SerializableAsyncResult<T> getSerializableAsyncResult(AsyncResult<T> asyncResult) {
        if (asyncResult == null || asyncResult instanceof SerializableAsyncResult)
            return (SerializableAsyncResult<T>) asyncResult;
        return new SerializableAsyncResult<T>(asyncResult);
    }

    public SerializableAsyncResult(AsyncResult<T> asyncResult) {
        this(asyncResult.result(), asyncResult.cause());
    }

    public SerializableAsyncResult(T result, Throwable cause) {
        this.result = result;
        this.cause = cause;
    }

    @Override
    public T result() {
        return result;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public boolean succeeded() {
        return cause == null;
    }

    @Override
    public boolean failed() {
        return cause != null;
    }

    /****************************************************
     *                 Composite Codec                  *
     *                 Composite Codec                  *
     * *************************************************/

    private static String CODEC_ID = "asyncRes";
    private static String RESULT_KEY = "res";
    private static String ERROR_KEY = "err";

    public static void registerCompositeCodec() {
        new AbstractCompositeCodec<SerializableAsyncResult>(SerializableAsyncResult.class, CODEC_ID) {

            @Override
            public void encodeToComposite(SerializableAsyncResult result, WritableCompositeObject co) {
                if (result.cause() != null)
                    co.set(ERROR_KEY, result.cause().getMessage());
                if (result.result() != null)
                    co.set(RESULT_KEY, CompositeCodecManager.encodeToCompositeObject(result.result()));
            }

            @Override
            public SerializableAsyncResult decodeFromComposite(CompositeObject co) {
                String errorMessage = co.getString(ERROR_KEY);
                Exception error = errorMessage == null ? null : new Exception(errorMessage);
                return new SerializableAsyncResult<>(
                        CompositeCodecManager.decodeFromCompositeObject(co.getObject(RESULT_KEY)),
                        error
                );
            }
        };
    }
}
