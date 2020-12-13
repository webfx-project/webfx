package dev.webfx.platform.shared.services.buscall;

import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import dev.webfx.platform.shared.services.serial.SerialCodecManager;
import dev.webfx.platform.shared.util.async.AsyncResult;

/**
 * @author Bruno Salmon
 */
public final class SerializableAsyncResult<T> implements AsyncResult<T> {

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
     *                   Serial Codec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<SerializableAsyncResult> {

        private final static String CODEC_ID = "AsyncResult";
        private final static String RESULT_KEY = "result";
        private final static String ERROR_KEY = "error";

        public ProvidedSerialCodec() {
            super(SerializableAsyncResult.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(SerializableAsyncResult result, WritableJsonObject json) {
            if (result.cause() != null)
                json.set(ERROR_KEY, result.cause().getMessage());
            if (result.result() != null)
                json.set(RESULT_KEY, SerialCodecManager.encodeToJson(result.result()));
        }

        @Override
        public SerializableAsyncResult decodeFromJson(JsonObject json) {
            String errorMessage = json.getString(ERROR_KEY);
            return new SerializableAsyncResult<>(
                    SerialCodecManager.decodeFromJson(json.get(RESULT_KEY)),
                    errorMessage == null ? null : new Exception(errorMessage)
            );
        }
    }
}
