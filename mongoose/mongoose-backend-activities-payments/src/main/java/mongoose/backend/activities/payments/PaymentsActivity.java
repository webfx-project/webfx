package mongoose.backend.activities.payments;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import mongoose.backend.controls.masterslave.MasterTableView;
import mongoose.backend.controls.masterslave.SlaveTableView;
import mongoose.backend.controls.masterslave.group.GroupMasterSlaveView;
import mongoose.backend.controls.masterslave.group.GroupView;
import mongoose.client.activity.eventdependent.EventDependentPresentationModel;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.entities.util.filters.FilterButtonSelectorFactoryMixin;
import mongoose.client.entities.util.filters.FilterSearchBar;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.fxkit.util.properties.Properties;

final class PaymentsActivity extends EventDependentViewDomainActivity implements
        FilterButtonSelectorFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    /*==================================================================================================================
    ================================================= Graphical layer ==================================================
    ==================================================================================================================*/

    private final PaymentsPresentationModel pm = new PaymentsPresentationModel();

    @Override
    public EventDependentPresentationModel getPresentationModel() {
        return pm; // eventId and organizationId will then be updated from route
    }

    private FilterSearchBar filterSearchBar; // Keeping this reference for activity resume

    @Override
    public Node buildUi() {
        BorderPane container = new BorderPane();

        // Building the filter search bar and put it on top
        filterSearchBar = createFilterSearchBar("payments", "MoneyTransfer", container, pm);
        container.setTop(filterSearchBar.buildUi());

        container.setCenter(
                GroupMasterSlaveView.createAndBind(Orientation.VERTICAL,
                        GroupView.createAndBind(pm),
                        MasterTableView.createAndBind(this, pm).buildUi(),
                        SlaveTableView.createAndBind(pm).buildUi(),
                        pm.selectedPaymentProperty(), selectedPayment -> selectedPayment.getDocument() == null
                ).buildUi());

        return container;
    }

    @Override
    public void onResume() {
        super.onResume();
        filterSearchBar.onResume();
    }


    /*==================================================================================================================
    =================================================== Logical layer ==================================================
    ==================================================================================================================*/

    private ReactiveExpressionFilter<MoneyTransfer> groupFilter, masterFilter, slaveFilter;

    @Override
    protected void startLogic() {
        // Setting up the group filter that controls the content displayed in the group view
        groupFilter = this.<MoneyTransfer>createReactiveExpressionFilter("{class: 'MoneyTransfer', alias: 'mt', where: '!receiptsTransfer', orderBy: 'date desc,parent nulls first'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document.event=" + eventId + "`}")
                // Applying the condition and group selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                .combineIfNotNullOtherwiseForceEmptyResult(pm.groupStringFilterProperty(), stringFilter -> stringFilter.contains("groupBy") ? stringFilter : "{where: 'false'}")
                // Displaying the result in the group view
                .displayResultInto(pm.groupDisplayResultProperty())
                // Reacting to a group selection
                .setSelectedEntityHandler(pm.groupDisplaySelectionProperty(), pm::setSelectedGroup)
                // Everything set up, let's start now!
                .start();
        pm.setSelectedGroupReferenceResolver(groupFilter.getRootAliasReferenceResolver());

        // Setting up the master filter that controls the content displayed in the master view
        masterFilter = this.<MoneyTransfer>createReactiveExpressionFilter("{class: 'MoneyTransfer', alias: 'mt', where: '!receiptsTransfer', orderBy: 'date desc,parent nulls first'}")
                .combine("{columns: 'date,document,transactionRef,status,comment,amount,methodIcon,pending,successful'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.eventIdProperty(), eventId -> "{where:  `document..event=" + eventId + " or document is null and exists(select MoneyTransfer where parent=mt and document.event=" + eventId + ")`}")
                // Applying the condition and columns selected by the user
                .combineIfNotNullOtherwiseForceEmptyResult(pm.conditionStringFilterProperty(), stringFilter -> stringFilter)
                // Also, in case groups are showing and a group is selected, applying the condition associated with that group
                .combineIfNotNull(pm.selectedGroupConditionStringFilterProperty(), s -> s)
                // Applying the user search
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? "{where: `document.ref = " + s + "`}"
                                : s.contains("@") ? "{where: `lower(document.person_email) like '%" + s.toLowerCase() + "%'`}"
                                : "{where: `document.person_abcNames like '" + AbcNames.evaluate(s, true) + "'`}")
                // Limit clause
                .combineIfPositive(pm.limitProperty(), limit -> "{limit: `" + limit + "`}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Displaying the result in the master view
                .displayResultInto(pm.masterDisplayResultProperty())
                // When the result is a singe row, automatically select it
                .autoSelectSingleRow()
                // Reacting the a booking selection
                .setSelectedEntityHandler(pm.masterDisplaySelectionProperty(), pm::setSelectedMaster)
                // Activating server push notification
                .setPush(true)
                // Everything set up, let's start now!
                .start();

        // Slave filter
        slaveFilter = this.<MoneyTransfer>createReactiveExpressionFilter("{class: 'MoneyTransfer', alias: 'mt', orderBy: 'date desc,parent nulls first'}")
                .combine("{columns: 'date,document,transactionRef,status,comment,amount,methodIcon,pending,successful'}")
                .combineIfTrue(Properties.compute(pm.selectedPaymentProperty(), p -> p == null || p.getDocument() != null), () -> "{where: 'false'}")
                // Applying the event condition
                .combineIfNotNullOtherwiseForceEmptyResult(pm.selectedPaymentProperty(), mt -> "{where: 'parent=" + mt.getPrimaryKey() + "'}")
                // Colorizing the rows
                .applyDomainModelRowStyle()
                // Displaying the result in the master view
                .displayResultInto(pm.slaveDisplayResultProperty())
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
