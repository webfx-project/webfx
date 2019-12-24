package webfx.framework.shared.services.querypush;


/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final Object updateScope;
    private final Object queryInfo;

    public PulseArgument(Object dataSourceId, Object updateScope, Object queryInfo) {
        this.dataSourceId = dataSourceId;
        this.updateScope = updateScope;
        this.queryInfo = queryInfo;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Object getUpdateScope() {
        return updateScope;
    }

    public Object getQueryInfo() {
        return queryInfo;
    }

    // Static factory methods

    public static PulseArgument create(Object dataSourceId) {
        return new PulseArgument(dataSourceId, null, null);
    }

    public static PulseArgument createWithQueryInfo(Object queryInfo) {
        return new PulseArgument(null, null, queryInfo);
    }

    public static PulseArgument createWithUpdateScope(Object dataSourceId, Object updateScope) {
        return new PulseArgument(dataSourceId, updateScope, null);
    }
}
