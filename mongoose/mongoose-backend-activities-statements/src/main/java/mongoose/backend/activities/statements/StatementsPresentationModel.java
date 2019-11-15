package mongoose.backend.activities.statements;

import javafx.beans.property.*;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.*;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.framework.shared.orm.entity.Entity;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;

/**
 * @author Bruno Salmon
 */
final class StatementsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionEqlFilterStringProperty,
        HasGroupEqlFilterStringProperty,
        HasGroupVisualResultProperty,
        HasGroupVisualSelectionProperty,
        HasSelectedGroupProperty<MoneyTransfer>,
        HasSelectedGroupConditionEqlFilterStringProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterVisualResultProperty,
        HasMasterVisualSelectionProperty,
        HasSelectedMasterProperty<MoneyTransfer>,
        HasSelectedPaymentProperty,
        HasSlaveVisualResultProperty,
        HasSlaveVisibilityCondition<MoneyTransfer> {

    private final ObjectProperty<Entity> selectedMoneyAccountProperty = new SimpleObjectProperty<>();
    public ObjectProperty<Entity> selectedMoneyAccountProperty() { return selectedMoneyAccountProperty; }

    private final BooleanProperty flatPaymentsProperty = new SimpleBooleanProperty(true);
    public BooleanProperty flatPaymentsProperty() { return flatPaymentsProperty; }

    private final BooleanProperty flatBatchesProperty = new SimpleBooleanProperty(false);
    public BooleanProperty flatBatchesProperty() { return flatBatchesProperty; }

    private final StringProperty conditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty conditionEqlFilterStringProperty() { return conditionEqlFilterStringProperty; }

    private final StringProperty groupEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty groupEqlFilterStringProperty() { return groupEqlFilterStringProperty; }

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final ObjectProperty<VisualSelection> groupVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> groupVisualSelectionProperty() { return groupVisualSelectionProperty; }

    private final ObjectProperty<MoneyTransfer> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<MoneyTransfer> selectedGroupProperty() { return selectedGroupProperty; }

    private final StringProperty selectedGroupConditionEqlFilterStringProperty = new SimpleStringProperty();
    @Override public StringProperty selectedGroupConditionEqlFilterStringProperty() { return selectedGroupConditionEqlFilterStringProperty; }

    private ReferenceResolver selectedGroupReferenceResolver;
    @Override public ReferenceResolver getSelectedGroupReferenceResolver() { return selectedGroupReferenceResolver; }
    @Override public void setSelectedGroupReferenceResolver(ReferenceResolver referenceResolver) { this.selectedGroupReferenceResolver = referenceResolver; }

    private final ObjectProperty<VisualResult> masterVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> masterVisualResultProperty() { return masterVisualResultProperty; }

    private final ObjectProperty<VisualSelection> masterVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> masterVisualSelectionProperty() { return masterVisualSelectionProperty; }

    private final ObjectProperty<MoneyTransfer> selectedMasterProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<MoneyTransfer> selectedMasterProperty() { return selectedMasterProperty; }

    @Override public ObjectProperty<MoneyTransfer> selectedPaymentProperty() { return selectedMasterProperty(); }

    private final ObjectProperty<VisualResult> slaveVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> slaveVisualResultProperty() { return slaveVisualResultProperty; }

    @Override
    public boolean isSlaveVisible(MoneyTransfer selectedPayment) {
        return selectedPayment.getDocument() == null;
    }
}
