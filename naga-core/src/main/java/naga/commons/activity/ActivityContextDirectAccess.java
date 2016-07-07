package naga.commons.activity;

import javafx.beans.property.Property;
import naga.commons.json.spi.JsonObject;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.commons.url.history.spi.History;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * Mixin interface that can be used to have a direct access to the ActivityContext methods on any class implementing
 * HasActivityContext. When there are many calls to the ActivityContext, this can be convenient to just write
 * getHistory().push(...) instead of getActivityContext().getHistory().push(...) - for example.
 *
 * @author Bruno Salmon
 */
public interface ActivityContextDirectAccess extends HasActivityContext, ActivityContext {

    @Override
    default ActivityContext getParentContext() {
        return getActivityContext().getParentContext();
    }

    @Override
    default ActivityManager getActivityManager() { return getActivityContext().getActivityManager(); }

    @Override
    default void setDataSourceModel(DataSourceModel dataSourceModel) {
        getActivityContext().setDataSourceModel(dataSourceModel);
    }

    @Override
    default DataSourceModel getDataSourceModel() { return getActivityContext().getDataSourceModel(); }

    @Override
    default History getHistory() { return getActivityContext().getHistory(); }

    @Override
    default JsonObject getParams() { return getActivityContext().getParams(); }

    @Override
    default Property<GuiNode> nodeProperty() { return getActivityContext().nodeProperty(); }

    @Override
    default Property<GuiNode> mountNodeProperty() { return getActivityContext().mountNodeProperty(); }

}
