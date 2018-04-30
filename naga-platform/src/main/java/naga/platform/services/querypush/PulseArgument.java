package naga.platform.services.querypush;

/**
 * @author Bruno Salmon
 */
public class PulseArgument {

    private final Object dataSourceId;

    public PulseArgument(Object dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }
}
