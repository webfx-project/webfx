package webfx.platforms.core.services.json.codec;

import webfx.platforms.core.services.json.JsonArray;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public class BatchJsonCodec extends AbstractJsonCodec<Batch> {

    private final static String BATCH_CODEC_ID = "Batch";
    private final static String BATCH_ARRAY_KEY = "array";

    public BatchJsonCodec() {
        super(Batch.class, BATCH_CODEC_ID);
    }

    @Override
    public void encodeToJson(Batch batch, WritableJsonObject json) {
        json.set(BATCH_ARRAY_KEY, JsonCodecManager.encodeToJsonArray(batch.getArray()));
    }

    @Override
    public Batch decodeFromJson(JsonObject json) {
        JsonArray array = json.getArray(BATCH_ARRAY_KEY);
        Class contentClass = Object.class;
        if (array.size() > 0)
            contentClass = JsonCodecManager.getJavaClass(array.getObject(0).getString(JsonCodecManager.CODEC_ID_KEY));
        return new Batch<>(JsonCodecManager.decodeFromJsonArray(array, contentClass));
    }

}
