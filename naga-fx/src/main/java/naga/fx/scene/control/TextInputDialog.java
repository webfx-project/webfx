package naga.fx.scene.control;

import javafx.geometry.Pos;
import naga.fx.scene.layout.GridPane;
import naga.fx.scene.layout.Priority;
import naga.fx.scene.layout.Region;
import naga.fx.spi.Toolkit;

/**
 * A dialog that shows a text input control to the user.
 *
 * @see Dialog
 * @since JavaFX 8u40
 */
public class TextInputDialog extends Dialog<String> {

    /**************************************************************************
     *
     * Fields
     *
     **************************************************************************/

    private final GridPane grid;
    private final Label label;
    private final TextField textField;
    private final String defaultValue;



    /**************************************************************************
     *
     * Constructors
     *
     **************************************************************************/

    /**
     * Creates a new TextInputDialog without a default value entered into the
     * dialog {@link TextField}.
     */
    public TextInputDialog() {
        this("");
    }

    /**
     * Creates a new TextInputDialog with the default value entered into the
     * dialog {@link TextField}.
     */
    public TextInputDialog(String defaultValue) {
        final DialogPane dialogPane = getDialogPane();

        // -- textfield
        this.textField = new TextField(defaultValue);
        this.textField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(textField, Priority.ALWAYS);
        GridPane.setFillWidth(textField, true);

        // -- label
        label = DialogPane.createContentLabel(dialogPane.getContentText());
        label.setPrefWidth(Region.USE_COMPUTED_SIZE);
        label.textProperty().bind(dialogPane.contentTextProperty());

        this.defaultValue = defaultValue;

        this.grid = new GridPane();
        this.grid.setHgap(10);
        this.grid.setMaxWidth(Double.MAX_VALUE);
        this.grid.setAlignment(Pos.CENTER_LEFT);

        dialogPane.contentTextProperty().addListener(o -> updateGrid());

        setTitle("Title" /*ControlResources.getString("Dialog.confirm.title")*/);
        dialogPane.setHeaderText("Header"/*ControlResources.getString("Dialog.confirm.header")*/);
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid();

        setResultConverter((dialogButton) -> {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            return data == ButtonBar.ButtonData.OK_DONE ? textField.getText() : null;
        });
    }



    /**************************************************************************
     *
     * Public API
     *
     **************************************************************************/

    /**
     * Returns the {@link TextField} used within this dialog.
     */
    public final TextField getEditor() {
        return textField;
    }

    /**
     * Returns the default value that was specified in the constructor.
     */
    public final String getDefaultValue() {
        return defaultValue;
    }



    /**************************************************************************
     *
     * Private Implementation
     *
     **************************************************************************/

    private void updateGrid() {
        grid.getChildren().clear();

        grid.add(label, 0, 0);
        grid.add(textField, 1, 0);
        getDialogPane().setContent(grid);

        /*Platform.runLater*/
        Toolkit.get().scheduler().scheduleDeferred(() -> textField.requestFocus());
    }
}
