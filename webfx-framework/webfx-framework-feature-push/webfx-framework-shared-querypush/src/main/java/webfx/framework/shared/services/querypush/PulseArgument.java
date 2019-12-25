package webfx.framework.shared.services.querypush;


/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final Object updateScope;

    public PulseArgument(Object dataSourceId, Object updateScope) {
        this.dataSourceId = dataSourceId;
        this.updateScope = updateScope;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Object getUpdateScope() {
        return updateScope;
    }

    // Static factory methods

    public static PulseArgument createToRefreshAllQueries(Object dataSourceId) {
        return new PulseArgument(dataSourceId, null);
    }

    public static PulseArgument createToRefreshAllQueriesImpactedByUpdate(Object dataSourceId, Object updateScope) {
        return new PulseArgument(dataSourceId, updateScope);
    }
}
