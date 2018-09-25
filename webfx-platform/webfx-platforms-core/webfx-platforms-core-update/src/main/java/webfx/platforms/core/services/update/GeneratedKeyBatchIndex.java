package webfx.platforms.core.services.update;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.serial.SerialCodecBase;

/**
 * @author Bruno Salmon
 */
public final class GeneratedKeyBatchIndex {

    private final int batchIndex;

    public GeneratedKeyBatchIndex(int batchIndex) {
        this.batchIndex = batchIndex;
    }

    public int getBatchIndex() {
        return batchIndex;
    }

    /****************************************************
     *                   Serial ProvidedSerialCodec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<GeneratedKeyBatchIndex> {

        private static final String CODEC_ID = "GenKeyBatchIndex";
        private static final String BATCH_INDEX_KEY = "index";

        public ProvidedSerialCodec() {
            super(GeneratedKeyBatchIndex.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(GeneratedKeyBatchIndex arg, WritableJsonObject json) {
            json.set(BATCH_INDEX_KEY, arg.getBatchIndex());
        }

        @Override
        public GeneratedKeyBatchIndex decodeFromJson(JsonObject json) {
            return new GeneratedKeyBatchIndex(
                    json.getInteger(BATCH_INDEX_KEY)
            );
        }
    }
}
