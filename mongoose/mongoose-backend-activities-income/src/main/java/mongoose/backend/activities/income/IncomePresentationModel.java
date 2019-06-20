package mongoose.backend.activities.income;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import mongoose.client.presentationmodel.HasGroupDisplayResultProperty;
import mongoose.client.presentationmodel.HasSelectedGroupConditionStringFilterProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.HasGroupStringFilterProperty;
import webfx.fxkit.extra.displaydata.DisplayResult;

/**
 * @author Bruno Salmon
 */
final class IncomePresentationModel extends EventDependentGenericTablePresentationModel implements
        HasGroupDisplayResultProperty,
        HasGroupStringFilterProperty,
        HasSelectedGroupConditionStringFilterProperty {

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

}
