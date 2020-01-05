package webfx.platform.shared.services.submit;

import webfx.platform.shared.datascope.DataScope;

/**
 * @author Bruno Salmon
 */
public final class SubmitArgumentBuilder {

    private SubmitArgument originalArgument;
    private Object dataSourceId;
    private DataScope schemaScope;
    private boolean returnGeneratedKeys;
    private String language;
    private String statement;
    private Object[] parameters;

    public SubmitArgumentBuilder setOriginalArgument(SubmitArgument originalArgument) {
        this.originalArgument = originalArgument;
        return this;
    }

    public SubmitArgumentBuilder setDataSourceId(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    public SubmitArgumentBuilder setSchemaScope(DataScope schemaScope) {
        this.schemaScope = schemaScope;
        return this;
    }

    public SubmitArgumentBuilder setReturnGeneratedKeys(boolean returnGeneratedKeys) {
        this.returnGeneratedKeys = returnGeneratedKeys;
        return this;
    }

    public SubmitArgumentBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public SubmitArgumentBuilder setStatement(String statement) {
        this.statement = statement;
        return this;
    }

    public SubmitArgumentBuilder setParameters(Object... parameters) {
        this.parameters = parameters;
        return this;
    }

    public SubmitArgumentBuilder copy(SubmitArgument argument) {
        return setOriginalArgument(argument)
                .setDataSourceId(argument.getDataSourceId())
                .setSchemaScope(argument.getSchemaScope())
                .setReturnGeneratedKeys(argument.returnGeneratedKeys())
                .setLanguage(argument.getLanguage())
                .setStatement(argument.getStatement())
                .setParameters(argument.getParameters());
    }

    public SubmitArgument build() {
        return new SubmitArgument(originalArgument, dataSourceId, schemaScope, returnGeneratedKeys, language, statement, parameters);
    }
}
