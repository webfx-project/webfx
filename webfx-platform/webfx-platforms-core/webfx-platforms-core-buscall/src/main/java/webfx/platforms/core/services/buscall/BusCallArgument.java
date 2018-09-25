package webfx.platforms.core.services.buscall;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.json.codec.AbstractJsonCodec;
import webfx.platforms.core.services.json.codec.JsonCodecManager;


/*
 * @author Bruno Salmon
 */
public final class BusCallArgument {

    private static int SEQ = 0;

    private final String targetAddress;
    private final Object targetArgument;
    private final int callNumber;

    private Object jsonEncodedTargetArgument; // can be a JsonObject or simply a scalar

    BusCallArgument(String targetAddress, Object targetArgument) {
        this(targetAddress, targetArgument, ++SEQ);
    }

    private BusCallArgument(String targetAddress, Object targetArgument, int callNumber) {
        this.targetAddress = targetAddress;
        this.targetArgument = targetArgument;
        this.callNumber = callNumber;
    }

    String getTargetAddress() {
        return targetAddress;
    }

    Object getTargetArgument() {
        return targetArgument;
    }

    int getCallNumber() {
        return callNumber;
    }

    Object getJsonEncodedTargetArgument() {
        if (jsonEncodedTargetArgument == null && targetArgument != null)
            jsonEncodedTargetArgument = JsonCodecManager.encodeToJson(targetArgument);
        return jsonEncodedTargetArgument;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final class Codec extends AbstractJsonCodec<BusCallArgument> {

        private static final String CODEC_ID = "call";
        private static final String CALL_NUMBER_KEY = "seq";
        private static final String TARGET_ADDRESS_KEY = "addr";
        private static final String TARGET_ARGUMENT_KEY = "arg";

        public Codec() {
            super(BusCallArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(BusCallArgument call, WritableJsonObject json) {
            json.set(CALL_NUMBER_KEY, call.callNumber)
                    .set(TARGET_ADDRESS_KEY, call.getTargetAddress())
                    .set(TARGET_ARGUMENT_KEY, call.getJsonEncodedTargetArgument());
        }

        @Override
        public BusCallArgument decodeFromJson(JsonObject json) {
            return new BusCallArgument(
                    json.getString(TARGET_ADDRESS_KEY),
                    JsonCodecManager.decodeFromJson(json.get(TARGET_ARGUMENT_KEY)),
                    json.getInteger(CALL_NUMBER_KEY)
            );
        }
    }
}
