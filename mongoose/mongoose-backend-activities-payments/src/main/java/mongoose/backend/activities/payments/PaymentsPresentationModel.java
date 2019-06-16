package mongoose.backend.activities.payments;

import javafx.beans.property.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.shared.entities.MoneyTransfer;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class PaymentsPresentationModel extends EventDependentGenericTablePresentationModel {

    private final Property<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    Property<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final Property<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final Property<DisplayResult> slaveDisplayResultProperty = new SimpleObjectProperty<>();
    Property<DisplayResult> slaveDisplayResultProperty() { return slaveDisplayResultProperty; }

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    final StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }
    String getGroupStringFilter() { return groupStringFilterProperty().get(); }

    private final ObjectProperty<MoneyTransfer> selectedPaymentProperty = new SimpleObjectProperty<>();
    ObjectProperty<MoneyTransfer> selectedPaymentProperty() {
        return selectedPaymentProperty;
    }
    void setSelectedDocument(MoneyTransfer document) {
        selectedPaymentProperty.set(document);
    }

    private final ObjectProperty<MoneyTransfer> selectedGroupProperty = new SimpleObjectProperty<>();
    ObjectProperty<MoneyTransfer> selectedGroupProperty() {
        return selectedGroupProperty;
    }
    void setSelectedGroup(MoneyTransfer selectedGroup) {
        selectedGroupProperty.set(selectedGroup);
    }
    MoneyTransfer getSelectedGroup() {
        return selectedGroupProperty.get();
    }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

}
