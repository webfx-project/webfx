package mongoose.backend.activities.payments;

import javafx.beans.property.*;
import mongoose.backend.controls.masterslave.group.HasGroupDisplayResultProperty;
import mongoose.backend.controls.masterslave.group.HasGroupDisplaySelectionProperty;
import mongoose.backend.controls.masterslave.group.HasSelectedGroupConditionStringFilterProperty;
import mongoose.backend.controls.masterslave.group.HasSelectedGroupProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.entities.util.filters.HasConditionStringFilterProperty;
import mongoose.client.entities.util.filters.HasGroupStringFilterProperty;
import mongoose.shared.entities.MoneyTransfer;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class PaymentsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasGroupDisplayResultProperty,
        HasGroupDisplaySelectionProperty,
        HasConditionStringFilterProperty,
        HasGroupStringFilterProperty,
        HasSelectedGroupProperty<MoneyTransfer>,
        HasSelectedGroupConditionStringFilterProperty {

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override
    public ObjectProperty<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final ObjectProperty<MoneyTransfer> selectedGroupProperty = new SimpleObjectProperty<>();
    public ObjectProperty<MoneyTransfer> selectedGroupProperty() {
        return selectedGroupProperty;
    }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override
    public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private final ObjectProperty<MoneyTransfer> selectedPaymentProperty = new SimpleObjectProperty<>();
    ObjectProperty<MoneyTransfer> selectedPaymentProperty() {
        return selectedPaymentProperty;
    }
    void setSelectedDocument(MoneyTransfer document) {
        selectedPaymentProperty.set(document);
    }

    private final ObjectProperty<DisplayResult> slaveDisplayResultProperty = new SimpleObjectProperty<>();
    public ObjectProperty<DisplayResult> slaveDisplayResultProperty() { return slaveDisplayResultProperty; }

}
