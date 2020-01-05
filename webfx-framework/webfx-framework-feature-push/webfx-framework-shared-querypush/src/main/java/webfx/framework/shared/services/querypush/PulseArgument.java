package webfx.framework.shared.services.querypush;


import webfx.platform.shared.datascope.DataScope;

/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final DataScope schemaScope;

    public PulseArgument(Object dataSourceId, DataScope schemaScope) {
        this.dataSourceId = dataSourceId;
        this.schemaScope = schemaScope;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public DataScope getSchemaScope() {
        return schemaScope;
    }

    // Static factory methods

    public static PulseArgument createToRefreshAllQueriesImpactedBySchemaScope(Object dataSourceId, DataScope schemaScope) {
        return new PulseArgument(dataSourceId, schemaScope);
    }
}
