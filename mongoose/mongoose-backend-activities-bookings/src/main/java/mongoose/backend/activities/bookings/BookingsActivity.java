package mongoose.backend.activities.bookings;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import mongoose.backend.operations.bookings.RouteToNewBackendBookingRequest;
import mongoose.backend.operations.cloneevent.RouteToCloneEventRequest;
import mongoose.client.activity.eventdependent.EventDependentViewDomainActivity;
import mongoose.client.activity.table.GenericTableView;
import mongoose.client.controls.personaldetails.PersonalDetailsPanel;
import mongoose.shared.domainmodel.functions.AbcNames;
import mongoose.shared.entities.Document;
import webfx.framework.client.operation.action.OperationActionFactoryMixin;
import webfx.framework.client.ui.controls.button.EntityButtonSelector;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.framework.shared.orm.entity.impl.DynamicEntity;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.util.ImageStore;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.services.json.WritableJsonObject;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.Strings;

import java.time.LocalDate;

import static mongoose.backend.activities.bookings.routing.BookingsRouting.parseDayParam;
import static webfx.framework.client.ui.layouts.LayoutUtil.*;
import static webfx.framework.shared.expression.sqlcompiler.terms.ConstantSqlCompiler.toSqlDate;

final class BookingsActivity extends EventDependentViewDomainActivity
        implements OperationActionFactoryMixin,
        ReactiveExpressionFilterFactoryMixin {

    private GenericTableView genericTableView;
    private final BookingsPresentationModel pm = new BookingsPresentationModel();
    private ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();

    @Override
    public Node buildUi() {
        Button newBookingButton = newButton(newAction(() -> new RouteToNewBackendBookingRequest(getEventId(), getHistory())));
        Button cloneEventButton = newButton(newAction(() -> new RouteToCloneEventRequest(getEventId(), getHistory())));
        return new StackPane((genericTableView = new GenericTableView(buildBookingDetails()) {
            @Override
            public void initUi() {
                super.initUi();
                EntityButtonSelector<DynamicEntity> columnsSelector = new EntityButtonSelector<>("{class: 'Filter', fields: 'columns', where: `isColumns and class='Document'`, orderBy: 'name'}", this, borderPane, getDataSourceModel());
                borderPane.setTop(new HBox(10, setUnmanagedWhenInvisible(newBookingButton), columnsSelector.getButton(), setMaxHeightToInfinite(setHGrowable(searchBox)), setUnmanagedWhenInvisible(cloneEventButton)));

                // Initialization from the presentation model current state
                searchBox.setText(pm.searchTextProperty().getValue());

                // Binding the UI with the presentation model for further state changes
                // User inputs: the UI state changes are transferred in the presentation model
                pm.searchTextProperty().bind(searchBox.textProperty());
                Properties.runNowAndOnPropertiesChange(() -> pm.limitProperty().setValue(limitCheckBox.isSelected() ? 20 : -1), limitCheckBox.selectedProperty());
                table.fullHeightProperty().bind(limitCheckBox.selectedProperty());
                //pm.limitProperty().bind(limitCheckBox.selectedProperty());
                pm.genericDisplaySelectionProperty().bind(table.displaySelectionProperty());
                // User outputs: the presentation model changes are transferred in the UI
                table.displayResultProperty().bind(pm.genericDisplayResultProperty());
                genericTableView.getMasterSlaveView().slaveVisibleProperty().bind(Properties.compute(selectedDocumentProperty, java.util.Objects::nonNull));
                pm.columnsProperty().bind(Properties.compute(columnsSelector.selectedItemProperty(), filter -> filter == null ? null : filter.getStringFieldValue("columns")));
            }
        }).buildUi());
    }

    private String inlineFilter(DynamicEntity filter) {
        return null;
    }

    private Node buildBookingDetails() {
//        Button button = new Button(document.getFullName());
        return new VBox(/*button, */new TabPane(
                createTab("Personal details", "images/s16/personalDetails.png", buildPersonalDetailsView()),
                createFilterTab("Options", "images/s16/options.png", "{class: 'DocumentLine', columns: `['site','item','dates','lockAllocation','resourceConfiguration','comment','price_isCustom',{expression: 'price_net', format:'price'},{expression: 'price_nonRefundable', format: 'price'},{expression: 'price_minDeposit', format: 'price'},{expression: 'price_deposit', format: 'price'}]`, where: 'document=${selectedDocument}', orderBy: 'item.family.ord,site..ord,item.ord'}"),
                createFilterTab("Payments", "images/s16/methods/generic.png", "{class: 'MoneyTransfer', columns: `['date','method','transactionRef','comment',{expression:'amount', format:'price'},'verified']`, where: 'document=${selectedDocument}', orderBy: 'date,id'}"),
                createTab("Comments", "images/s16/note.png", buildCommentView()),
                createFilterTab("Cart", "images/s16/cart.png", "{class: 'Document', columns:`['ref','multipleBookingIcon','langIcon','genderIcon','person_firstName','person_lastName','person_age','noteIcon',{expression: 'price_net', format:'price'},{expression: 'price_deposit', format: 'price'},{expression: 'price_balance', format: 'price'}]`, where: 'cart=(select cart from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("Multiple bookings", "images/s16/multipleBookings/redCross.png", "{class: 'Document', columns:`['ref','multipleBookingIcon','langIcon','genderIcon','person_firstName','person_lastName','person_age','noteIcon',{expression: 'price_deposit', format: 'price'},'plainOptions']`, where: 'multipleBooking=(select multipleBooking from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("Children", "images/s16/child.png", "{class: 'Document', columns:`['ref','multipleBookingIcon','langIcon','genderIcon','person_firstName','person_lastName','person_age','noteIcon',{expression: 'price_deposit', format: 'price'},'plainOptions']`, where: 'person_carer1Document=${selectedDocument} or person_carer2Document=${selectedDocument}', orderBy: 'ref'}"),
                createFilterTab("Mails", "images/s16/mailbox.png", "{class: 'Mail', columns: 'date,subject,transmitted,error', where: 'document=${selectedDocument}', orderBy: 'date desc'}"),
                createFilterTab("History", "images/s16/history.png", "{class: 'History', columns: 'date,username,comment,request', where: 'document=${selectedDocument}', orderBy: 'date desc'}")
        ));
    }

    private static Tab createTab(String text, String iconUrl, Node node) {
        Tab tab = new Tab(text);
        tab.setGraphic(ImageStore.createImageView(iconUrl));
        tab.setContent(node);
        tab.setClosable(false);
        return tab;
    }

    private Tab createFilterTab(String text, String iconUrl, String filter) {
        DataGrid table = new DataGrid();
        String classOnly = filter.substring(0, filter.indexOf(',')) + "}";
        createReactiveExpressionFilter(classOnly)
                .combineIfNotNullOtherwiseForceEmptyResult(selectedDocumentProperty, document -> Strings.replaceAll(filter, "${selectedDocument}", document.getPrimaryKey()))
                .applyDomainModelRowStyle()
                .displayResultInto(table.displayResultProperty())
                .start();
        return createTab(text, iconUrl, table);
    }

    GridPane gridPane = new GridPane();
    private Node buildPersonalDetailsView() {
        gridPane.setHgap(0);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));
        gridPane.setMinHeight(150);

        ColumnConstraints cc5p = new ColumnConstraints();
        cc5p.setPercentWidth(5);
        cc5p.setHgrow(Priority.NEVER);
        ColumnConstraints cc10p = new ColumnConstraints();
        cc10p.setPercentWidth(10);
        cc10p.setHgrow(Priority.NEVER);
        ColumnConstraints cc15p = new ColumnConstraints();
        cc15p.setPercentWidth(15);
        cc15p.setHgrow(Priority.NEVER);

        gridPane.getColumnConstraints().setAll(cc10p, cc10p, cc5p, cc5p, cc5p, cc10p, cc5p, cc10p, cc10p, cc5p, cc5p, cc5p, cc10p, cc5p);

        RowConstraints rc = new RowConstraints();
        rc.setMinHeight(3.5);
        gridPane.getRowConstraints().setAll(rc, rc, rc, rc, rc);

        addFieldLabelAndValue(0, 0, 6, "person_firstName");
        addFieldLabelAndValue(1, 0, 6, "person_lastName");
        addFieldLabelAndValue(2, 0, 1, "person_age");
        addFieldLabelAndValue(3, 0, 6, "person_email");
        addFieldLabelAndValue(4, 0, 6, "person_organization");
        addFieldLabelAndValue(0, 7, 6, "person_phone");
        addFieldLabelAndValue(1, 7, 6, "person_cityName");
        addFieldLabelAndValue(2, 7, 6, "person_country");
        addFieldLabelAndValue(3, 7, 6, "person_carer1Name");
        addFieldLabelAndValue(4, 7, 6, "person_carer2Name");

        gridPane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                editPersonalDetails();
        });

        return gridPane;
    }

    private void editPersonalDetails() {
        Document document = selectedDocumentProperty.get();
        PersonalDetailsPanel details = new PersonalDetailsPanel(document.getEvent(), BookingsActivity.this, (Pane) getNode());
        details.setEditable(true);
        details.syncUiFromModel(document);
        BorderPane sectionPanel = details.getSectionPanel();
        ScrollPane scrollPane = new ScrollPane(sectionPanel);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        sectionPanel.setPrefWidth(400);
        scrollPane.setPrefWidth(400);
        scrollPane.setPrefHeight(600);
        //scrollPane.setFitToWidth(true);
        DialogContent dialogContent = new DialogContent().setContent(scrollPane);
        DialogUtil.showModalNodeInGoldLayout(dialogContent, (Pane) getNode(), 0, 0.9);
        DialogUtil.armDialogContentButtons(dialogContent, dialogCallback -> {
            details.isValid();
            //dialogCallback.closeDialog();
            /*
            syncModelFromUi();
            if (!updateStore.hasChanges())
                dialogCallback.closeDialog();
            else {
                updateStore.executeUpdate().setHandler(ar -> {
                    if (ar.failed())
                        dialogCallback.showException(ar.cause());
                    else
                        dialogCallback.closeDialog();
                });
            }
*/
        });
    }

    private void addFieldLabelAndValue(int rowIndex, int columnIndex, int columnSpan, String fieldName) {
        webfx.fxkit.extra.label.Label fieldLabel = getDomainModel().getClass("Document").getField(fieldName).getLabel();
        ObservableValue<String> fieldValueProperty = Properties.compute(selectedDocumentProperty, document -> {
            Object fieldValue = document == null ? null : document.getFieldValue(fieldName);
            if (fieldValue instanceof EntityId)
                fieldValue = document.getForeignEntity(fieldName).getFieldValue("name");
            return fieldValue == null ? null : fieldValue.toString();
        });
        Label valueLabel = new Label();
        valueLabel.textProperty().bind(fieldValueProperty);
        addNodeToGrid(rowIndex, columnIndex, 1, new Label(fieldLabel.getText(), ImageStore.createImageView(fieldLabel.getIconPath())));
        addNodeToGrid(rowIndex, columnIndex + 1, columnSpan, valueLabel);
    }

    private void addNodeToGrid(int rowIndex, int columnIndex, int columnSpan, Node node) {
        GridPane.setRowIndex(node, rowIndex);
        GridPane.setColumnIndex(node, columnIndex);
        GridPane.setColumnSpan(node, columnSpan);
        gridPane.getChildren().add(node);
    }

    private Node buildCommentView() {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        //gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5));

        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(100);
        gridPane.getRowConstraints().add(rc);

        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(33.3333);
        ObservableList<ColumnConstraints> columnConstraints = gridPane.getColumnConstraints();
        columnConstraints.add(cc);
        columnConstraints.add(cc);
        columnConstraints.add(cc);

        gridPane.getChildren().setAll(createComment("Person request", "request"), createComment("Registration comment", "comment"), createComment("Special needs", "specialNeeds"));

        return gridPane;
    }

    private int columnIndex;
    private Node createComment(String title, String commentField) {
        TextArea textArea = new TextArea();
        textArea.textProperty().bind(Properties.compute(selectedDocumentProperty, document -> document == null ? null : document.getStringFieldValue(commentField)));
        textArea.setEditable(false);
        TitledPane titledPane = new TitledPane(title, textArea);
        titledPane.setCollapsible(false);
        titledPane.setMaxHeight(Double.MAX_VALUE);
        GridPane.setColumnIndex(titledPane, columnIndex++);
        return titledPane;
    }

    @Override
    public void onResume() {
        super.onResume();
        genericTableView.onResume();
    }

    @Override
    protected void updateContextParametersFromRoute() {
        super.updateContextParametersFromRoute();
        String routingPath = getRoutingPath();
        WritableJsonObject contextParams = (WritableJsonObject) getParams(); // not beautiful...
        contextParams.set("arrivals", Strings.contains(routingPath, "/arrivals"));
        contextParams.set("departures", Strings.contains(routingPath, "/departures"));
    }

    @Override
    protected void updateModelFromContextParameters() {
        LocalDate day;
        setActive(false);
        super.updateModelFromContextParameters(); // for eventId and organizationId
        pm.setColumns(getParameter("columns"));
        pm.setDay(day = parseDayParam(getParameter("day")));
        pm.setArrivals(day != null && Booleans.isTrue(getParameter("arrivals")));
        pm.setDepartures(day != null && Booleans.isTrue(getParameter("departures")));
        pm.setMinDay(parseDayParam(getParameter("minDay")));
        pm.setMaxDay(parseDayParam(getParameter("maxDay")));
        pm.setFilter(getParameter("filter"));
        pm.setGroupBy(getParameter("groupBy"));
        pm.setOrderBy(getParameter("orderBy"));
        pm.setEventId(getEventId());
        pm.setOrganizationId(getOrganizationId());
        setActive(true);
    }

    private static final String DEFAULT_COLUMNS = "[" +
            "'ref'," +
            "'multipleBookingIcon','countryOrLangIcon','genderIcon'," +
            "'person_firstName'," +
            "'person_lastName'," +
            "'person_age','noteIcon'," +
            "{expression: 'price_net', format: 'price'}," +
            "{expression: 'price_minDeposit', format: 'price'}," +
            "{expression: 'price_deposit', format: 'price'}," +
            "{expression: 'price_balance', format: 'price'}" +
            "]";
    private static final String DEFAULT_FILTER = "!cancelled";
    private static final String DEFAULT_ORDER_BY = "ref desc";
    private ReactiveExpressionFilter<Document> filter;

    @Override
    protected void startLogic() {
        filter = this.<Document>createReactiveExpressionFilter("{class: 'Document', alias: 'd', fields: 'cart.uuid'}")
                // Fields required for personal details
                .combine("{fields: 'person_firstName,person_lastName,person_age,person_email,person_organization,person_phone,person_cityName,person_country,person_carer1Name,person_carer2Name'}")
                // Columns to display
                .combine(pm.columnsProperty(), columns -> "{columns: `" + Objects.coalesce(columns, DEFAULT_COLUMNS) + "`}")
                // Condition clause
                .combine(         pm.filterProperty(),   filter -> "{where: `" + Objects.coalesce(filter, DEFAULT_FILTER) + "`}")
                .combineIfNotNull(pm.organizationIdProperty(),
                        organisationId -> "{where:  `event.organization=" + organisationId + "`}")
                .combineIfNotNull(pm.eventIdProperty(), eventId -> "{where:  `event=" + eventId + "`}")
                .combineIfNotNull(pm.dayProperty(),         day -> "{where:  `exists(select Attendance where documentLine.document=d and date="  + toSqlDate(day) + ")`}")
                .combineIfTrue(   pm.arrivalsProperty(),     () -> "{where: `!exists(select Attendance where documentLine.document=d and date="  + toSqlDate(pm.getDay().minusDays(1)) + ")`}")
                .combineIfTrue(   pm.departuresProperty(),   () -> "{where: `!exists(select Attendance where documentLine.document=d and date="  + toSqlDate(pm.getDay().plusDays(1)) + ")`}")
                .combineIfNotNull(pm.minDayProperty(),   minDay -> "{where:  `exists(select Attendance where documentLine.document=d and date>=" + toSqlDate(minDay) + ")`}")
                .combineIfNotNull(pm.maxDayProperty(),   maxDay -> "{where:  `exists(select Attendance where documentLine.document=d and date<=" + toSqlDate(maxDay) + ")`}")
                // Search box condition
                .combineIfNotEmptyTrim(pm.searchTextProperty(), s ->
                        Character.isDigit(s.charAt(0)) ? "{where: `ref = " + s + "`}"
                                : s.contains("@") ? "{where: `lower(person_email) like '%" + s.toLowerCase() + "%'`}"
                                : "{where: `person_abcNames like '" + AbcNames.evaluate(s, true) + "'`}")
                // Group by clause
                .combineIfNotNull(pm.groupByProperty(), groupBy -> "{groupBy: `" + groupBy + "`}")
                // Order by clause
                .combine(pm.orderByProperty(), orderBy -> "{orderBy: `" + Objects.coalesce(orderBy, DEFAULT_ORDER_BY) + "`}")
                // Limit clause
                .combineIfPositive(pm.limitProperty(), l -> "{limit: `" + l + "`}")
                .applyDomainModelRowStyle()
                .displayResultInto(pm.genericDisplayResultProperty())
                .setSelectedEntityHandler(pm.genericDisplaySelectionProperty(), document -> selectedDocumentProperty.set(document))
                .start();
    }

    @Override
    protected void refreshDataOnActive() {
        filter.refreshWhenActive();
    }

}
