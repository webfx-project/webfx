package naga.platform.services.update;

import naga.commons.util.Arrays;
import naga.platform.json.codec.AbstractJsonCodec;
import naga.platform.json.codec.JsonCodecManager;
import naga.platform.json.spi.JsonObject;
import naga.platform.json.spi.WritableJsonObject;

/**
 * @author Bruno Salmon
 */
public class UpdateResult {

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
     *                    Json Codec                    *
     * *************************************************/

    private static String CODEC_ID = "UpdateRes";
    private static String ROW_COUNT_KEY = "update";
    private static String GENERATED_KEYS_KEY = "genKeys";

    public static void registerJsonCodec() {
        new AbstractJsonCodec<UpdateResult>(UpdateResult.class, CODEC_ID) {

            @Override
            public void encodeToJson(UpdateResult arg, WritableJsonObject json) {
                json.set(ROW_COUNT_KEY, arg.getRowCount());
                if (!Arrays.isEmpty(arg.getGeneratedKeys()))
                    json.set(GENERATED_KEYS_KEY, JsonCodecManager.encodePrimitiveArrayToJsonArray(arg.getGeneratedKeys()));
            }

            @Override
            public UpdateResult decodeFromJson(JsonObject json) {
                return new UpdateResult(
                        json.getInt(ROW_COUNT_KEY),
                        JsonCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(GENERATED_KEYS_KEY))
                );
            }
        };
    }

}
