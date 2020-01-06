package mongoose.backend.controls.bookingdetailspanel;

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
import mongoose.backend.controls.masterslave.UiBuilder;
import mongoose.backend.operations.entities.document.EditDocumentPersonalDetailsRequest;
import mongoose.backend.operations.entities.document.cart.OpenBookingCartRequest;
import mongoose.backend.operations.entities.document.multiplebookings.CancelOtherMultipleBookingsRequest;
import mongoose.backend.operations.entities.document.multiplebookings.GetBackCancelledMultipleBookingsDepositRequest;
import mongoose.backend.operations.entities.document.multiplebookings.MergeMultipleBookingsOptionsRequest;
import mongoose.backend.operations.entities.document.multiplebookings.ToggleMarkMultipleBookingRequest;
import mongoose.backend.operations.entities.documentline.AddNewDocumentLineRequest;
import mongoose.backend.operations.entities.documentline.DeleteDocumentLineRequest;
import mongoose.backend.operations.entities.documentline.EditDocumentLineRequest;
import mongoose.backend.operations.entities.documentline.ToggleCancelDocumentLineRequest;
import mongoose.backend.operations.entities.generic.CopyAllRequest;
import mongoose.backend.operations.entities.generic.CopySelectionRequest;
import mongoose.backend.operations.entities.mail.ComposeNewMailRequest;
import mongoose.backend.operations.entities.mail.OpenMailRequest;
import mongoose.backend.operations.entities.moneytransfer.AddNewPaymentRequest;
import mongoose.backend.operations.entities.moneytransfer.AddNewTransferRequest;
import mongoose.backend.operations.entities.moneytransfer.DeletePaymentRequest;
import mongoose.backend.operations.entities.moneytransfer.EditPaymentRequest;
import mongoose.client.presentationmodel.HasSelectedDocumentProperty;
import mongoose.shared.entities.Document;
import webfx.extras.imagestore.ImageStore;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.activity.impl.elementals.activeproperty.HasActiveProperty;
import webfx.framework.client.ui.action.operation.OperationActionFactoryMixin;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.services.i18n.I18nControls;
import webfx.framework.client.ui.action.ActionGroup;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.HasDataSourceModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.util.Strings;

import java.util.function.Supplier;

