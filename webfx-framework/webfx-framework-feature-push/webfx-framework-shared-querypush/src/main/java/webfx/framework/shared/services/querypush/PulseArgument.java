package webfx.framework.shared.services.querypush;


import webfx.platform.shared.schemascope.Scope;

/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final Scope schemaScope;

    public PulseArgument(Object dataSourceId, Scope schemaScope) {
        this.dataSourceId = dataSourceId;
        this.schemaScope = schemaScope;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Scope getSchemaScope() {
        return schemaScope;
    }

    // Static factory methods

    public static PulseArgument createToRefreshAllQueriesImpactedBySchemaScope(Object dataSourceId, Scope schemaScope) {
        return new PulseArgument(dataSourceId, schemaScope);
    }
}
