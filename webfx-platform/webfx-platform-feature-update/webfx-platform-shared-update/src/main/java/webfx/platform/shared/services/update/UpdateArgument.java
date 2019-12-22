package webfx.platform.shared.services.update;

import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class UpdateArgument {

    private final Object dataSourceId;
    private final boolean returnGeneratedKeys;
    private final String updateLang;
    private final String updateString;
    private final Object[] parameters;

    public UpdateArgument(Object dataSourceId, String updateString, Object... parameters) {
        this(dataSourceId, false, updateString, parameters);
    }

    public UpdateArgument(Object dataSourceId, boolean returnGeneratedKeys, String updateString, Object... parameters) {
        this(dataSourceId, returnGeneratedKeys, null, updateString, parameters);
    }

    public UpdateArgument(Object dataSourceId, String updateLang, String updateString, Object... parameters) {
        this(dataSourceId, false, updateLang, updateString, parameters);
    }

    public UpdateArgument(Object dataSourceId, boolean returnGeneratedKeys, String updateLang, String updateString, Object... parameters) {
        this.dataSourceId = dataSourceId;
        this.returnGeneratedKeys = returnGeneratedKeys;
        this.updateLang = updateLang;
        this.updateString = updateString;
        this.parameters = parameters;
    }

    public String getUpdateLang() {
        return updateLang;
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
     *                   Serial ProvidedSerialCodec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<UpdateArgument> {

        private static final String CODEC_ID = "UpdateArg";
        private static final String UPDATE_LANG_KEY = "updateLang";
        private static final String UPDATE_STRING_KEY = "updateString";
        private static final String PARAMETERS_KEY = "params";
        private static final String RETURN_GENERATED_KEYS_KEY = "genKeys";
        private static final String DATA_SOURCE_ID_KEY = "dsId";

        public ProvidedSerialCodec() {
            super(UpdateArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(UpdateArgument arg, WritableJsonObject json) {
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            json.set(RETURN_GENERATED_KEYS_KEY, arg.returnGeneratedKeys());
            if (arg.getUpdateLang() != null)
                json.set(UPDATE_LANG_KEY, arg.getUpdateLang());
            json.set(UPDATE_STRING_KEY, arg.getUpdateString());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
        }

        @Override
        public UpdateArgument decodeFromJson(JsonObject json) {
            return new UpdateArgument(
                    json.get(DATA_SOURCE_ID_KEY),
                    json.getBoolean(RETURN_GENERATED_KEYS_KEY),
                    json.getString(UPDATE_LANG_KEY),
                    json.getString(UPDATE_STRING_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY))
            );
        }
    }
}
