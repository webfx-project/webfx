package webfx.platforms.core.services.buscall;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.json.codec.AbstractJsonCodec;
import webfx.platforms.core.services.json.codec.JsonCodecManager;
import webfx.platforms.core.util.async.AsyncResult;

/**
 * @author Bruno Salmon
 */
public class SerializableAsyncResult<T> implements AsyncResult<T> {

    private final T result;
    private final Throwable cause;

    static <T> SerializableAsyncResult<T> getSerializableAsyncResult(AsyncResult<T> asyncResult) {
        if (asyncResult == null || asyncResult instanceof SerializableAsyncResult)
            return (SerializableAsyncResult<T>) asyncResult;
        return new SerializableAsyncResult<>(asyncResult);
    }

    private SerializableAsyncResult(AsyncResult<T> asyncResult) {
        this(asyncResult.result(), asyncResult.cause());
    }

    private SerializableAsyncResult(T result, Throwable cause) {
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
     *                    Json Codec                    *
     * *************************************************/

    public static class Codec extends AbstractJsonCodec<SerializableAsyncResult> {

        private final static String CODEC_ID = "AsyncResult";
        private final static String RESULT_KEY = "result";
        private final static String ERROR_KEY = "error";

        public Codec() {
            super(SerializableAsyncResult.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(SerializableAsyncResult result, WritableJsonObject json) {
            if (result.cause() != null)
                json.set(ERROR_KEY, result.cause().getMessage());
            if (result.result() != null)
                json.set(RESULT_KEY, JsonCodecManager.encodeToJson(result.result()));
        }

        @Override
        public SerializableAsyncResult decodeFromJson(JsonObject json) {
            String errorMessage = json.getString(ERROR_KEY);
            return new SerializableAsyncResult<>(
                    JsonCodecManager.decodeFromJson(json.get(RESULT_KEY)),
                    errorMessage == null ? null : new Exception(errorMessage)
            );
        }
    }
}
