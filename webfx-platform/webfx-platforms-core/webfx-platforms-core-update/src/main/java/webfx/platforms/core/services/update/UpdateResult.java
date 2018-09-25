package webfx.platforms.core.services.update;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.services.serial.SerialCodecBase;
import webfx.platforms.core.services.serial.SerialCodecManager;

/**
 * @author Bruno Salmon
 */
public final class UpdateResult {

    private final int rowCount;
    private final Object[] generatedKeys;

    public UpdateResult(int rowCount, Object[] generatedKeys) {
        this.rowCount = rowCount;
        this.generatedKeys = generatedKeys;
    }

    public int getRowCount() {
        return rowCount;
    }

    public Object[] getGeneratedKeys() {
        return generatedKeys;
    }

    /****************************************************
     *                   Serial Codec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<UpdateResult> {

        private final static String CODEC_ID = "UpdateRes";
        private final static String ROW_COUNT_KEY = "update";
        private final static String GENERATED_KEYS_KEY = "genKeys";

        public ProvidedSerialCodec() {
            super(UpdateResult.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(UpdateResult arg, WritableJsonObject json) {
            json.set(ROW_COUNT_KEY, arg.getRowCount());
            if (!Arrays.isEmpty(arg.getGeneratedKeys()))
                json.set(GENERATED_KEYS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getGeneratedKeys()));
        }

        @Override
        public UpdateResult decodeFromJson(JsonObject json) {
            return new UpdateResult(
                    json.getInteger(ROW_COUNT_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(GENERATED_KEYS_KEY))
            );
        }
    }
}
