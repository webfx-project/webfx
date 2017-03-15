package mongoose.activities.backend.book.event.options;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import mongoose.actions.MongooseActions;
import mongoose.activities.backend.book.event.shared.EditableBookingCalendar;
import mongoose.activities.backend.util.MultiLanguageEditor;
import mongoose.activities.shared.book.event.options.OptionsViewActivity;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.entities.Label;
import mongoose.entities.Option;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.ui.controls.DialogCallback;
import naga.framework.ui.controls.DialogContent;
import naga.framework.ui.controls.DialogUtil;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;
import naga.fxdata.control.DataGrid;
import naga.platform.services.update.UpdateArgument;
import naga.platform.spi.Platform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static naga.framework.ui.controls.LayoutUtil.*;

/**
 * @author Bruno Salmon
 */
public class EditableOptionsViewActivity extends OptionsViewActivity {

    private ObservableValue<Boolean> editModeProperty;

    @Override
    protected void createViewNodes() {
        I18n i18n = getI18n();
        CheckBox editModeCheckBox = i18n.translateText(new CheckBox(), "EditMode");
        editModeProperty = editModeCheckBox.selectedProperty();
        Properties.runOnPropertiesChange(p -> ((EditableBookingCalendar) bookingCalendar).setEditMode(editModeProperty.getValue()), editModeProperty);
        Button addOptionButton = newButton(MongooseActions.newAddOptionAction(this::showAddOptionDialog));
        addOptionButton.visibleProperty().bind(editModeProperty);
        super.createViewNodes();
        HBox hbox = new HBox(20, addOptionButton, createHGrowable(), editModeCheckBox, createHGrowable(), priceText);
        hbox.setPadding(new Insets(5, 10, 0, 10));
        HBox.setMargin(editModeCheckBox, new Insets(5, 0, 5, 0));
        hbox.setAlignment(Pos.CENTER);
        borderPane.setTop(hbox);
    }

    @Override
    protected void addPriceText() {
        // Already included in the hBox
    }

    @Override
    protected BookingCalendar createBookingCalendar() {
        return new EditableBookingCalendar(true, getI18n(), borderPane);
    }

    @Override
    protected List<Node> createOptionPanelHeaderNodes(Option option, Property<String> i18nTitle) {
        List<Node> list = new ArrayList<>(super.createOptionPanelHeaderNodes(option, i18nTitle));
        Collections.addAll(list, createHGrowable(), setUnmanagedWhenInvisible(newRemoveButton(() -> showRemoveOptionDialog(option)), editModeProperty));
        return list;
    }

    private void showRemoveOptionDialog(Option option) {
        DialogUtil.showDialog(
                DialogContent.createConfirmationDialog(
                        "Removing an option",
                        "Do you really want to remove this option?",
                        getI18n()),
                dialogCallback -> {
                    // Creating an update store
                    UpdateStore store = UpdateStore.create(getEventDataSourceModel());
                    // Creating an instance of Option entity
                    store.deleteEntity(option);
                    // Asking the update record this change in the database
                    store.executeUpdate().setHandler(asyncResult -> {
                        if (asyncResult.failed())
                            dialogCallback.showException(asyncResult.cause());
                        else {
                            dialogCallback.closeDialog();
                            // Updating the UI
                            getWorkingDocument().getWorkingDocumentLines().removeIf(line -> getTopParentOption(line.getOption()) == option);
                            getSelectedOptionsPreselection().getOptionPreselections().removeIf(optionPreselection -> getTopParentOption(optionPreselection.getOption()) == option);
                            clearEventOptions();
                            startLogic();
                        }
                    });
                }, borderPane);
    }

    private BorderPane addOptionDialogPane;
    private DialogCallback addOptionDialogCallback;
    private ReactiveExpressionFilter addOptionDialogFilter;

