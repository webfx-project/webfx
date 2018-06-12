package mongoose.activities.backend.book.options;

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
import mongoose.activities.backend.book.shared.EditableBookingCalendar;
import mongoose.activities.backend.util.MultiLanguageEditor;
import mongoose.activities.bothends.book.options.OptionsActivity;
import mongoose.activities.bothends.book.shared.BookingCalendar;
import mongoose.activities.bothends.book.shared.FeesGroup;
import mongoose.activities.bothends.logic.preselection.OptionsPreselection;
import mongoose.entities.Label;
import mongoose.entities.Option;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.ui.graphic.controls.dialog.DialogCallback;
import naga.framework.ui.graphic.controls.dialog.DialogContent;
import naga.framework.ui.graphic.controls.dialog.DialogUtil;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.fx.properties.Properties;
import naga.fxdata.control.DataGrid;
import naga.platform.services.update.UpdateArgument;
import naga.platform.services.update.UpdateService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static naga.framework.ui.layouts.LayoutUtil.*;

/**
 * @author Bruno Salmon
 */
class EditableOptionsActivity extends OptionsActivity {

    private ObservableValue<Boolean> editModeProperty;

    @Override
    protected void createViewNodes() {
        CheckBox editModeCheckBox = newCheckBox( "EditMode");
        editModeProperty = editModeCheckBox.selectedProperty();
        Properties.runOnPropertiesChange(() -> ((EditableBookingCalendar) bookingCalendar).setEditMode(editModeProperty.getValue()), editModeProperty);
        Button addOptionButton = newButton(MongooseActions.newAddOptionAction(this::showAddOptionDialog));
        addOptionButton.visibleProperty().bind(editModeProperty);
        super.createViewNodes();
        HBox hbox = new HBox(20, addOptionButton, createHGrowable(), editModeCheckBox, createHGrowable(), priceText);
        hbox.setPadding(new Insets(5, 10, 0, 10));
        HBox.setMargin(editModeCheckBox, new Insets(5, 0, 5, 0));
        hbox.setAlignment(Pos.CENTER);
        pageContainer.setTop(hbox);
    }

    @Override
    protected void addPriceText() {
        // Already included in the hBox
    }

    @Override
    protected BookingCalendar createBookingCalendar() {
        return new EditableBookingCalendar(true, pageContainer);
    }

    @Override
    protected List<Node> createOptionPanelHeaderNodes(Option option, Property<String> i18nTitle) {
        List<Node> list = new ArrayList<>(super.createOptionPanelHeaderNodes(option, i18nTitle));
        Collections.addAll(list, createHGrowable(), setUnmanagedWhenInvisible(newRemoveButton(() -> showRemoveOptionDialog(option)), editModeProperty));
        return list;
    }

    @Override
    protected Node createLabelNode(Label label) {
        Node labelNode = super.createLabelNode(label);
        labelNode.setOnMouseClicked(e -> {
            if (editModeProperty.getValue())
              showLabelDialog(label);
        });
        return labelNode;
    }

    private void showRemoveOptionDialog(Option option) {
        DialogUtil.showDialog(
                DialogContent.createConfirmationDialog(
                        "Removing an option",
                        "Do you really want to remove this option?"),
                dialogCallback -> {
                    // Creating an update store
                    UpdateStore store = UpdateStore.create(getDataSourceModel());
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
                }, pageContainer);
    }

    private BorderPane addOptionDialogPane;
    private DialogCallback addOptionDialogCallback;
    private ReactiveExpressionFilter<Option> addOptionDialogFilter;

    private void showAddOptionDialog() {
        if (addOptionDialogPane == null) {
            DataGrid dataGrid = new DataGrid();
            addOptionDialogPane = new BorderPane(setMaxPrefSizeToInfinite(dataGrid));
            addOptionDialogFilter = new ReactiveExpressionFilter<Option>("{class: 'Option', alias: 'o', where: 'parent=null and template', orderBy: 'event.id desc,ord'}").setDataSourceModel(getDataSourceModel())
                    .combine(eventIdProperty(), e -> "{where: 'event.organization=" + getEvent().getOrganization().getPrimaryKey() + "'}")
                    .setExpressionColumns("[" +
                            "{label: 'Option', expression: 'coalesce(itemFamily.icon,item.family.icon),coalesce(name, item.name)'}," +
                            "{label: 'Event', expression: 'event.(icon, name + ` ~ ` + dateIntervalFormat(startDate,endDate))'}," +
                            "{label: 'Event type', expression: 'event.type'}" +
                            "]")
                    .displayResultInto(dataGrid.displayResultProperty())
                    .setDisplaySelectionProperty(dataGrid.displaySelectionProperty())
                    //.setSelectedEntityHandler(dataGrid.displaySelectionProperty(), o -> closeAddOptionDialog(true))
                    .start();
            HBox hBox = new HBox(20, createHGrowable(), newOkButton(this::onOkAddOptionDialog), newCancelButton(this::onCancelAddOptionDialog), createHGrowable());
            hBox.setPadding(new Insets(20, 0, 0, 0));
            addOptionDialogPane.setBottom(hBox);
            dataGrid.setOnMouseClicked(e -> {if (e.getClickCount() == 2) closeAddOptionDialog(); });
        }
        addOptionDialogFilter.setActive(true);
        addOptionDialogCallback = DialogUtil.showModalNodeInGoldLayout(addOptionDialogPane, pageContainer, 0.9, 0.8);
    }

    private void onOkAddOptionDialog() {
        Option selectedOption = addOptionDialogFilter.getSelectedEntity();
        if (selectedOption != null) {
            UpdateService.executeUpdate(new UpdateArgument("select copy_option(null,?::int,?::int,null)", new Object[]{selectedOption.getPrimaryKey(), getEventId()}, true, getDataSourceId())).setHandler(ar -> {
                if (ar.failed())
                    addOptionDialogCallback.showException(ar.cause());
                else {
                    closeAddOptionDialog();
                    OptionsPreselection selectedOptionsPreselection = getSelectedOptionsPreselection();
                    clearEventOptions();
                    onFeesGroups().setHandler(ar2 -> {
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

    private DialogCallback labelDialogCallback;

    void showLabelDialog(Label label) {
        if (labelDialogCallback == null)
            labelDialogCallback = DialogUtil.showModalNodeInGoldLayout(
                    new MultiLanguageEditor(this, label, lang -> lang, null)
                            .showOkCancelButton(e -> closeLabelDialog(e, label))
                            .getUiNode(), pageContainer, 0.9, 0.8);
    }

    private void closeLabelDialog(Entity savedEntity, Label label) {
        labelDialogCallback.closeDialog();
        labelDialogCallback = null;
        if (savedEntity != null) {
            label.getStore().copyEntity(savedEntity);
            updateLabelText(label);
        }
    }
}