package webfx.platforms.core.services.update;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.json.codec.AbstractJsonCodec;

/**
 * @author Bruno Salmon
 */
public class GeneratedKeyBatchIndex {

    private final int batchIndex;

    public GeneratedKeyBatchIndex(int batchIndex) {
        this.batchIndex = batchIndex;
    }

    public int getBatchIndex() {
        return batchIndex;
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static class Codec extends AbstractJsonCodec<GeneratedKeyBatchIndex> {

        private static String CODEC_ID = "GenKeyBatchIndex";
        private static String BATCH_INDEX_KEY = "index";

        public Codec() {
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
