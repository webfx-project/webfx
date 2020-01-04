package webfx.framework.shared.services.querypush;


import webfx.platform.shared.schemascope.SchemaScope;

/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final SchemaScope schemaScope;

    public PulseArgument(Object dataSourceId, SchemaScope schemaScope) {
        this.dataSourceId = dataSourceId;
        this.schemaScope = schemaScope;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public SchemaScope getSchemaScope() {
        return schemaScope;
    }

    // Static factory methods

    public static PulseArgument createToRefreshAllQueriesImpactedBySchemaScope(Object dataSourceId, SchemaScope schemaScope) {
        return new PulseArgument(dataSourceId, schemaScope);
    }
}
