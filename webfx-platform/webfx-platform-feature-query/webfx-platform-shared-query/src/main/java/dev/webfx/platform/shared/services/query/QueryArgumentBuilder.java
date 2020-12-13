package dev.webfx.platform.shared.services.query;

import dev.webfx.platform.shared.datascope.DataScope;

/**
 * @author Bruno Salmon
 */
public final class QueryArgumentBuilder {

    private QueryArgument originalArgument;
    private Object dataSourceId;
    private DataScope dataScope;
    private String language;
    private String statement;
    private Object[] parameters;

    public QueryArgumentBuilder setOriginalArgument(QueryArgument originalArgument) {
        this.originalArgument = originalArgument;
        return this;
    }

    public QueryArgumentBuilder setDataSourceId(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
        return this;
    }

    public QueryArgumentBuilder setDataScope(DataScope dataScope) {
        this.dataScope = dataScope;
        return this;
    }

    public QueryArgumentBuilder addDataScope(DataScope dataScope) {
        return setDataScope(DataScope.concat(this.dataScope, dataScope));
    }

    public QueryArgumentBuilder setLanguage(String language) {
        this.language = language;
        return this;
    }

    public QueryArgumentBuilder setStatement(String statement) {
        this.statement = statement;
        return this;
    }

    public QueryArgumentBuilder setParameters(Object... parameters) {
        this.parameters = parameters;
        return this;
    }

    public QueryArgumentBuilder copy(QueryArgument argument) {
        return setOriginalArgument(argument)
                .setDataSourceId(argument.getDataSourceId())
                .setDataScope(argument.getDataScope())
                .setLanguage(argument.getLanguage())
                .setStatement(argument.getStatement())
                .setParameters(argument.getParameters())
                ;
    }

    public QueryArgument build() {
        return new QueryArgument(originalArgument, dataSourceId, dataScope, language, statement, parameters);
    }
}
