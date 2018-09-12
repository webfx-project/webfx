package webfx.platforms.core;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import webfx.platforms.core.services.json.JsonArray;
import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.services.json.codec.AbstractJsonCodec;
import webfx.platforms.core.services.json.codec.JsonCodecManager;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.services.query.QueryResult;
import webfx.platforms.core.services.querypush.QueryPushService;
import webfx.platforms.core.services.update.UpdateArgument;
import webfx.platforms.core.services.update.UpdateResult;
import webfx.platforms.core.services.update.UpdateService;
import webfx.platforms.core.util.async.Batch;

import static webfx.platforms.core.services.json.codec.JsonCodecManager.decodeFromJsonArray;
import static webfx.platforms.core.services.json.codec.JsonCodecManager.encodeToJsonArray;

/**
 * @author Bruno Salmon
 */
public class MonolithModule implements ApplicationModule {

    @Override
    public void start() {
        registerJsonCodecs();
    }

    /***********************************************
     *          JsonCodec registration             *
     * *********************************************/


    private void registerJsonCodecs() {
        // Registering all required json codecs (especially for network bus calls)
        QueryPushService.registerJsonCodecsAndBusCalls();
        UpdateService.registerJsonCodecsAndBusCalls();
        registerBatchJsonCodec();
    }

    // Batch json Serialization support

    private static String BATCH_CODEC_ID = "Batch";
    private static String BATCH_ARRAY_KEY = "array";

    static void registerBatchJsonCodec() {
        new AbstractJsonCodec<Batch>(Batch.class, BATCH_CODEC_ID) {

            @Override
            public void encodeToJson(Batch batch, WritableJsonObject json) {
                json.set(BATCH_ARRAY_KEY, encodeToJsonArray(batch.getArray()));
            }

            @Override
            public Batch decodeFromJson(JsonObject json) {
                JsonArray array = json.getArray(BATCH_ARRAY_KEY);
                Class contentClass = Object.class;
                if (array.size() > 0) {
                    switch (array.getObject(0).getString(JsonCodecManager.CODEC_ID_KEY)) {
                        case QueryResult.CODEC_ID:
                            contentClass = QueryResult.class;
                            break;
                        case QueryArgument.CODEC_ID:
                            contentClass = QueryArgument.class;
                            break;
                        case UpdateArgument.CODEC_ID:
                            contentClass = UpdateArgument.class;
                            break;
                        case UpdateResult.CODEC_ID:
                            contentClass = UpdateResult.class;
                            break;
                    }
                }
                return new Batch<>(decodeFromJsonArray(array, contentClass));
            }
        };
    }


}
