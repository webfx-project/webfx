package mongoose.backend.activities.income;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.HasGroupVisualResultProperty;
import mongoose.client.presentationmodel.HasGroupEqlFilterStringProperty;
import webfx.extras.visual.VisualResult;

/**
 * @author Bruno Salmon
 */
final class IncomePresentationModel extends EventDependentGenericTablePresentationModel implements
        HasGroupVisualResultProperty,
        HasGroupEqlFilterStringProperty {

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final StringProperty groupEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty groupEqlFilterStringProperty() { return groupEqlFilterStringProperty; }

}
