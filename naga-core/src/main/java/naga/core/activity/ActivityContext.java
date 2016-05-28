package naga.core.activity;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.core.orm.domainmodel.DataSourceModel;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.hasproperties.HasNodeProperty;

import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class ActivityContext implements HasNodeProperty {

    private final ActivityContext parentContext;
    private Map<String, String> params;
    private ActivityManager activityManager;
    private DataSourceModel dataSourceModel;
    private ActivityRouter activityRouter;

    public ActivityContext(ActivityContext parentContext) {
        this.parentContext = parentContext;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public ActivityManager getActivityManager() {
        return activityManager;
    }

    void setActivityManager(ActivityManager activityManager) {
        this.activityManager = activityManager;
    }

    public DataSourceModel getDataSourceModel() {
        return dataSourceModel != null || parentContext == null ? dataSourceModel : parentContext.getDataSourceModel();
    }

    void setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
    }

    public void setActivityRouter(ActivityRouter activityRouter) {
        this.activityRouter = activityRouter;
    }

    public ActivityRouter getActivityRouter() {
        return activityRouter != null || parentContext == null ? activityRouter : parentContext.getActivityRouter();
    }

    private Property<GuiNode> nodeProperty;
    public Property<GuiNode> nodeProperty() {
        if (nodeProperty == null)
            nodeProperty = new SimpleObjectProperty<>();
        return nodeProperty;
    }
}
