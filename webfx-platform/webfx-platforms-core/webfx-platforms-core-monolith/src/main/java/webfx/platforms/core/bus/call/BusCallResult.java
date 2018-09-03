package webfx.platforms.core.bus.call;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.json.codec.AbstractJsonCodec;
import webfx.platforms.core.services.json.codec.JsonCodecManager;

/**
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
        //Platform.log("BusCallResult constructor, class of object = " + object.getClass());
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
        //Platform.log("BusCallResult getTargetResult(), class of targetResult = " + targetResult.getClass());
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
                //Platform.log("Decoding " + json.toJsonString());
                return new BusCallResult(
                        json.getInteger(CALL_NUMBER_KEY),
                        json.get(TARGET_RESULT_KEY)
                );
            }
        };
    }
}
