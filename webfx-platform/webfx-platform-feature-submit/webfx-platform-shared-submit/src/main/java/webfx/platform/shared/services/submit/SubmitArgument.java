package webfx.platform.shared.services.submit;

import webfx.platform.shared.services.serial.SerialCodecManager;
import webfx.platform.shared.services.serial.spi.impl.SerialCodecBase;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class SubmitArgument {

    private final transient SubmitArgument originalArgument;

    private final Object dataSourceId;
    private Object submitScope;
    private final boolean returnGeneratedKeys;
    private final String submitLang;
    private final String submitString;
    private final Object[] parameters;

    public SubmitArgument(Object dataSourceId, String submitString, Object... parameters) {
        this(dataSourceId, false, submitString, parameters);
    }

    public SubmitArgument(Object dataSourceId, boolean returnGeneratedKeys, String submitString, Object... parameters) {
        this(null, dataSourceId, returnGeneratedKeys, null, submitString, parameters);
    }

    public SubmitArgument(Object dataSourceId, String submitLang, String submitString, Object... parameters) {
        this(null, dataSourceId, false, submitLang, submitString, parameters);
    }

    public SubmitArgument(SubmitArgument originalArgument, String submitString) {
        this(originalArgument, originalArgument.getDataSourceId(), originalArgument.returnGeneratedKeys(), null, submitString, originalArgument.getParameters());
    }

    public SubmitArgument(SubmitArgument originalArgument, Object dataSourceId, boolean returnGeneratedKeys, String submitLang, String submitString, Object... parameters) {
        this.originalArgument = originalArgument;
        this.dataSourceId = dataSourceId;
        this.returnGeneratedKeys = returnGeneratedKeys;
        this.submitLang = submitLang;
        this.submitString = submitString;
        this.parameters = parameters;
    }

    public SubmitArgument getOriginalArgument() {
        return originalArgument;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Object getSubmitScope() {
        return submitScope;
    }

    public boolean returnGeneratedKeys() {
        return returnGeneratedKeys;
    }

    public String getSubmitLang() {
        return submitLang;
    }

    public String getSubmitString() {
        return submitString;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "SubmitArgument('" + submitString + (parameters == null ? "'" : "', " + Arrays.toString(parameters)) + ')';
    }

    /****************************************************
     *                   Serial Codec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<SubmitArgument> {

        private static final String CODEC_ID = "SubmitArg";
        private static final String UPDATE_LANG_KEY = "submitLang";
        private static final String UPDATE_STRING_KEY = "submitString";
        private static final String PARAMETERS_KEY = "params";
        private static final String RETURN_GENERATED_KEYS_KEY = "genKeys";
        private static final String DATA_SOURCE_ID_KEY = "dsId";

        public ProvidedSerialCodec() {
            super(SubmitArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(SubmitArgument arg, WritableJsonObject json) {
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            json.set(RETURN_GENERATED_KEYS_KEY, arg.returnGeneratedKeys());
            if (arg.getSubmitLang() != null)
                json.set(UPDATE_LANG_KEY, arg.getSubmitLang());
            json.set(UPDATE_STRING_KEY, arg.getSubmitString());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
        }

        @Override
        public SubmitArgument decodeFromJson(JsonObject json) {
            return new SubmitArgument(null,
                    json.get(DATA_SOURCE_ID_KEY),
                    json.getBoolean(RETURN_GENERATED_KEYS_KEY),
                    json.getString(UPDATE_LANG_KEY),
                    json.getString(UPDATE_STRING_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY))
            );
        }
    }
}
