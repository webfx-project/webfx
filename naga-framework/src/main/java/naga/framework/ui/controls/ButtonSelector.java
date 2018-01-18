package naga.framework.ui.controls;

import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import naga.framework.ui.action.ButtonFactoryMixin;
import naga.framework.ui.layouts.SceneUtil;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.util.function.Callable;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static naga.framework.ui.layouts.LayoutUtil.*;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonSelector<T> {

    public enum ShowMode {
        MODAL_DIALOG,
        DROP_DOWN,
        DROP_UP,
        AUTO
    }

    private final Callable<Pane> parentGetter;
    private final Pane parent;
    private final ButtonFactoryMixin buttonFactory;
    private ObservableValue resizeProperty;
    private BorderPane dialogPane;
    private final TextField searchTextField;
    protected DialogCallback dialogCallback;
    private Button button;
    private HBox searchBox;
    private Button okButton;
    private Button cancelButton;
    private HBox buttonBar;

    private final Property<ShowMode> showModeProperty = new SimpleObjectProperty<>(ShowMode.AUTO);
    private final Property<T> selectedItemProperty = new SimpleObjectProperty<>();
    private final DoubleProperty dialogHeightProperty = new SimpleDoubleProperty();

    public ButtonSelector(ButtonFactoryMixin buttonFactory, Callable<Pane> parentGetter) {
        this(buttonFactory, parentGetter, null);
    }

    public ButtonSelector(ButtonFactoryMixin buttonFactory, Pane parent) {
        this(buttonFactory, null, parent);
    }

    protected ButtonSelector(ButtonFactoryMixin buttonFactory, Callable<Pane> parentGetter, Pane parent) {
        this.parentGetter = parentGetter;
        this.parent = parent;
        this.buttonFactory = buttonFactory;
        Properties.runOnPropertiesChange(p -> updateButtonContentOnNewSelectedItem(), selectedItemProperty());
        searchTextField = buttonFactory.newTextFieldWithPrompt("GenericSearchPlaceholder");
        searchTextField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (KeyCode.ESCAPE.equals(e.getCode()) || e.getCharacter().charAt(0) == 27) {
                dialogCallback.closeDialog();
                e.consume();
            }
        });
        HBox.setHgrow(searchTextField, Priority.ALWAYS);
    }

    protected ButtonFactoryMixin getButtonFactory() {
        return buttonFactory;
    }

    protected void setResizeProperty(ObservableValue resizeProperty) {
        this.resizeProperty = resizeProperty;
    }

    public Property<T> selectedItemProperty() {
        return selectedItemProperty;
    }

    public T getSelectedItem() {
        return selectedItemProperty.getValue();
    }

    public void setSelectedItem(T item) {
        selectedItemProperty.setValue(item);
    }

    protected ReadOnlyDoubleProperty dialogHeightProperty() {
        return dialogHeightProperty;
    }

    public void setEditable(boolean editable) {
        getButton().setDisable(!editable);
    }

    public Button getButton() {
        if (button == null)
            setButton(ButtonUtil.newDropDownButton());
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
        button.setOnAction(e -> showDialog());
    }

    public void updateButtonContentOnNewSelectedItem() {
        Toolkit.get().scheduler().runInUiThread(() -> getButton().setGraphic(getOrCreateButtonContentFromSelectedItem()));
    }

    protected abstract Node getOrCreateButtonContentFromSelectedItem();

    public void showDialog() {
        setUpDialog(true);
    }

    protected void setUpDialog(boolean show) {
        if (dialogPane == null) {
            Node dialogContent = getOrCreateDialogContent();
            if (dialogContent == null)
                return;
            dialogPane = new BorderPane(dialogContent);
            dialogPane.setBorder(BorderUtil.newBorder(Color.DARKGRAY));
            dialogHeightProperty.bind(dialogPane.heightProperty());
        }
        if (show) {
            dialogPane.setPadding(Insets.EMPTY);
            show(computeDecidedShowMode());
        }
    }

    protected void forceDialogRebuiltOnNextShow() {
        dialogPane = null;
    }

    protected abstract Region getOrCreateDialogContent();

    protected StringProperty searchTextProperty() {
        return searchTextField.textProperty();
    }


    public Property<ShowMode> showModeProperty() {
        return showModeProperty;
    }

    public ShowMode getShowMode() {
        return showModeProperty().getValue();
    }

    public void setShowMode(ShowMode showModeProperty) {
        this.showModeProperty().setValue(showModeProperty);
    }

    private ShowMode computeDecidedShowMode() {
        ShowMode decidedShowMode = getShowMode();
        if (decidedShowMode == ShowMode.AUTO) {
            Point2D buttonBottom = button.localToScene(0, button.getHeight());
            decidedShowMode = button.getScene().getHeight() - buttonBottom.getY() < 200d + searchTextField.getHeight() ? ShowMode.DROP_UP : ShowMode.DROP_DOWN;
        }
        return decidedShowMode;
    }

    protected void show(ShowMode decidedShowMode) {
        if (dialogCallback != null)
            dialogCallback.closeDialog();
        Region dialogContent = getOrCreateDialogContent();
        Pane parentNow = parentGetter != null ? parentGetter.call() : parent;
        switch (decidedShowMode) {
            case MODAL_DIALOG:
                setMaxPrefSizeToInfinite(dialogContent);
                if (buttonBar == null) {
                    okButton = buttonFactory.newOkButton(this::onDialogOk);
                    cancelButton = buttonFactory.newCancelButton(this::onDialogCancel);
                    buttonBar = new HBox(20, createHGrowable(), okButton, cancelButton, createHGrowable());
                    buttonBar.setPadding(new Insets(20, 0, 0, 0));
                }
                dialogPane.setTop(searchTextField);
                dialogPane.setBottom(buttonBar);
                dialogCallback = DialogUtil.showModalNodeInGoldLayout(dialogPane, parentNow, 0.9, 0.8);
                // Resetting default and cancel buttons (required for JavaFx if displayed a second time)
                ButtonUtil.resetDefaultButton(okButton);
                ButtonUtil.resetCancelButton(cancelButton);
                break;

            case DROP_DOWN:
            case DROP_UP:
                setMaxPrefSize(dialogContent, USE_COMPUTED_SIZE);
                dialogContent.setMaxHeight(200d);
                Button modalButton = new Button("...");
                searchBox = new HBox(searchTextField, modalButton);
                modalButton.setOnAction(e -> {
                    dialogCallback.closeDialog();
                    forceDialogRebuiltOnNextShow(); setUpDialog(false); // This line could be removed but
                    show(ShowMode.MODAL_DIALOG);
                });
                onDecidedShowMode(decidedShowMode);
                dialogCallback = DialogUtil.showDropUpOrDownDialog(dialogPane, button, parentNow, resizeProperty, decidedShowMode == ShowMode.DROP_UP);
                ChangeListener<Number> sceneHeightListener = (observable, oldValue, newValue) -> Platform.runLater(() -> {
                    SceneUtil.scrollNodeToBeVerticallyVisibleOnScene(button, true, true);
                    onDecidedShowMode(computeDecidedShowMode()); // decided show mode may change in dependence of the height
                    DialogUtil.updateDropUpOrDownDialogPosition(dialogPane);
                });
                ObservableValue<? extends Number> sceneHeightProperty = dialogPane.getScene().heightProperty();
                sceneHeightProperty.addListener(sceneHeightListener);
                dialogCallback.addCloseHook(() -> sceneHeightProperty.removeListener(sceneHeightListener));
                break;
        }
        if (searchTextField != null) {
            searchTextField.setText(null); // Resetting the search box
            SceneUtil.autoFocusIfEnabled(searchTextField);
        }
    }

    private void onDecidedShowMode(ShowMode decidedShowMode) {
        boolean focused = searchTextField.isFocused();
        if (decidedShowMode == ShowMode.DROP_DOWN) {
            dialogPane.setBottom(null);
            dialogPane.setTop(searchBox);
        } else {
            dialogPane.setTop(null);
            dialogPane.setBottom(searchBox);
        }
        if (focused)
            searchTextField.requestFocus();
        DialogUtil.setDropDialogUp(dialogPane, decidedShowMode == ShowMode.DROP_UP);
    }

    protected void onDialogOk() {
        closeDialog();
    }

    protected void onDialogCancel() {
        closeDialog();
    }

    protected void closeDialog() {
        dialogCallback.closeDialog();
        dialogCallback = null;
    }
}
