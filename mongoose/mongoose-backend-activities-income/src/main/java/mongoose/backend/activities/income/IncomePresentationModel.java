package mongoose.backend.activities.income;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.HasGroupDqlStatementProperty;
import mongoose.client.presentationmodel.HasGroupVisualResultProperty;
import webfx.extras.visual.VisualResult;
import webfx.framework.client.orm.entity.filter.DqlStatement;

/**
 * @author Bruno Salmon
 */
final class IncomePresentationModel extends EventDependentGenericTablePresentationModel implements
        HasGroupVisualResultProperty,
        HasGroupDqlStatementProperty {

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final ObjectProperty<DqlStatement> groupDqlStatementProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DqlStatement> groupDqlStatementProperty() { return groupDqlStatementProperty; }

}
