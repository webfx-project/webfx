package naga.platform.activity;

import javafx.beans.property.Property;
import naga.platform.json.spi.JsonObject;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.platform.client.url.history.History;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.properties.markers.HasNodeProperty;

/**
 * @author Bruno Salmon
 */
public interface ActivityContext extends HasNodeProperty {

    static ActivityContext create(ActivityContext parentContext) {
        return new ActivityContextImpl(parentContext);
    }

    ActivityContext getParentContext();

    ActivityManager getActivityManager();

    void setDataSourceModel(DataSourceModel dataSourceModel);

    DataSourceModel getDataSourceModel();

    History getHistory();

    JsonObject getParams();

    Property<GuiNode> nodeProperty();

    Property<GuiNode> mountNodeProperty();
}
