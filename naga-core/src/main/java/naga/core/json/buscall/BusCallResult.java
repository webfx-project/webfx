package naga.core.json.buscall;

import naga.core.json.JsonObject;
import naga.core.json.codec.AbstractJsonCodec;
import naga.core.json.codec.JsonCodecManager;
import naga.core.json.WritableJsonObject;

/*
 * @author Bruno Salmon
 */
class BusCallResult<T> {

    private final int callNumber;
    private T targetResult;

    private Object serializedTargetResult; // JsonObject or scalar

    /**
     * Standard constructor accepting a java representation (T) of the target result.
    public BusCallResult(int callNumber, T targetResult) {
        this.callNumber = callNumber;
        this.targetResult = targetResult;
    }
     */

    /**
     * Alternative constructor accepting a json representation (JsonObject) of the target result.
     * This is particularly useful when forwarding a result over the json bus. In this case the result has indeed already
     * been serialized into json format (because coming from the json bus) so it wouldn't be optimized to deserialize it
     * when the only usage of it will finally be to serialize it again (sending it again on the json bus). Using this
     * constructor will avoid this useless serialization/deserialization and offer an optimized performance.
    public BusCallResult(int callNumber, JsonObject serializedTargetResult) {
        this.callNumber = callNumber;
        this.serializedTargetResult = serializedTargetResult;
    }
     */

    /**
     * Convenient alternative constructor accepting both representations (AsyncResult or JsonObject) of the target result.
     */
    public BusCallResult(int callNumber, Object object) {
        this.callNumber = callNumber;
        if (object instanceof JsonObject)
            this.serializedTargetResult = object;
        else
            this.targetResult = (T) object;
    }


    public int getCallNumber() {
        return callNumber;
    }

    public T getTargetResult() {
        if (targetResult == null && serializedTargetResult != null)
            targetResult = JsonCodecManager.decodeFromJson(serializedTargetResult);
        return targetResult;
    }

    Object getSerializedTargetResult() {
        if (serializedTargetResult == null && targetResult != null) {
            // Transforming the target result into a serializable object before serializing it
            //SerializableAsyncResult serializableTargetResult = SerializableAsyncResult.getSerializableAsyncResult(targetResult);
            // Now that we are sure it can be serialized, we do it
            serializedTargetResult = JsonCodecManager.encodeToJson(targetResult);
        }
        return serializedTargetResult;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    private static String CODEC_ID = "callRes";
    private static String CALL_NUMBER_KEY = "seq";
    private static String TARGET_RESULT_KEY = "res";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<BusCallResult>(BusCallResult.class, CODEC_ID) {

            @Override
            public void encodeToJson(BusCallResult result, WritableJsonObject json) {
                json.set(CALL_NUMBER_KEY, result.getCallNumber())
                    .set(TARGET_RESULT_KEY, result.getSerializedTargetResult());
            }

            @Override
            public BusCallResult decodeFromJson(JsonObject json) {
                return new BusCallResult(
                        json.getInt(CALL_NUMBER_KEY),
                        json.get(TARGET_RESULT_KEY)
                );
            }
        };
    }
}
