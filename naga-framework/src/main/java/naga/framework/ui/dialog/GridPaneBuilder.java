package naga.framework.ui.dialog;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import naga.commons.util.collection.Collections;
import naga.commons.util.tuples.Pair;
import naga.framework.ui.i18n.I18n;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class GridPaneBuilder {

    private final GridPane gridPane = new GridPane();
    private final Font font = Font.getDefault();
    private final I18n i18n;
    private int rowCount;
    private int colCount;
    private List<Pair<TextField, String>> watched = new ArrayList<>();
    private Property<Boolean> noChangesProperty = new SimpleObjectProperty<>(true);

    public GridPaneBuilder(I18n i18n) {
        this.i18n = i18n;
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        ColumnConstraints cc1 = new ColumnConstraints();
        cc1.setHgrow(Priority.NEVER);
        ColumnConstraints cc2 = new ColumnConstraints();
        cc2.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().setAll(cc1, cc2);
    }

    public GridPaneBuilder addLabelTextFieldRow(String labelKey, TextField textField) {
        Label label = i18n.translateText(new Label(), labelKey);
        textField.setPrefWidth(150d);
        label.setFont(font);
        textField.setFont(font);
        //label.textFillProperty().bind(Theme.dialogTextFillProperty());
        GridPane.setHalignment(label, HPos.RIGHT);
        // Watching textField
        watched.add(new Pair<>(textField, textField.getText()));
        textField.textProperty().addListener((observable, oldValue, newValue) ->
            noChangesProperty.setValue(Collections.findFirst(watched, pair -> !pair.get1().getText().equals(pair.get2())) == null)
        );
        return addNewRow(label, textField);
    }

    public GridPaneBuilder addNewRow(Node... children) {
        colCount = Math.max(colCount, children.length);
        gridPane.addRow(rowCount++, children);
        return this;
    }

    public GridPaneBuilder addButton(String buttonKey, Button button) {
        i18n.translateText(button, buttonKey).setFont(font);
        GridPane.setHalignment(button, HPos.RIGHT);
        gridPane.add(button, 0, rowCount++, colCount, 1);
        return this;
    }

    public GridPaneBuilder addButtons(String button1Key, Button button1, String button2Key, Button button2) {
        if ("Ok".equals(button1Key))
            button1.disableProperty().bind(noChangesProperty);
        i18n.translateText(button1, button1Key).setFont(font);
        i18n.translateText(button2, button2Key).setFont(font);
        Region grow = new Region();
        grow.setMaxWidth(Double.MAX_VALUE);
        HBox hbox = new HBox(10, grow, button1, button2);
        HBox.setHgrow(grow, Priority.ALWAYS);
        GridPane.setMargin(hbox, new Insets(20, 0, 0, 0));
        gridPane.add(hbox, 0, rowCount++, colCount, 1);
        return this;
    }
    public GridPane getGridPane() {
        return gridPane;
    }
}
