package mongoose.backend.activities.payments;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.Pane;
import mongoose.backend.controls.masterslave.ConventionalReactiveExpressionFilterFactoryMixin;
import mongoose.backend.controls.masterslave.ConventionalUiBuilder;
import mongoose.backend.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.backend.operations.entities.generic.CopyAllRequest;
import mongoose.backend.operations.entities.generic.CopySelectionRequest;
import mongoose.backend.operations.entities.moneytransfer.DeletePaymentRequest;
import mongoose.backend.operations.entities.moneytransfer.EditPaymentRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.layouts.LayoutUtil;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;

final class PaymentsActivity extends EventDependentViewDomainActivity implements
        ConventionalUiBuilderMixin,
        ConventionalReactiveExpressionFilterFactoryMixin,
        OperationActionFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final PaymentsPresentationModel pm = new PaymentsPresentationModel();

    @Override
    public PaymentsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private ConventionalUiBuilder ui; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        ui = createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, "payments", "MoneyTransfer");

        CheckBox flatPaymentsCheckBox = newCheckBox("Flat payments");
        flatPaymentsCheckBox.setSelected(pm.flatPaymentsProperty().get());
        pm.flatPaymentsProperty().bind(flatPaymentsCheckBox.selectedProperty());

        ui.setLeftTopNodes(flatPaymentsCheckBox);

        Pane container = ui.buildUi();
        setUpContextMenu(LayoutUtil.lookupChild(container, node -> node instanceof DataGrid), () -> newActionGroup(
                newSeparatorActionGroup(
                        newOperationAction(() -> new EditPaymentRequest(   pm.getSelectedPayment(), container)),
                        newOperationAction(() -> new DeletePaymentRequest( pm.getSelectedPayment(), container))
                ),
                newSeparatorActionGroup(
                        newOperationAction(() -> new CopySelectionRequest( masterFilter.getSelectedEntities(),  masterFilter.getExpressionColumns())),
                        newOperationAction(() -> new CopyAllRequest(       masterFilter.getCurrentEntityList(), masterFilter.getExpressionColumns()))
                )));
        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        ui.onResume();
    }

    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<MoneyTransfer> groupFilter, masterFilter, slaveFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        groupFilter = this.<MoneyTransfer>createGroupReactiveExpressionFilter(pm, "{class: 'MoneyTransfer', alias: 'mt', where: '!receiptsTransfer', orderBy: 'date desc,parent nulls first,id'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                .combineIfFalse(pm.flatPaymentsProperty(), () -> "{where: `parent=null`}")
                // Everything set up, let's start now!
                .start();

        // Setting up the master filter that controls the content displayed in the master view
        masterFilter = this.<MoneyTransfer>createMasterReactiveExpressionFilter(pm, "{class: 'MoneyTransfer', alias: 'mt', where: '!receiptsTransfer', orderBy: 'date desc,parent nulls first,id'}")
                .combine("{columns: 'date,document,transactionRef,status,comment,amount,methodIcon,pending,successful'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document..event=" + eventId + " or document is null and exists(select MoneyTransfer where parent=mt and document.event=" + eventId + ")`}")
                // Applying the flat mode
                .combineIfFalse(pm.flatPaymentsProperty(), () -> "{where: `parent=null`}")
                // Applying the user search
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? "{where: `document.ref = " + s + "`}"
                                : s.contains("@") ? "{where: `lower(document.person_email) like '%" + s.toLowerCase() + "%'`}"
                                : "{where: `document.person_abcNames like '" + AbcNames.evaluate(s, true) + "'`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // When the result is a singe row, automatically select it
                .autoSelectSingleRow()
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();

        // Slave filter
        slaveFilter = this.<MoneyTransfer>createSlaveReactiveExpressionFilter(pm, "{class: 'MoneyTransfer', alias: 'mt', orderBy: 'date desc,parent nulls first,id'}")
                .combine("{columns: 'date,document,transactionRef,status,comment,amount,methodIcon,pending,successful'}")
                // Applying the selection condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedPaymentProperty(), mt -> "{where: 'parent=" + mt.getPrimaryKey() + "'}")
                // Applying the flat mode
                .combineIfFalse(pm.flatPaymentsProperty(), () -> "{where: `parent=null`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupFilter .refreshWhenActive();
        masterFilter.refreshWhenActive();
        slaveFilter .refreshWhenActive();
    }
}
