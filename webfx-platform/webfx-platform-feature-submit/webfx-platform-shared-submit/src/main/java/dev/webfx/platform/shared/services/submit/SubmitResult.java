package dev.webfx.platform.shared.services.submit;

import dev.webfx.platform.shared.services.serial.SerialCodecManager;
import dev.webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class SubmitResult {

    private final int rowCount;
    private final Object[] generatedKeys;

    public SubmitResult(int rowCount, Object[] generatedKeys) {
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

    public static final class ProvidedSerialCodec extends SerialCodecBase<SubmitResult> {

        private final static String CODEC_ID = "SubmitRes";
        private final static String ROW_COUNT_KEY = "rows";
        private final static String GENERATED_KEYS_KEY = "genKeys";

        public ProvidedSerialCodec() {
            super(SubmitResult.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(SubmitResult arg, WritableJsonObject json) {
            json.set(ROW_COUNT_KEY, arg.getRowCount());
            if (!Arrays.isEmpty(arg.getGeneratedKeys()))
                json.set(GENERATED_KEYS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getGeneratedKeys()));
        }

        @Override
        public SubmitResult decodeFromJson(JsonObject json) {
            return new SubmitResult(
                    json.getInteger(ROW_COUNT_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(GENERATED_KEYS_KEY))
            );
        }
    }
}
