package naga.core.activity;

import javafx.beans.property.Property;
import naga.core.json.JsonObject;
import naga.core.orm.domainmodel.DataSourceModel;
import naga.core.routing.history.History;
import naga.core.spi.toolkit.GuiNode;

/**
 * Mixin interface that can be used to have a direct access to the ActivityContext methods on any class implementing
 * HasActivityContext. When there are many calls to the ActivityContext, this can be convenient to just write
 * getHistory().push(...) instead of getActivityContext().getHistory().push(...) - for example.
 *
 * @author Bruno Salmon
 */
public interface ActivityContextDirectAccess extends HasActivityContext, ActivityContext {

    default ActivityManager getActivityManager() { return getActivityContext().getActivityManager(); }

    default DataSourceModel getDataSourceModel() { return getActivityContext().getDataSourceModel(); }

    default History getHistory() { return getActivityContext().getHistory(); }

    default JsonObject getParams() { return getActivityContext().getParams(); }

    default Property<GuiNode> nodeProperty() { return getActivityContext().nodeProperty(); }

    default Property<GuiNode> mountNodeProperty() { return getActivityContext().mountNodeProperty(); }

}
