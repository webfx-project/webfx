package naga.framework.ui.controls;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import naga.framework.ui.action.ButtonFactoryMixin;
import naga.framework.ui.controls.material.textfield.MaterialTextFieldPane;
import naga.framework.ui.layouts.LayoutUtil;
import naga.framework.ui.layouts.SceneUtil;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.scheduler.Scheduled;
import naga.uischeduler.AnimationFramePass;
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
    private boolean searchEnabled = false;
    private ObservableValue<?> loadedContentProperty;
    private BorderPane dialogPane;
    private TextField searchTextField;
    private DialogCallback dialogCallback;
    private Button button;
    private HBox searchBox;
    private Button okButton;
    private Button cancelButton;
    private HBox buttonBar;
    private ShowMode decidedShowMode;

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
        Properties.runOnPropertiesChange(this::updateButtonContentOnNewSelectedItem, selectedItemProperty());
    }

    public boolean isSearchEnabled() {
        return searchEnabled;
    }

    public void setSearchEnabled(boolean searchEnabled) {
        this.searchEnabled = searchEnabled;
    }

    private TextField getSearchTextField() {
        return isSearchEnabled() ? getOrCreateSearchTextField() : null;
    }

    private TextField getOrCreateSearchTextField() {
        if (searchTextField == null) {
            searchTextField = buttonFactory.newTextFieldWithPrompt("GenericSearchPlaceholder");
            searchTextField.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
                if (KeyCode.ESCAPE.equals(e.getCode()) || e.getCharacter().charAt(0) == 27) {
                    closeDialog();
                    e.consume();
                }
            });
            HBox.setHgrow(searchTextField, Priority.ALWAYS);
        }
        return searchTextField;
    }

    ButtonFactoryMixin getButtonFactory() {
        return buttonFactory;
    }

    protected void setLoadedContentProperty(ObservableValue loadedContentProperty) {
        this.loadedContentProperty = loadedContentProperty;
    }

    private boolean isContentLoaded() {
        return loadedContentProperty == null || loadedContentProperty.getValue() != null;
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
        button.setOnAction(e -> toggleDialog());
    }

    public MaterialTextFieldPane toMaterialButton(Object labelKey, Object placeholderKey) {
        return buttonFactory.setMaterialLabelAndPlaceholder(newMaterialButton(), labelKey, placeholderKey);
    }

    public MaterialTextFieldPane toMaterialButton(ObservableValue<String> labelProperty, ObservableValue<String> placeholderProperty) {
        MaterialTextFieldPane materialButton = newMaterialButton();
        if (labelProperty != null)
            materialButton.labelTextProperty().bind(labelProperty);
        if (placeholderProperty != null)
            materialButton.placeholderTextProperty().bind(placeholderProperty);
        return materialButton;
    }

    private MaterialTextFieldPane newMaterialButton() {
        return new MaterialTextFieldPane(LayoutUtil.setMaxWidthToInfinite(getButton()), selectedItemProperty());
    }

    public void updateButtonContentOnNewSelectedItem() {
        Toolkit.get().scheduler().runInUiThread(() -> getButton().setGraphic(getOrCreateButtonContentFromSelectedItem()));
    }

    protected abstract Node getOrCreateButtonContentFromSelectedItem();


    private boolean isDialogOpen() {
        return dialogCallback != null && !dialogCallback.isDialogClosed();
    }

    private void toggleDialog() {
        if (isDialogOpen())
            closeDialog();
        else
            showDialog();
    }

    public void showDialog() {
        setUpDialog(true);
    }

    protected void setUpDialog(boolean show) {
        // Instantiating the dialog pane if not yet done
        if (dialogPane == null) {
            Node dialogContent = getOrCreateDialogContent();
            if (dialogContent == null)
                return;
            dialogPane = new BorderPane(dialogContent);
        }
        if (!isContentLoaded()) {
            setInitialHiddenDialogHeightPropertyForContentLoading();
            startLoading();
        }
        if (show && !isDialogOpen())
            Properties.onPropertySet(loadedContentProperty, x -> {
                dialogPane.setPadding(Insets.EMPTY);
                show(computeDecidedShowMode());
            }, true);
    }

    private static final double INITIAL_HIDDEN_DIALOG_HEIGHT = 400;

    private void setInitialHiddenDialogHeightPropertyForContentLoading() {
        dialogPane.setVisible(false);
        dialogHeightProperty.unbind();
        dialogHeightProperty.setValue(INITIAL_HIDDEN_DIALOG_HEIGHT);
        // Also resetting the highest dialog height used for computeDecidedShowMode()
        dialogHighestHeight = 0;
    }

    protected abstract void startLoading();

    protected void forceDialogRebuiltOnNextShow() {
        dialogPane = null;
    }

    protected abstract Region getOrCreateDialogContent();

    protected StringProperty searchTextProperty() {
        return getOrCreateSearchTextField().textProperty();
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

    private double dialogHighestHeight;

    private ShowMode computeDecidedShowMode() {
        ShowMode showMode = getShowMode();
        if (showMode != ShowMode.AUTO)
            decidedShowMode = showMode;
        else if (dialogPane.getScene() == null)
            decidedShowMode = ShowMode.DROP_DOWN;
        else {
            double spaceAboveButton = computeMaxAvailableHeightAboveButton();
            double spaceBelowButton = computeMaxAvailableHeightBelowButton(spaceAboveButton);
            double dialogHeight = dialogPane.prefHeight(-1);
            // Making the decision from the highest dialog height (we don't change decision when it shrinks, only when it grows)
            dialogHighestHeight = Math.max(dialogHighestHeight, dialogHeight);
            decidedShowMode = dialogHighestHeight < spaceBelowButton ? ShowMode.DROP_DOWN
                    : dialogHighestHeight < spaceAboveButton ? ShowMode.DROP_UP
                    : isSearchEnabled() ? (spaceBelowButton > spaceAboveButton ? ShowMode.DROP_DOWN : ShowMode.DROP_UP)
                    : ShowMode.MODAL_DIALOG;
        }
        return decidedShowMode;
    }

    public ShowMode getDecidedShowMode() {
        return decidedShowMode;
    }

    private double computeMaxAvailableHeightForDropDialog() {
        double spaceAboveButton = computeMaxAvailableHeightAboveButton();
        double spaceBelowButton = computeMaxAvailableHeightBelowButton(spaceAboveButton);
        return Math.max(spaceAboveButton, spaceBelowButton);
    }

    private double computeMaxAvailableHeightAboveButton() {
        return button.localToScene(0, 0).getY();
    }

    private double computeMaxAvailableHeightBelowButton(double spaceAboveButton) {
        return button.getScene().getHeight() - spaceAboveButton - button.getHeight();
    }

    private void show(ShowMode decidedShowMode) {
        closeDialog();
        Region dialogContent = getOrCreateDialogContent();
        Pane parentNow = parentGetter != null ? parentGetter.call() : parent;
        TextField searchTextField = getSearchTextField(); // may return null in case search is not enabled
        switch (decidedShowMode) {
            case MODAL_DIALOG:
                // Removing the (square) border as it will be displayed in a modal gold layout which already has a (rounded) border
                dialogPane.setBorder(null);
                setMaxPrefSizeToInfinite(dialogContent);
                if (buttonBar == null) {
                    okButton = buttonFactory.newOkButton(this::onDialogOk);
                    cancelButton = buttonFactory.newCancelButton(this::onDialogCancel);
                    buttonBar = new HBox(20, createHGrowable(), okButton, cancelButton, createHGrowable());
                    buttonBar.setPadding(new Insets(10, 0, 0, 0));
                }
                dialogPane.setTop(searchTextField);
                dialogPane.setBottom(buttonBar);
                dialogCallback = DialogUtil.showModalNodeInGoldLayout(dialogPane, parentNow, 0.95, 0.95);
                dialogHeightProperty.bind(dialogPane.heightProperty());
                // Resetting default and cancel buttons (required for JavaFx if displayed a second time)
                ButtonUtil.resetDefaultButton(okButton);
                ButtonUtil.resetCancelButton(cancelButton);
                dialogPane.setVisible(true);
                break;

            case DROP_DOWN:
            case DROP_UP:
                dialogPane.setBorder(BorderUtil.newBorder(Color.DARKGRAY));
                setMaxPrefSize(dialogContent, USE_COMPUTED_SIZE);
                double maxHeight = computeMaxAvailableHeightForDropDialog();
                if (isSearchEnabled())
                    maxHeight = Math.min(maxHeight, INITIAL_HIDDEN_DIALOG_HEIGHT);
                dialogContent.setMaxHeight(maxHeight);
                searchBox = !isSearchEnabled() ? null :
                        new HBox(searchTextField, buttonFactory.newButton("...", this::switchToModalDialog));
                onDecidedShowMode(decidedShowMode);
                dialogCallback = DialogUtil.showDropUpOrDownDialog(dialogPane, button, parentNow, loadedContentProperty, decidedShowMode == ShowMode.DROP_UP);
                dialogCallback.addCloseHook(
                            Properties.runNowAndOnPropertiesChange(this::applyNewDecidedShowMode,
                                dialogPane.getScene().heightProperty(),
                                dialogPane.heightProperty(),
                                loadedContentProperty
                            )::unregister);
                break;
        }
        dialogCallback.addCloseHook(() -> {
            if (button != null)
                button.requestFocus();
            dialogPane = null; // Could be reused but for any reason has width resizing issue after being shown in modal dialog, so we force re-creation to a brand new instance
        });
        if (searchTextField != null)
            searchTextField.setText(null); // Resetting the search box
    }

    private Scheduled scheduled;

    private void applyNewDecidedShowMode() {
        if (isLoadedContentLayoutInDialog())
            applyNewDecidedShowModeNow();
        else if (scheduled == null)
            scheduled = Toolkit.get().scheduler().scheduleInFutureAnimationFrame(1, () -> {
                scheduled = null;
                applyNewDecidedShowMode();
            }, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
    }

    private boolean isLoadedContentLayoutInDialog() {
        return dialogPane.isVisible() || dialogPane.getCenter().prefHeight(-1) > 5;
    }

    private void applyNewDecidedShowModeNow() {
        ShowMode decidedShowMode2 = computeDecidedShowMode(); // decided show mode may change in dependence of the height
        onDecidedShowMode(decidedShowMode2);
        if (decidedShowMode2 == ShowMode.MODAL_DIALOG)
            switchToModalDialog();
        else {
            DialogUtil.updateDropUpOrDownDialogPosition(dialogPane);
            SceneUtil.scrollNodeToBeVerticallyVisibleOnScene(button, true, true);
            if (!dialogPane.isVisible())
                dialogPane.setVisible(true);
            if (searchTextField != null)
                SceneUtil.autoFocusIfEnabled(searchTextField);
        }
    }

    private void onDecidedShowMode(ShowMode decidedShowMode) {
        TextField searchTextField = getSearchTextField();
        boolean focused = searchTextField != null && searchTextField.isFocused();
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

    private void switchToModalDialog() {
        closeDialog();
        forceDialogRebuiltOnNextShow(); setUpDialog(false); // This line could be removed but
        show(ShowMode.MODAL_DIALOG);
    }

    protected void onDialogOk() {
        closeDialog();
    }

    protected void onDialogCancel() {
        closeDialog();
    }

    protected void closeDialog() {
        if (isDialogOpen())
            dialogCallback.closeDialog();
    }
}
