package webfx.framework.shared.services.querypush;


/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final Object schemaScope;

    public PulseArgument(Object dataSourceId, Object schemaScope) {
        this.dataSourceId = dataSourceId;
        this.schemaScope = schemaScope;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Object getSchemaScope() {
        return schemaScope;
    }

    // Static factory methods

    public static PulseArgument createToRefreshAllQueriesImpactedBySchemaScope(Object dataSourceId, Object schemaScope) {
        return new PulseArgument(dataSourceId, schemaScope);
    }
}
