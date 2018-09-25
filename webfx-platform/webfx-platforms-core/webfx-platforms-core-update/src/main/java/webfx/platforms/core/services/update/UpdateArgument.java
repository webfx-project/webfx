package webfx.platforms.core.services.update;

import webfx.platforms.core.services.json.JsonObject;
import webfx.platforms.core.services.json.WritableJsonObject;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.services.json.codec.AbstractJsonCodec;
import webfx.platforms.core.services.json.codec.JsonCodecManager;

/**
 * @author Bruno Salmon
 */
public final class UpdateArgument {

    private final String updateString;
    private final Object[] parameters;
    private final boolean returnGeneratedKeys;
    private final Object dataSourceId;

    public UpdateArgument(String updateString, Object dataSourceId) {
        this(updateString, null, dataSourceId);
    }

    public UpdateArgument(String updateString, Object[] parameters, Object dataSourceId) {
        this(updateString, parameters, false, dataSourceId);
    }

    public UpdateArgument(String updateString, Object[] parameters, boolean returnGeneratedKeys, Object dataSourceId) {
        this.updateString = updateString;
        this.parameters = parameters;
        this.returnGeneratedKeys = returnGeneratedKeys;
        this.dataSourceId = dataSourceId;
    }

    public String getUpdateString() {
        return updateString;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public boolean returnGeneratedKeys() {
        return returnGeneratedKeys;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    @Override
    public String toString() {
        return "UpdateArgument('" + updateString + (parameters == null ? "'" : "', " + Arrays.toString(parameters)) + ')';
    }

    /****************************************************
     *                    Json Codec                    *
     * *************************************************/

    public static final class Codec extends AbstractJsonCodec<UpdateArgument> {

        private static final String CODEC_ID = "UpdateArg";
        private static final String UPDATE_KEY = "update";
        private static final String PARAMETERS_KEY = "params";
        private static final String RETURN_GENERATED_KEYS_KEY = "genKeys";
        private static final String DATA_SOURCE_ID_KEY = "dsId";

        public Codec() {
            super(UpdateArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(UpdateArgument arg, WritableJsonObject json) {
            json.set(UPDATE_KEY, arg.getUpdateString());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, JsonCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
            json.set(RETURN_GENERATED_KEYS_KEY, arg.returnGeneratedKeys());
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
        }

        @Override
        public UpdateArgument decodeFromJson(JsonObject json) {
            return new UpdateArgument(
                    json.getString(UPDATE_KEY),
                    JsonCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY)),
                    json.getBoolean(RETURN_GENERATED_KEYS_KEY),
                    json.get(DATA_SOURCE_ID_KEY)
            );
        }
    }
}
