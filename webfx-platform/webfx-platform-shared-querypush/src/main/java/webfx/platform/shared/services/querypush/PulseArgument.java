package webfx.platform.shared.services.querypush;


/**
 * @author Bruno Salmon
 */
public final class PulseArgument {

    private final Object dataSourceId;
    private final Object queryInfo;

    public PulseArgument(Object dataSourceId) {
        this(dataSourceId, null);
    }

    public PulseArgument(Object dataSourceId, Object queryInfo) {
        this.dataSourceId = dataSourceId;
        this.queryInfo = queryInfo;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }

    public Object getQueryInfo() {
        return queryInfo;
    }

    public static PulseArgument createWithQueryInfo(Object queryInfo) {
        return new PulseArgument(null, queryInfo);
    }
}
