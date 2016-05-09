package naga.core.buscall;

import naga.core.codec.AbstractCompositeCodec;
import naga.core.codec.CompositeCodecManager;
import naga.core.composite.CompositeObject;
import naga.core.composite.WritableCompositeObject;

/*
 * @author Bruno Salmon
 */
class BusCallResult<T> {

    private final int callNumber;
    private T targetResult;

    private Object serializedTargetResult; // CompositeObject or scalar

    /**
     * Standard constructor accepting a java representation (T) of the target result.
    public BusCallResult(int callNumber, T targetResult) {
        this.callNumber = callNumber;
        this.targetResult = targetResult;
    }
     */

    /**
     * Alternative constructor accepting a composite representation (CompositeObject) of the target result.
     * This is particularly useful when forwarding a result over the json bus. In this case the result has indeed already
     * been serialized into json format (because coming from the json bus) so it wouldn't be optimized to deserialize it
     * when the only usage of it will finally be to serialize it again (sending it again on the json bus). Using this
     * constructor will avoid this useless serialization/deserialization and offer an optimized performance.
    public BusCallResult(int callNumber, CompositeObject serializedTargetResult) {
        this.callNumber = callNumber;
        this.serializedTargetResult = serializedTargetResult;
    }
     */

    /**
     * Convenient alternative constructor accepting both representations (AsyncResult or CompositeObject) of the target result.
     */
    public BusCallResult(int callNumber, Object object) {
        this.callNumber = callNumber;
        if (object instanceof CompositeObject)
            this.serializedTargetResult = object;
        else
            this.targetResult = (T) object;
    }


    public int getCallNumber() {
        return callNumber;
    }

    public T getTargetResult() {
        if (targetResult == null && serializedTargetResult != null)
            targetResult = CompositeCodecManager.decodeFromComposite(serializedTargetResult);
        return targetResult;
    }

    Object getSerializedTargetResult() {
        if (serializedTargetResult == null && targetResult != null) {
            // Transforming the target result into a serializable object before serializing it
            //SerializableAsyncResult serializableTargetResult = SerializableAsyncResult.getSerializableAsyncResult(targetResult);
            // Now that we are sure it can be serialized, we do it
            serializedTargetResult = CompositeCodecManager.encodeToComposite(targetResult);
        }
        return serializedTargetResult;
    }

    /****************************************************
     *                 Composite Codec                  *
     * *************************************************/

    private static String CODEC_ID = "callRes";
    private static String CALL_NUMBER_KEY = "seq";
    private static String TARGET_RESULT_KEY = "res";

    public static void registerCompositeCodec() {
        new AbstractCompositeCodec<BusCallResult>(BusCallResult.class, CODEC_ID) {

            @Override
            public void encodeToComposite(BusCallResult result, WritableCompositeObject co) {
                co.set(CALL_NUMBER_KEY, result.getCallNumber());
                co.set(TARGET_RESULT_KEY, result.getSerializedTargetResult());
            }

            @Override
            public BusCallResult decodeFromComposite(CompositeObject co) {
                return new BusCallResult(
                        (int) co.getDouble(CALL_NUMBER_KEY),
                        co.get(TARGET_RESULT_KEY)
                );
            }
        };
    }
}
