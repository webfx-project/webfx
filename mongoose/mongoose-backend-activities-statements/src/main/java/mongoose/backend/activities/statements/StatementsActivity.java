package mongoose.backend.activities.statements;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import mongoose.backend.controls.masterslave.ConventionalReactiveVisualFilterFactoryMixin;
import mongoose.backend.controls.masterslave.ConventionalUiBuilder;
import mongoose.backend.controls.masterslave.ConventionalUiBuilderMixin;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;
import webfx.framework.client.ui.controls.button.ButtonSelector;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.shared.orm.entity.Entity;

import static webfx.framework.client.orm.dql.DqlStatement.where;

final class StatementsActivity extends EventDependentViewDomainActivity implements
        ConventionalUiBuilderMixin,
        ConventionalReactiveVisualFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final StatementsPresentationModel pm = new StatementsPresentationModel();

    @Override
    public StatementsPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private ConventionalUiBuilder ui; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        ui = createAndBindGroupMasterSlaveViewWithFilterSearchBar(pm, "statements", "MoneyTransfer");

        EntityButtonSelector<Entity> moneyAccountSelector = new EntityButtonSelector<>("{class: 'MoneyAccount', alias: 'ma', columns: [{expression: `[icon,name,currency.icon]`}], where: 'type.internal=true', orderBy: 'closed,type desc,exists(select MoneyFlow where toMoneyAccount=ma and positiveAmounts) ? 0 : 1,id desc'}", this, ui::buildUi, getDataSourceModel());
        moneyAccountSelector.autoSelectFirstEntity();
        moneyAccountSelector.setAutoOpenOnMouseEntered(true);
        moneyAccountSelector.setShowMode(ButtonSelector.ShowMode.DROP_DOWN);
        moneyAccountSelector.getEntityDialogFilter()
                //.combineIfNotNullOtherwiseForceEmptyResult(pm.organizationIdProperty(), organizationId -> where("organization=?", organizationId))
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> where("(event=? or event=null) and organization=(select organization from Event where id=?)", eventId, eventId));
        pm.selectedMoneyAccountProperty().bind(moneyAccountSelector.selectedItemProperty());

        CheckBox flatPaymentsCheckBox = newCheckBox("Flat payments");
        flatPaymentsCheckBox.setSelected(pm.flatPaymentsProperty().get());
        pm.flatPaymentsProperty().bind(flatPaymentsCheckBox.selectedProperty());

        CheckBox flatBatchesCheckBox = newCheckBox("Flat batches");
        flatBatchesCheckBox.setSelected(pm.flatBatchesProperty().get());
        pm.flatBatchesProperty().bind(flatBatchesCheckBox.selectedProperty());

        ui.setLeftTopNodes(flatPaymentsCheckBox, moneyAccountSelector.getButton());
        ui.setRightTopNodes(flatBatchesCheckBox);

        return ui.buildUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        ui.onResume();
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveVisualFilter<MoneyTransfer> groupFilter, masterFilter, slaveFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        groupFilter = this.<MoneyTransfer>createGroupReactiveVisualFilter(pm, "{class: 'MoneyTransfer', alias: 'mt', orderBy: 'date desc,parent nulls first,id'}")
                // Applying the money account condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedMoneyAccountProperty(), ma -> where("parent = null and (fromMoneyAccount=? or toMoneyAccount=?) or parent != null and (parent..fromMoneyAccount=? or parent..toMoneyAccount=?", ma.getPrimaryKey(), ma.getPrimaryKey(), ma.getPrimaryKey(), ma.getPrimaryKey()))
                .combineStringIfFalse(pm.flatPaymentsProperty(), () -> "{where: `parent=null`}")
                .combineStringIfFalse(pm.flatBatchesProperty(), () -> "{where: `transfer=null and (parent=null || parent..transfer=null)`}")
                // Everything set up, let's start now!
                .start();

        // Setting up the master filter that controls the content displayed in the master view
        masterFilter = this.<MoneyTransfer>createMasterReactiveVisualFilter(pm, "{class: 'MoneyTransfer', alias: 'mt', orderBy: 'date desc,parent nulls first,id'}")
                .combine("{columns: 'date,document.event,document,transactionRef,status,comment,amount,methodIcon,pending,successful'}")
                // Applying the money account condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedMoneyAccountProperty(), ma -> where("parent = null and (fromMoneyAccount=? or toMoneyAccount=?) or parent != null and (parent..fromMoneyAccount=? or parent..toMoneyAccount=?)", ma.getPrimaryKey(), ma.getPrimaryKey(), ma.getPrimaryKey(), ma.getPrimaryKey()))
                // Applying the flat modes
                .combineStringIfFalse(pm.flatPaymentsProperty(), () -> "{where: `parent=null`}")
                .combineStringIfFalse(pm.flatBatchesProperty(), () -> "{where: `transfer=null and (parent=null || parent..transfer=null)`}")
                // Applying the user search
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? where("document.ref=?", Integer.parseInt(s))
                                : s.contains("@") ? where("lower(document.person_email) like ?", "%" + s.toLowerCase() + "%")
                                : where("document.person_abcNames like ?", AbcNames.evaluate(s, true)))
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // When the result is a singe row, automatically select it
                .autoSelectSingleRow()
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();

        // Slave filter
        slaveFilter = this.<MoneyTransfer>createSlaveReactiveVisualFilter(pm, "{class: 'MoneyTransfer', alias: 'mt', orderBy: 'date desc,parent nulls first,id'}")
                .combine("{columns: 'date,document.event,document,transactionRef,status,comment,amount,methodIcon,pending,successful'}")
                // Applying the selection condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedPaymentProperty(), mt -> where("parent=? or transfer=? or parent..transfer=?", mt.getPrimaryKey(), mt.getPrimaryKey(), mt.getPrimaryKey()))
                // Applying the flat modes
                .combineStringIfFalse(pm.flatPaymentsProperty(), () -> "{where: `parent=null`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        groupFilter.refreshWhenActive();
        masterFilter.refreshWhenActive();
        slaveFilter.refreshWhenActive();
    }
}
