package webfx.platform.shared.services.query;

/**
 * @author Bruno Salmon
 */
public final class QueryArgumentBuilder {

    private QueryArgument originalArgument;
    private Object dataSourceId;
    private Object schemaScope;
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

    public QueryArgumentBuilder setSchemaScope(Object schemaScope) {
        this.schemaScope = schemaScope;
        return this;
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
                .setSchemaScope(argument.getSchemaScope())
                .setLanguage(argument.getLanguage())
                .setStatement(argument.getStatement())
                .setParameters(argument.getParameters())
                ;
    }

    public QueryArgument build() {
        return new QueryArgument(originalArgument, dataSourceId, schemaScope, language, statement, parameters);
    }
}
