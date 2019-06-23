package mongoose.backend.activities.statements;

import javafx.beans.property.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.*;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.shared.expression.builder.ReferenceResolver;
import webfx.framework.shared.orm.entity.Entity;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.fxkit.extra.displaydata.DisplaySelection;

/**
 * @author Bruno Salmon
 */
final class StatementsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionStringFilterProperty,
        HasGroupStringFilterProperty,
        HasGroupDisplayResultProperty,
        HasGroupDisplaySelectionProperty,
        HasSelectedGroupProperty<MoneyTransfer>,
        HasSelectedGroupConditionStringFilterProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterDisplayResultProperty,
        HasMasterDisplaySelectionProperty,
        HasSelectedMasterProperty<MoneyTransfer>,
        HasSelectedPaymentProperty,
        HasSlaveDisplayResultProperty,
        HasSlaveVisibilityCondition<MoneyTransfer> {

    private final ObjectProperty<Entity> selectedMoneyAccountProperty = new SimpleObjectProperty<>();
    public ObjectProperty<Entity> selectedMoneyAccountProperty() { return selectedMoneyAccountProperty; }

    private final BooleanProperty flatPaymentsProperty = new SimpleBooleanProperty(true);
    public BooleanProperty flatPaymentsProperty() { return flatPaymentsProperty; }

    private final BooleanProperty flatBatchesProperty = new SimpleBooleanProperty(false);
    public BooleanProperty flatBatchesProperty() { return flatBatchesProperty; }

    private final StringProperty conditionStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty conditionStringFilterProperty() { return conditionStringFilterProperty; }

    private final StringProperty groupStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty groupStringFilterProperty() { return groupStringFilterProperty; }

    private final ObjectProperty<DisplayResult> groupDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> groupDisplayResultProperty() { return groupDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> groupDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> groupDisplaySelectionProperty() { return groupDisplaySelectionProperty; }

    private final ObjectProperty<MoneyTransfer> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<MoneyTransfer> selectedGroupProperty() { return selectedGroupProperty; }

    private final StringProperty selectedGroupConditionStringFilterProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionStringFilterProperty() { return selectedGroupConditionStringFilterProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    @Override public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<DisplayResult> masterDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> masterDisplayResultProperty() { return masterDisplayResultProperty; }

    private final ObjectProperty<DisplaySelection> masterDisplaySelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplaySelection> masterDisplaySelectionProperty() { return masterDisplaySelectionProperty; }

    private final ObjectProperty<MoneyTransfer> selectedMasterProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<MoneyTransfer> selectedMasterProperty() { return selectedMasterProperty; }

    @Override public ObjectProperty<MoneyTransfer> selectedPaymentProperty() { return selectedMasterProperty(); }

    private final ObjectProperty<DisplayResult> slaveDisplayResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DisplayResult> slaveDisplayResultProperty() { return slaveDisplayResultProperty; }

    @Override
    public boolean isSlaveVisible(MoneyTransfer selectedPayment) {
        return selectedPayment.getDocument() == null;
    }
}
