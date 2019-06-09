package mongoose.backend.activities.bookings;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import mongoose.client.controls.personaldetails.PersonalDetailsPanel;
import mongoose.shared.entities.Document;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.client.ui.filter.ReactiveExpressionFilterFactoryMixin;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.util.ImageStore;
import webfx.fxkit.util.properties.Properties;
import webfx.platform.shared.util.Strings;

final class BookingDetailsPanel implements ReactiveExpressionFilterFactoryMixin {

    static final String REQUIRED_FIELDS_STRING_FILTER = "{fields: 'person_firstName,person_lastName,person_age,person_email,person_organization,person_phone,person_cityName,person_country,person_carer1Name,person_carer2Name'}";

    private final ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();
    private final BooleanProperty activeProperty = new SimpleBooleanProperty(true);

    private final GridPane gridPane = new GridPane();
    private final Pane parent;
    private final ButtonFactoryMixin buttonFactoryMixin;
    private final DataSourceModel dataSourceModel;

    BookingDetailsPanel(Pane parent, ButtonFactoryMixin buttonFactoryMixin, DataSourceModel dataSourceModel) {
        this.parent = parent;
        this.buttonFactoryMixin = buttonFactoryMixin;
        this.dataSourceModel = dataSourceModel;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public BooleanProperty activeProperty() {
        return activeProperty;
    }

    public ObjectProperty<Document> selectedDocumentProperty() {
        return selectedDocumentProperty;
    }

    public Document getSelectedDocument() {
        return selectedDocumentProperty.get();
    }

    public void setSelectedDocument(Document document) {
        selectedDocumentProperty.set(document);
    }

    Node buildUi() {
        return new VBox(/*button, */new TabPane(
                createTab("Personal details", "images/s16/personalDetails.png", buildPersonalDetailsView()),
                createFilterTab("Options", "images/s16/options.png", "{class: 'DocumentLine', columns: `site,item,dates,lockAllocation,resourceConfiguration,comment,price_isCustom,price_net,price_nonRefundable,price_minDeposit,price_deposit`, where: 'document=${selectedDocument}', orderBy: 'item.family.ord,site..ord,item.ord'}"),
                createFilterTab("Payments", "images/s16/methods/generic.png", "{class: 'MoneyTransfer', columns: `date,method,transactionRef,comment,amount,verified`, where: 'document=${selectedDocument}', orderBy: 'date,id'}"),
                createTab("Comments", "images/s16/note.png", buildCommentView()),
                createFilterTab("Cart", "images/s16/cart.png", "{class: 'Document', columns:`ref,multipleBookingIcon,langIcon,genderIcon,person_firstName,person_lastName,person_age,noteIcon,price_net,price_deposit,price_balance`, where: 'cart=(select cart from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("Multiple bookings", "images/s16/multipleBookings/redCross.png", "{class: 'Document', columns:`ref,multipleBookingIcon,langIcon,genderIcon,person_firstName,person_lastName,person_age,noteIcon,price_deposit,plainOptions`, where: 'multipleBooking=(select multipleBooking from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("Children", "images/s16/child.png", "{class: 'Document', columns:`ref,multipleBookingIcon,langIcon,genderIcon,person_firstName,person_lastName,person_age,noteIcon,price_deposit,plainOptions`, where: 'person_carer1Document=${selectedDocument} or person_carer2Document=${selectedDocument}', orderBy: 'ref'}"),
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
        Tab tab = createTab(text, iconUrl, table);
        // The following is required only for gwt version for any reason (otherwise the table height is not resize when growing)
        Properties.runOnPropertiesChange(() -> {
            TabPane tabPane = tab.getTabPane();
            if (tabPane != null)
                tabPane.requestLayout();
        }, table.displayResultProperty());
        // Setting up the reactive filter
        String classOnly = filter.substring(0, filter.indexOf(',')) + "}";
        createReactiveExpressionFilter(classOnly)
                .bindActivePropertyTo(tab.selectedProperty())
                .combineIfNotNullOtherwiseForceEmptyResult(selectedDocumentProperty, document -> Strings.replaceAll(filter, "${selectedDocument}", document.getPrimaryKey()))
                .applyDomainModelRowStyle()
                .displayResultInto(table.displayResultProperty())
                .setPush(true)
                .start();
        return tab;
    }

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
        PersonalDetailsPanel details = new PersonalDetailsPanel(document.getEvent(), buttonFactoryMixin, parent);
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
        DialogUtil.showModalNodeInGoldLayout(dialogContent, parent, 0, 0.9);
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
}

