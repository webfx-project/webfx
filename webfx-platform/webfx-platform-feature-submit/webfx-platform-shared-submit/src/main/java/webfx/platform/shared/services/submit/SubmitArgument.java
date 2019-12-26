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
    private final Object schemaScope;
    private final boolean returnGeneratedKeys;
    private final String language;
    private final String statement;
    private final Object[] parameters;

    public SubmitArgument(SubmitArgument originalArgument, Object dataSourceId, Object schemaScope, boolean returnGeneratedKeys, String language, String statement, Object[] parameters) {
        this.originalArgument = originalArgument;
        this.dataSourceId = dataSourceId;
        this.schemaScope = schemaScope;
        this.returnGeneratedKeys = returnGeneratedKeys;
        this.language = language;
        this.statement = statement;
        this.parameters = parameters;
    }

    public SubmitArgument getOriginalArgument() {
        return originalArgument;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Object getSchemaScope() {
        return schemaScope;
    }

    public boolean returnGeneratedKeys() {
        return returnGeneratedKeys;
    }

    public String getLanguage() {
        return language;
    }

    public String getStatement() {
        return statement;
    }

    public Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "SubmitArgument('" + statement + (parameters == null ? "'" : "', " + Arrays.toString(parameters)) + ')';
    }

    public static SubmitArgumentBuilder builder() {
        return new SubmitArgumentBuilder();
    }

    /****************************************************
     *                   Serial Codec                   *
     * *************************************************/

    public static final class ProvidedSerialCodec extends SerialCodecBase<SubmitArgument> {

        private static final String CODEC_ID = "SubmitArgument";
        private static final String DATA_SOURCE_ID_KEY = "dataSourceId";
        private static final String RETURN_GENERATED_KEYS_KEY = "genKeys";
        private static final String LANGUAGE_KEY = "lang";
        private static final String STATEMENT_KEY = "statement";
        private static final String PARAMETERS_KEY = "parameters";

        public ProvidedSerialCodec() {
            super(SubmitArgument.class, CODEC_ID);
        }

        @Override
        public void encodeToJson(SubmitArgument arg, WritableJsonObject json) {
            json.set(DATA_SOURCE_ID_KEY, arg.getDataSourceId());
            json.set(RETURN_GENERATED_KEYS_KEY, arg.returnGeneratedKeys());
            if (arg.getLanguage() != null)
                json.set(LANGUAGE_KEY, arg.getLanguage());
            json.set(STATEMENT_KEY, arg.getStatement());
            if (!Arrays.isEmpty(arg.getParameters()))
                json.set(PARAMETERS_KEY, SerialCodecManager.encodePrimitiveArrayToJsonArray(arg.getParameters()));
        }

        @Override
        public SubmitArgument decodeFromJson(JsonObject json) {
            return new SubmitArgument(null,
                    json.get(DATA_SOURCE_ID_KEY),
                    null,
                    json.getBoolean(RETURN_GENERATED_KEYS_KEY),
                    json.getString(LANGUAGE_KEY),
                    json.getString(STATEMENT_KEY),
                    SerialCodecManager.decodePrimitiveArrayFromJsonArray(json.getArray(PARAMETERS_KEY))
            );
        }
    }
}
