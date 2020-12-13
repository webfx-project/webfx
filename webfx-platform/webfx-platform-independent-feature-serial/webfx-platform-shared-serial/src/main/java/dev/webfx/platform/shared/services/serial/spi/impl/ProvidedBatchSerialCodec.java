package dev.webfx.platform.shared.services.serial.spi.impl;

import dev.webfx.platform.shared.services.json.JsonArray;
import dev.webfx.platform.shared.services.json.JsonObject;
import dev.webfx.platform.shared.services.json.WritableJsonObject;
import dev.webfx.platform.shared.services.serial.SerialCodecManager;
import dev.webfx.platform.shared.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public final class ProvidedBatchSerialCodec extends SerialCodecBase<Batch> {

    private final static String BATCH_CODEC_ID = "Batch";
    private final static String BATCH_ARRAY_KEY = "array";

    public ProvidedBatchSerialCodec() {
        super(Batch.class, BATCH_CODEC_ID);
    }

    @Override
    public void encodeToJson(Batch batch, WritableJsonObject json) {
        json.set(BATCH_ARRAY_KEY, SerialCodecManager.encodeToJsonArray(batch.getArray()));
    }

    @Override
    public Batch decodeFromJson(JsonObject json) {
        JsonArray array = json.getArray(BATCH_ARRAY_KEY);
        Class contentClass = Object.class;
        if (array.size() > 0)
            contentClass = SerialCodecManager.getJavaClass(array.getObject(0).getString(SerialCodecManager.CODEC_ID_KEY));
        return new Batch<>(SerialCodecManager.decodeFromJsonArray(array, contentClass));
    }
}
