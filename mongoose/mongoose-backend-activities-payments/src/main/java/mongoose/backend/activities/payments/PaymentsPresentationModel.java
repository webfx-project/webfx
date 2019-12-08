package mongoose.backend.activities.payments;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import mongoose.client.activity.eventdependent.EventDependentGenericTablePresentationModel;
import mongoose.client.presentationmodel.HasSelectedPaymentProperty;
import webfx.framework.client.orm.reactive.dql.statement.conventions.*;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasSlaveVisualResultProperty;
import mongoose.shared.entities.MoneyTransfer;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.VisualSelection;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasGroupVisualResultProperty;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasGroupVisualSelectionProperty;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasMasterVisualResultProperty;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.conventions.HasMasterVisualSelectionProperty;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;

/**
 * @author Bruno Salmon
 */
final class PaymentsPresentationModel extends EventDependentGenericTablePresentationModel implements
        HasConditionDqlStatementProperty,
        HasGroupDqlStatementProperty,
        HasGroupVisualResultProperty,
        HasGroupVisualSelectionProperty,
        HasSelectedGroupProperty<MoneyTransfer>,
        HasSelectedGroupConditionDqlStatementProperty,
        HasSelectedGroupReferenceResolver,
        HasMasterVisualResultProperty,
        HasMasterVisualSelectionProperty,
        HasSelectedMasterProperty<MoneyTransfer>,
        HasSelectedPaymentProperty,
        HasSlaveVisualResultProperty,
        HasSlaveVisibilityCondition<MoneyTransfer> {

    private final BooleanProperty flatPaymentsProperty = new SimpleBooleanProperty(true);
    public BooleanProperty flatPaymentsProperty() { return flatPaymentsProperty; }

    private final ObjectProperty<DqlStatement> conditionDqlStatementProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DqlStatement> conditionDqlStatementProperty() { return conditionDqlStatementProperty; }

    private final ObjectProperty<DqlStatement> groupDqlStatementProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DqlStatement> groupDqlStatementProperty() { return groupDqlStatementProperty; }

    private final ObjectProperty<VisualResult> groupVisualResultProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualResult> groupVisualResultProperty() { return groupVisualResultProperty; }

    private final ObjectProperty<VisualSelection> groupVisualSelectionProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<VisualSelection> groupVisualSelectionProperty() { return groupVisualSelectionProperty; }

    private final ObjectProperty<MoneyTransfer> selectedGroupProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<MoneyTransfer> selectedGroupProperty() { return selectedGroupProperty; }

    private final ObjectProperty<DqlStatement> selectedGroupConditionDqlStatementProperty = new SimpleObjectProperty<>();
    @Override public ObjectProperty<DqlStatement> selectedGroupConditionDqlStatementProperty() { return selectedGroupConditionDqlStatementProperty; }

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
