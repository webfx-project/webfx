package naga.platform.services.update;

/**
 * @author Bruno Salmon
 */
public class UpdateArgument {

    private final String updateString;
    private final Object[] parameters;
    private final boolean returnGeneratedKeys;
    private final Object dataSourceId;

    public UpdateArgument(String updateString, Object[] parameters, boolean returnGeneratedKeys, Object dataSourceId) {
        this.updateString = updateString;
        this.parameters = parameters;
        this.returnGeneratedKeys = returnGeneratedKeys;
        this.dataSourceId = dataSourceId;
    }

    public String getUpdateString() {
        return updateString;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public boolean returnGeneratedKeys() {
        return returnGeneratedKeys;
    }

    public Object getDataSourceId() {
        return dataSourceId;
    }
}
