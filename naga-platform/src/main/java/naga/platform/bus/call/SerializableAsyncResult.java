package naga.platform.bus.call;

import naga.platform.services.json.JsonObject;
import naga.platform.services.json.WritableJsonObject;
import naga.platform.services.json.codec.AbstractJsonCodec;
import naga.platform.services.json.codec.JsonCodecManager;
import naga.util.async.AsyncResult;

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
     *                    Json Codec                    *
     * *************************************************/

    private final static String CODEC_ID = "AsyncResult";
    private final static String RESULT_KEY = "result";
    private final static String ERROR_KEY = "error";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<SerializableAsyncResult>(SerializableAsyncResult.class, CODEC_ID) {

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
        };
    }
}
