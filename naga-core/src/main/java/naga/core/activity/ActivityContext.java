package naga.core.activity;

import javafx.beans.property.Property;
import naga.core.json.JsonObject;
import naga.core.orm.domainmodel.DataSourceModel;
import naga.core.routing.history.History;
import naga.core.spi.toolkit.GuiNode;
import naga.core.spi.toolkit.hasproperties.HasNodeProperty;

/**
 * @author Bruno Salmon
 */
public interface ActivityContext extends HasNodeProperty {

    static ActivityContext create(ActivityContext parentContext) {
        return new ActivityContextImpl(parentContext);
    }

    ActivityContext getParentContext();

    ActivityManager getActivityManager();

    DataSourceModel getDataSourceModel();

    History getHistory();

    JsonObject getParams();

    Property<GuiNode> nodeProperty();

    Property<GuiNode> mountNodeProperty();
}