public final class BookingDetailsPanel implements
        OperationActionFactoryMixin,
        HasActiveProperty,
        UiBuilder {

    public static final String REQUIRED_FIELDS = "person_firstName,person_lastName,person_age,person_email,person_organization,person_phone,person_cityName,person_country,person_carer1Name,person_carer2Name,event.startDate"; // event.startDate is required for the personal details panel

    private final ObjectProperty<Document> selectedDocumentProperty = new SimpleObjectProperty<>();
    private final BooleanProperty activeProperty = new SimpleBooleanProperty(true);

    private final Supplier<Pane> parentGetter;
    private final GridPane gridPane = new GridPane();
    private final ButtonFactoryMixin mixin;
    private final DataSourceModel dataSourceModel;

    public BookingDetailsPanel(Supplier<Pane> parentGetter, ButtonFactoryMixin mixin, DataSourceModel dataSourceModel) {
        this.parentGetter = parentGetter;
        this.mixin = mixin;
        this.dataSourceModel = dataSourceModel;
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

    @Override
    public Node buildUi() {
        return new VBox(/*button, */new TabPane(
                createTab("PersonalDetails", buildPersonalDetailsView()),
                createFilterTab("Options", "{class: 'DocumentLine', columns: `site,item,dates,lockAllocation,resourceConfiguration,comment,price_isCustom,price_net,price_nonRefundable,price_minDeposit,price_deposit`, where: 'document=${selectedDocument}', orderBy: 'item.family.ord,site..ord,item.ord'}"),
                createFilterTab("Payments", "{class: 'MoneyTransfer', columns: `date,method,transactionRef,comment,amount,verified`, where: 'document=${selectedDocument}', orderBy: 'date,id'}"),
                createTab("Comments", buildCommentView()),
                createFilterTab("Cart", "{class: 'Document', columns:`ref,multipleBookingIcon,langIcon,genderIcon,person_firstName,person_lastName,person_age,noteIcon,price_net,price_deposit,price_balance`, where: 'cart=(select cart from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("MultipleBookings", "{class: 'Document', columns:`ref,multipleBookingIcon,langIcon,genderIcon,person_firstName,person_lastName,person_age,noteIcon,price_deposit,plainOptions`, where: 'multipleBooking=(select multipleBooking from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("Family", "{class: 'Document', columns:`ref,multipleBookingIcon,langIcon,genderIcon,person_firstName,person_lastName,person_age,noteIcon,price_deposit,plainOptions`, where: 'person_carer1Document=${selectedDocument} or person_carer2Document=${selectedDocument} or id=(select person_carer1Document from Document where id=${selectedDocument}) or id=(select person_carer2Document from Document where id=${selectedDocument})', orderBy: 'ref'}"),
                createFilterTab("Mails", "{class: 'Mail', columns: 'date,subject,transmitted,error', where: 'document=${selectedDocument}', orderBy: 'date desc'}"),
                createFilterTab("History", "{class: 'History', columns: 'date,username,comment,request', where: 'document=${selectedDocument}', orderBy: 'date desc'}")
        ));
    }

    private static Tab createTab(String i18nKey, Node node) {
        Tab tab = I18nControls.bindI18nProperties(new Tab(), i18nKey);
        tab.setContent(node);
        tab.setClosable(false);
        return tab;
    }

    private Tab createFilterTab(String i18nKey, String dqlStatementString) {
        VisualGrid table = new VisualGrid();
        Tab tab = createTab(i18nKey, table);
        // The following is required only for gwt version for any reason (otherwise the table height is not resized when growing)
        Properties.runOnPropertiesChange(() -> {
            TabPane tabPane = tab.getTabPane();
            if (tabPane != null)
                tabPane.requestLayout();
        }, table.visualResultProperty());
        // Setting up the reactive visual mapper
        String classOnly = dqlStatementString.substring(0, dqlStatementString.indexOf(',')) + "}";
        ObjectProperty<Entity> selectedEntityProperty = new SimpleObjectProperty<>();
        ReactiveVisualMapper<Entity> visualMapper = ReactiveVisualMapper.createPushReactiveChain()
                .always(classOnly)
                .ifNotNullOtherwiseEmptyString(selectedDocumentProperty, document -> Strings.replaceAll(dqlStatementString, "${selectedDocument}", document.getPrimaryKey()))
                .bindActivePropertyTo(tab.selectedProperty())
                .setDataSourceModel(dataSourceModel)
                .applyDomainModelRowStyle()
                .visualizeResultInto(table)
                .setSelectedEntityHandler(selectedEntityProperty::set)
                .start();

        Supplier<ActionGroup> contextMenuActionGroupFactory = null;
        switch (i18nKey) {
            case "Options":
                contextMenuActionGroupFactory = () -> newActionGroup(
                        newOperationAction(() -> new AddNewDocumentLineRequest(getSelectedDocument(), parentGetter.get())),
                        newSeparatorActionGroup(
                                newOperationAction(() -> new EditDocumentLineRequest(get(selectedEntityProperty), parentGetter.get())),
                                newOperationAction(() -> new ToggleCancelDocumentLineRequest(get(selectedEntityProperty), parentGetter.get()), selectedEntityProperty),
                                newOperationAction(() -> new DeleteDocumentLineRequest(get(selectedEntityProperty), parentGetter.get()))
                        ),
                        newSeparatorActionGroup(
                                newOperationAction(() -> new CopySelectionRequest(visualMapper.getSelectedEntities(), visualMapper.getEntityColumns())),
                                newOperationAction(() -> new CopyAllRequest(visualMapper.getCurrentEntities(), visualMapper.getEntityColumns()))
                        )
                );
                break;
            case "Payments":
                contextMenuActionGroupFactory = () -> newActionGroup(
                        newOperationAction(() -> new AddNewPaymentRequest(getSelectedDocument(), parentGetter.get())),
                        newOperationAction(() -> new AddNewTransferRequest(getSelectedDocument(), parentGetter.get())),
                        newSeparatorActionGroup(
                                newOperationAction(() -> new EditPaymentRequest(get(selectedEntityProperty), parentGetter.get())),
                                newOperationAction(() -> new DeletePaymentRequest(get(selectedEntityProperty), parentGetter.get()))
                        ),
                        newSeparatorActionGroup(
                                newOperationAction(() -> new CopySelectionRequest(visualMapper.getSelectedEntities(), visualMapper.getEntityColumns())),
                                newOperationAction(() -> new CopyAllRequest(visualMapper.getCurrentEntities(), visualMapper.getEntityColumns()))
                        )
                );
                break;
            case "MultipleBookings":
                contextMenuActionGroupFactory = () -> newActionGroup(
                        newOperationAction(() -> new MergeMultipleBookingsOptionsRequest(get(selectedEntityProperty), parentGetter.get())),
                        newOperationAction(() -> new CancelOtherMultipleBookingsRequest(get(selectedEntityProperty), parentGetter.get())),
                        newOperationAction(() -> new GetBackCancelledMultipleBookingsDepositRequest(get(selectedEntityProperty), parentGetter.get())),
                        newOperationAction(() -> new ToggleMarkMultipleBookingRequest(get(selectedEntityProperty), parentGetter.get()))
                );
                break;
            case "Cart":
                contextMenuActionGroupFactory = () -> newActionGroup(
                        newOperationAction(() -> new OpenBookingCartRequest(get(selectedEntityProperty), parentGetter.get()))
                );
                break;
            case "Mails":
                contextMenuActionGroupFactory = () -> newActionGroup(
                        newOperationAction(() -> new OpenMailRequest(get(selectedEntityProperty), parentGetter.get())),
                        newOperationAction(() -> new ComposeNewMailRequest(getSelectedDocument(), parentGetter.get())),
                        newSeparatorActionGroup(
                                newOperationAction(() -> new CopySelectionRequest(visualMapper.getSelectedEntities(), visualMapper.getEntityColumns())),
                                newOperationAction(() -> new CopyAllRequest(visualMapper.getCurrentEntities(), visualMapper.getEntityColumns()))
                        )
                );
                break;
            case "History":
                contextMenuActionGroupFactory = () -> newActionGroup(
                        newOperationAction(() -> new CopySelectionRequest(visualMapper.getSelectedEntities(), visualMapper.getEntityColumns())),
                        newOperationAction(() -> new CopyAllRequest(visualMapper.getCurrentEntities(), visualMapper.getEntityColumns()))
                );
                break;
        }
        if (contextMenuActionGroupFactory != null)
            mixin.setUpContextMenu(table, contextMenuActionGroupFactory);
        return tab;
    }

    private static <E extends Entity> E get(ObjectProperty<Entity> selectedEntityProperty) {
        return (E) selectedEntityProperty.get();
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

        mixin.setUpContextMenu(gridPane, () -> newActionGroup(
                newOperationAction(() -> new EditDocumentPersonalDetailsRequest(getSelectedDocument(), mixin, parentGetter.get()))
        ));
        gridPane.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2)
                executeOperation(new EditDocumentPersonalDetailsRequest(getSelectedDocument(), mixin, parentGetter.get()));
        });

        return gridPane;
    }

    private void addFieldLabelAndValue(int rowIndex, int columnIndex, int columnSpan, String fieldName) {
        webfx.extras.label.Label fieldLabel = dataSourceModel.getDomainModel().getClass("Document").getField(fieldName).getLabel();
        ObservableValue<String> fieldValueProperty = Properties.compute(selectedDocumentProperty, document -> {
            Object fieldValue = document == null ? null : document.getFieldValue(fieldName);
            if (fieldValue instanceof EntityId)
                fieldValue = document.getForeignEntity(fieldName).getFieldValue("name");
            return fieldValue == null ? null : fieldValue.toString();
        });
        Label valueLabel = new Label();
        valueLabel.textProperty().bind(fieldValueProperty);
        addNodeToGrid(rowIndex, columnIndex, 1, I18nControls.bindI18nProperties(new Label(null, ImageStore.createImageView(fieldLabel.getIconPath())), fieldLabel.getCode()));
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

    /*==================================================================================================================
    ============================================== Static factory methods ==============================================
    ==================================================================================================================*/

    public static <M extends ButtonFactoryMixin & HasDataSourceModel> BookingDetailsPanel createAndBind(HasSelectedDocumentProperty pm, M mixin, Supplier<Pane> containerGetter) {
        return createAndBind(pm, mixin, containerGetter, mixin.getDataSourceModel());
    }

    public static BookingDetailsPanel createAndBind(HasSelectedDocumentProperty pm, ButtonFactoryMixin mixin, Supplier<Pane> containerGetter, DataSourceModel dataSourceModel) {
        BookingDetailsPanel bookingDetailsPanel = new BookingDetailsPanel(containerGetter, mixin, dataSourceModel);
        bookingDetailsPanel.selectedDocumentProperty().bind(pm.selectedDocumentProperty());
        if (mixin instanceof HasActiveProperty)
            bookingDetailsPanel.activeProperty().bind(((HasActiveProperty) mixin).activeProperty());
        return bookingDetailsPanel;
    }


    public static BookingDetailsPanel createAndBindIfApplicable(Object pm, Object mixin, Supplier<Pane> containerGetter) {
        if (pm instanceof HasSelectedDocumentProperty && mixin instanceof ButtonFactoryMixin && mixin instanceof HasDataSourceModel)
            return createAndBind((HasSelectedDocumentProperty) pm, (ButtonFactoryMixin & HasDataSourceModel) mixin, containerGetter);
        return null;
    }
}

