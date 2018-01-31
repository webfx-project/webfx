package naga.framework.ui.graphic.controls.dialog;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import naga.util.collection.Collections;
import naga.util.function.Consumer;
import naga.util.tuples.Pair;
import naga.framework.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static naga.framework.ui.layouts.LayoutUtil.createHGrowable;

/**
 * @author Bruno Salmon
 */
public class GridPaneBuilder implements DialogBuilder {

    private final GridPane gridPane = new GridPane();
    private final Font font = Font.getDefault();
    private final I18n i18n;
    private int rowCount;
    private int colCount;
    private List<Pair<Property, Object>> watchedUserProperties = new ArrayList<>();
    private Property<Boolean> noChangesProperty = new SimpleObjectProperty<>(true);
    private final ChangeListener watchedUserPropertyListener = (observable, oldValue, newValue) ->
            noChangesProperty.setValue(Collections.hasNoOneMatching(watchedUserProperties, pair -> !Objects.equals(pair.get1().getValue(), pair.get2())));
    private DialogCallback dialogCallback;

    public GridPaneBuilder(I18n i18n) {
        this.i18n = i18n;
        gridPane.setHgap(10);
        gridPane.setVgap(10);
    }

    @Override
    public I18n getI18n() {
        return i18n;
    }

    public void setDialogCallback(DialogCallback dialogCallback) {
        this.dialogCallback = dialogCallback;
    }

    @Override
    public DialogCallback getDialogCallback() {
        return dialogCallback;
    }

    public GridPaneBuilder addLabelTextInputRow(String labelKey, TextInputControl textInput) {
        return addNewRow(createLabel(labelKey), setUpTextInput(textInput));
    }

    public GridPaneBuilder addLabelNodeRow(String labelKey, Node node) {
        GridPane.setHgrow(node, Priority.ALWAYS);
        return addNewRow(createLabel(labelKey), node);
    }

    public GridPaneBuilder addCheckBoxTextInputRow(String labelKey, CheckBox checkBox, TextInputControl textInput) {
        textInput.visibleProperty().bind(checkBox.selectedProperty());
        return addNewRow(setUpLabeled(checkBox, labelKey), setUpTextInput(textInput));
    }

    public GridPaneBuilder addNewRow(Node... children) {
        colCount = Math.max(colCount, children.length);
        gridPane.addRow(rowCount++, children);
        if (colCount >= 2 && gridPane.getColumnConstraints().isEmpty()) {
            ColumnConstraints cc1 = new ColumnConstraints();
            cc1.setHgrow(Priority.NEVER);
            ColumnConstraints cc2 = new ColumnConstraints();
            cc2.setHgrow(Priority.ALWAYS);
            gridPane.getColumnConstraints().setAll(cc1, cc2);
        }
        return this;
    }

    public GridPaneBuilder addTextInputRow(TextInputControl textInput) {
        return addNodeFillingRow(setUpTextInput(textInput));
    }

    public GridPaneBuilder addTextRow(String text) {
        return addNodeFillingRow(setUpLabeled(new Label(), text));
    }

    public GridPaneBuilder addNodeFillingRow(Node node) {
        return addNodeFillingRow(0, node);
    }

    public GridPaneBuilder addNodeFillingRowAndHeight(Node node) {
        GridPane.setVgrow(node, Priority.ALWAYS);
        return addNodeFillingRow(0, node);
    }

    public GridPaneBuilder addNodeFillingRow(int topMargin, Node node) {
        return addNodeFillingRow(topMargin, node, Math.max(colCount, 1));
    }

    public GridPaneBuilder addNodeFillingRow(Node node, int colSpan) {
        return addNodeFillingRow(0, node, colSpan);
    }

    public GridPaneBuilder addNodeFillingRow(int topMargin, Node node, int colSpan) {
        if (topMargin != 0)
            GridPane.setMargin(node, new Insets(topMargin, 0, 0, 0));
        gridPane.add(node, 0, rowCount++, colSpan, 1);
        GridPane.setHgrow(node, Priority.ALWAYS);
        return this;
    }

    public GridPaneBuilder addButton(String buttonKey, Button button) {
        i18n.translateText(button, buttonKey).setFont(font);
        GridPane.setHalignment(button, HPos.RIGHT);
        gridPane.add(button, 0, rowCount++, colCount, 1);
        return this;
    }

    public GridPaneBuilder addButtons(String button1Key, Consumer<DialogCallback> action1, String button2Key, Consumer<DialogCallback> action2) {
        return addNodeFillingRow(20, createButtonBar(button1Key, action1, button2Key, action2));
    }

    public GridPaneBuilder addButtons(String button1Key, Button button1, String button2Key, Button button2) {
        return addNodeFillingRow(20, createButtonBar(button1Key, button1, button2Key, button2));
    }

    public GridPaneBuilder addButtons(Button... buttons) {
        return addNodeFillingRow(20, createButtonBar(buttons));
    }

    private HBox createButtonBar(String button1Key, Consumer<DialogCallback> action1, String button2Key, Consumer<DialogCallback> action2) {
        return createButtonBar(button1Key, newButton(button1Key, action1), button2Key, newButton(button1Key, action2));
    }

    private HBox createButtonBar(String button1Key, Button button1, String button2Key, Button button2) {
        if ("Ok".equals(button1Key) && !watchedUserProperties.isEmpty())
            button1.disableProperty().bind(noChangesProperty);
        return createButtonBar(translateText(button1, button1Key), translateText(button2, button2Key));
    }

    private HBox createButtonBar(Button... buttons) {
        for (Button button : buttons)
            button.setFont(font);
        HBox hBox = new HBox(10, createHGrowable());
        hBox.getChildren().addAll(buttons);
        return hBox;
    }

    @Override
    public GridPane build() {
        return gridPane;
    }

    //// private methods

    private Label createLabel(String labelKey) {
        return setUpLabeled(new Label(), labelKey);
    }

    private <T extends Labeled> T setUpLabeled(T labeled, String labelKey) {
        i18n.translateText(labeled, labelKey).setFont(font);
        //label.textFillProperty().bind(Theme.dialogTextFillProperty());
        GridPane.setHalignment(labeled, HPos.RIGHT);
        if (labeled instanceof CheckBox)
            watchUserProperty(((CheckBox) labeled).selectedProperty());
        return labeled;
    }

    private TextInputControl setUpTextInput(TextInputControl textInput) {
        textInput.setPrefWidth(150d);
        textInput.setFont(font);
        watchUserProperty(textInput.textProperty());
        return textInput;
    }

    private void watchUserProperty(Property userProperty) {
        watchedUserProperties.add(new Pair<>(userProperty, userProperty.getValue()));
        userProperty.addListener(watchedUserPropertyListener);
    }
}