    private void showAddOptionDialog() {
        if (addOptionDialogPane == null) {
            DataGrid dataGrid = new DataGrid();
            addOptionDialogPane = new BorderPane(setPrefSizeToInfinite(dataGrid));
            addOptionDialogFilter = new ReactiveExpressionFilter("{class: 'Option', alias: 'o', where: 'parent=null and template', orderBy: 'event.id desc,ord'}").setDataSourceModel(getDataSourceModel()).setI18n(getI18n())
                    .combine(eventIdProperty(), e -> "{where: 'event.organization=" + getEvent().getOrganization().getPrimaryKey() + "'}")
                    .setExpressionColumns("[" +
                            "{label: 'Option', expression: 'coalesce(itemFamily.icon,item.family.icon),coalesce(name, item.name)'}," +
                            "{label: 'Event', expression: 'event.(icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate))'}," +
                            "{label: 'Event type', expression: 'event.type'}" +
                            "]")
                    .displayResultSetInto(dataGrid.displayResultSetProperty())
                    .setDisplaySelectionProperty(dataGrid.displaySelectionProperty())
                    //.setSelectedEntityHandler(dataGrid.displaySelectionProperty(), o -> closeAddOptionDialog(true))
                    .start();
            HBox hBox = new HBox(20, createHGrowable(), newOkButton(this::onOkAddOptionDialog), newCancelButton(this::onCancelAddOptionDialog), createHGrowable());
            hBox.setPadding(new Insets(20, 0, 0, 0));
            addOptionDialogPane.setBottom(hBox);
            dataGrid.setOnMouseClicked(e -> {if (e.getClickCount() == 2) closeAddOptionDialog(); });
        }
        addOptionDialogFilter.setActive(true);
        addOptionDialogCallback = DialogUtil.showModalNodeInGoldLayout(addOptionDialogPane, borderPane, 0.9, 0.8);
    }

    private void onOkAddOptionDialog() {
        Option selectedOption = (Option) addOptionDialogFilter.getSelectedEntity();
        if (selectedOption != null) {
            Platform.getUpdateService().executeUpdate(new UpdateArgument("select copy_option(null,?::int,?::int,null)", new Object[]{selectedOption.getPrimaryKey(), getEventId()}, true, getDataSourceModel().getId())).setHandler(ar -> {
                if (ar.failed())
                    addOptionDialogCallback.showException(ar.cause());
                else {
                    closeAddOptionDialog();
                    OptionsPreselection selectedOptionsPreselection = getSelectedOptionsPreselection();
                    clearEventOptions();
                    onFeesGroup().setHandler(ar2 -> {
                        if (ar2.succeeded()) {
                            for (FeesGroup feesGroup : ar2.result()) {
                                for (OptionsPreselection optionsPreselection : feesGroup.getOptionsPreselections()) {
                                    if (optionsPreselection.getLabel() == selectedOptionsPreselection.getLabel()) {
                                        setSelectedOptionsPreselection(optionsPreselection);
                                        setWorkingDocument(optionsPreselection.getWorkingDocument());
                                        startLogic();
                                        return;
                                    }
                                }
                            }
                        }
                    });
                }
            });
        }
        closeAddOptionDialog();
    }

    private void onCancelAddOptionDialog() {
        closeAddOptionDialog();
    }

    private void closeAddOptionDialog() {
        addOptionDialogCallback.closeDialog();
        addOptionDialogFilter.setActive(false);
    }

    private Option getTopParentOption(Option option) {
        Option parent = option == null ? null : option.getParent();
        return parent == null ? option : getTopParentOption(parent);
    }

    @Override
    protected Node createLabelNode(Label label) {
        Node labelNode = super.createLabelNode(label);
        labelNode.setOnMouseClicked(e -> showLabelDialog(label));
        return labelNode;
    }

    private DialogCallback labelDialogCallback;

    private void showLabelDialog(Label label) {
        if (labelDialogCallback == null)
            labelDialogCallback = DialogUtil.showModalNodeInGoldLayout(
                    new MultiLanguageEditor(getI18n(), label, lang -> lang, null)
                            .showOkCancelButton(e -> closeLabelDialog(e, label))
                            .getUiNode(), borderPane, 0.9, 0.8);
    }

    private void closeLabelDialog(Entity savedEntity, Label label) {
        labelDialogCallback.closeDialog();
        labelDialogCallback = null;
        if (savedEntity != null) {
            label.getStore().copyEntity(savedEntity);
            createOrUpdateOptionPanelsIfReady(true);
        }
    }
}
