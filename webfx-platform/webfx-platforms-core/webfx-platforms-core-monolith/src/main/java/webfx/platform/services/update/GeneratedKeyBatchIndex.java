package webfx.platform.services.update;

import webfx.platform.services.json.codec.AbstractJsonCodec;
import webfx.platform.services.json.JsonObject;
import webfx.platform.services.json.WritableJsonObject;

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

    private static String CODEC_ID = "GenKeyBatchIndex";
    private static String BATCH_INDEX_KEY = "index";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<GeneratedKeyBatchIndex>(GeneratedKeyBatchIndex.class, CODEC_ID) {

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
        };
    }

}
