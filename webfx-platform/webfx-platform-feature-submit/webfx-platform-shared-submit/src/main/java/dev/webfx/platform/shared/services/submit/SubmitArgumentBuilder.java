package dev.webfx.platform.shared.services.submit;

import dev.webfx.platform.shared.datascope.DataScope;

/**
 * @author Bruno Salmon
 */
public final class SubmitArgumentBuilder {

    private SubmitArgument originalArgument;
    private Object dataSourceId;
    private DataScope dataScope;
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

    public SubmitArgumentBuilder setDataScope(DataScope dataScope) {
        this.dataScope = dataScope;
        return this;
    }

    public SubmitArgumentBuilder addDataScope(DataScope dataScope) {
        return setDataScope(DataScope.concat(this.dataScope, dataScope));
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
                .setDataScope(argument.getDataScope())
                .setReturnGeneratedKeys(argument.returnGeneratedKeys())
                .setLanguage(argument.getLanguage())
                .setStatement(argument.getStatement())
                .setParameters(argument.getParameters());
    }

    public SubmitArgument build() {
        return new SubmitArgument(originalArgument, dataSourceId, dataScope, returnGeneratedKeys, language, statement, parameters);
    }
}
