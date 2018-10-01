package webfx.platform.shared.services.update;

import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.util.Arrays;

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
