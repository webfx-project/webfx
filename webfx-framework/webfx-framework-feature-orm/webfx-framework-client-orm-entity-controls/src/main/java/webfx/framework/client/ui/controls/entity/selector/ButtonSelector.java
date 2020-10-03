package webfx.framework.client.ui.controls.entity.selector;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import webfx.framework.client.ui.controls.MaterialFactoryMixin;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.client.ui.controls.button.ButtonUtil;
import webfx.framework.client.ui.controls.dialog.DialogCallback;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.framework.client.ui.util.scene.SceneUtil;
import webfx.extras.materialdesign.textfield.MaterialTextFieldPane;
import webfx.framework.client.ui.util.border.BorderUtil;
import webfx.kit.util.properties.Properties;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.scheduler.Scheduled;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.shared.util.function.Callable;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;
import static webfx.framework.client.ui.util.layout.LayoutUtil.*;

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
    private boolean autoOpenOnMouseEntered;
    private boolean searchEnabled = true;
    private ObservableValue<?> loadedContentProperty;
    private BorderPane dialogPane;
    private TextField searchTextField;
    private DialogCallback dialogCallback;
    protected Button button;
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

    public boolean isAutoOpenOnMouseEntered() {
        return autoOpenOnMouseEntered;
    }

    public void setAutoOpenOnMouseEntered(boolean autoOpenOnMouseEntered) {
        this.autoOpenOnMouseEntered = autoOpenOnMouseEntered;
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
            searchTextField = buttonFactory.newTextField("GenericSearch");
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

    public void setReadOnly(boolean readOnly) {
        getButton().setDisable(readOnly);
    }

    public boolean isReadOnly() {
        return getButton().isDisabled();
    }

    public Button getButton() {
        if (button == null)
            setButton(ButtonUtil.newDropDownButton());
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
        button.setOnAction(e -> onButtonClicked());
        button.setOnMouseEntered(e -> onMouseEntered());
        button.setOnMouseExited( e -> onMouseExited());
    }

    public MaterialTextFieldPane toMaterialButton(Object i18nKey) {
        // Assuming the passed buttonFactory is actually instance of MaterialFactoryMixin when we call this method
        return ((MaterialFactoryMixin) buttonFactory).setMaterialLabelAndPlaceholder(newMaterialButton(), i18nKey);
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
        UiScheduler.runInUiThread(() -> getButton().setGraphic(getOrCreateButtonContentFromSelectedItem()));
    }

    protected abstract Node getOrCreateButtonContentFromSelectedItem();


    private boolean isDialogOpen() {
        return dialogCallback != null && !dialogCallback.isDialogClosed();
    }

    private boolean userJustPressedButtonInOrderToCloseDialog;

    private boolean openDueToMouseEntered;
    private void onMouseEntered() {
        if (isAutoOpenOnMouseEntered() && !isReadOnly()) {
            openDueToMouseEntered = true;
            showDialog();
        }
    }

    private void onMouseExited() {
        scheduleMouseExistedDialogClose();
    }

    private void scheduleMouseExistedDialogClose() {
        if (openDueToMouseEntered) {
            openDueToMouseEntered = false;
            Scheduler.scheduleDelay(100, ()-> {
                if (!openDueToMouseEntered)
                    closeDialog();
            });
        }
    }

    private void onButtonClicked() {
        if (userJustPressedButtonInOrderToCloseDialog)
            userJustPressedButtonInOrderToCloseDialog = false;
        else if (!isReadOnly())
            toggleDialog();
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
            dialogPane.setOnMouseExited(e -> scheduleMouseExistedDialogClose());
            dialogPane.setOnMouseEntered(e-> onMouseEntered());
        }
        if (!isContentLoaded()) {
            setInitialHiddenDialogHeightPropertyForContentLoading();
            startLoading();
        }
        if (show && !isDialogOpen())
            Properties.onPropertySet(loadedContentProperty, x -> {
                updateDecidedShowMode();
                show();
            }, true);
    }

    private static final double INITIAL_HIDDEN_DIALOG_HEIGHT = 400;

    private void setInitialHiddenDialogHeightPropertyForContentLoading() {
        dialogPane.setVisible(false);
        dialogHeightProperty.unbind();
        dialogHeightProperty.setValue(INITIAL_HIDDEN_DIALOG_HEIGHT);
    }

    protected abstract void startLoading();

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

    private ShowMode updateDecidedShowMode() {
        ShowMode showMode = getShowMode();
        if (showMode != ShowMode.AUTO)
            decidedShowMode = showMode;
        else if (dialogPane.getScene() == null)
            decidedShowMode = ShowMode.DROP_DOWN;
        else if (!SceneUtil.isVirtualKeyboardShowing(dialogPane.getScene())) { // we don't change the decided show mode while the virtual keyboard is showing
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

    private void show() {
        // Doing nothing if the dialog is already showing (otherwise same node inserted twice in scene graph => error)
        if (dialogPane != null && dialogPane.getParent() != null) // May happen when quickly moving mouse over several
            return; // entity buttons in auto open mode
        Region dialogContent = getOrCreateDialogContent();
        Pane parentNow = parentGetter != null ? parentGetter.call() : parent;
        TextField searchTextField = getSearchTextField(); // may return null in case search is not enabled
        switch (decidedShowMode) {
            case MODAL_DIALOG:
                // Removing the (square) border as it will be displayed in a modal gold layout which already has a (rounded) border
                dialogPane.setBorder(null);
                setMaxPrefSizeToInfinite(dialogContent);
                if (buttonBar == null)
                    buttonBar = LayoutUtil.setPadding(new HBox(20, createHGrowable(),
                            okButton = buttonFactory.newOkButton(this::onDialogOk),
                            cancelButton = buttonFactory.newCancelButton(this::onDialogCancel),
                            createHGrowable()), 10, 0, 0, 0);
                dialogPane.setTop(searchTextField);
                dialogPane.setBottom(buttonBar);
                dialogCallback = DialogUtil.showModalNodeInGoldLayout(dialogPane, parentNow, 0.95, 0.95);
                dialogHeightProperty.bind(dialogPane.heightProperty());
                // Resetting default and cancel buttons (required for JavaFx if displayed a second time)
                ButtonUtil.resetDefaultAndCancelButtons(okButton, cancelButton);
                dialogPane.setVisible(true);
                break;

            case DROP_DOWN:
            case DROP_UP:
                LayoutUtil.removePadding(dialogPane).setBorder(BorderUtil.newBorder(Color.DARKGRAY));
                setMaxPrefSize(dialogContent, USE_COMPUTED_SIZE);
                double maxHeight = computeMaxAvailableHeightForDropDialog();
                if (isSearchEnabled())
                    maxHeight = Math.min(maxHeight, INITIAL_HIDDEN_DIALOG_HEIGHT);
                dialogContent.setMaxHeight(maxHeight);
                searchBox = !isSearchEnabled() ? null :
                        new HBox(searchTextField, buttonFactory.newButton("...", this::switchToModalDialog));
                installSearchBoxForDecidedShowModeIfEnabled();
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
            // Button focus management: 2 questions: 1) Should we restore the focus to the button? 2) Should the next button click reopen the dialog?
            if (button != null) {
                // Reply to 1): yes if the last focus was inside the dialog, no otherwise (ex: the dialog closed because the user clicked outside)
                if (SceneUtil.isFocusInside(dialogPane))
                    button.requestFocus();
                // Reply to 2): no if the dialog was closed because the user just pressed the button (his intention is to close the dialog, not to reopen it!)
                userJustPressedButtonInOrderToCloseDialog = button.isPressed(); // See onButtonClicked() which is using this flag
            }
            reset();
        });
        if (searchTextField != null)
            searchTextField.setText(null); // Resetting the search box
    }

    private void reset() {
        // This dialog instance could be reused in theory but for any reason (?) it has width resizing issue after having
        // been shown in modal dialog, so we force re-creation to have a brand new instance next time with no width issue
        if (decidedShowMode == ShowMode.MODAL_DIALOG)
            forceDialogRebuiltOnNextShow();
        decidedShowMode = null;
        dialogHighestHeight = 0;
    }

    protected void forceDialogRebuiltOnNextShow() {
        dialogPane = null;
    }

    private Scheduled scheduled;

    private void applyNewDecidedShowMode() {
        if (isLoadedContentLayoutInDialog())
            applyNewDecidedShowModeNow();
        else if (scheduled == null)
            scheduled = UiScheduler.scheduleInAnimationFrame(() -> {
                scheduled = null;
                applyNewDecidedShowMode();
            }, 1, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
    }

    private boolean isLoadedContentLayoutInDialog() {
        return dialogPane != null && (dialogPane.isVisible() || dialogPane.getCenter().prefHeight(-1) > 5);
    }

    private void applyNewDecidedShowModeNow() {
        ShowMode previousDecidedShowMode = decidedShowMode;
        if (updateDecidedShowMode() == ShowMode.MODAL_DIALOG)
            switchToModalDialog();
        else {
            if (decidedShowMode != previousDecidedShowMode) {
                installSearchBoxForDecidedShowModeIfEnabled();
                TextField searchTextField = getSearchTextField();
                if (searchTextField != null && !searchTextField.isFocused())
                    SceneUtil.autoFocusIfEnabled(searchTextField);
            } else {
                // This code is in case a virtual keyboard just appeared, at this stage, the layout is not finished so we
                // update the dialog position again later (2 animation frames later seems necessary)
                UiScheduler.scheduleInAnimationFrame(this::updateDropUpOrDownDialogPosition, 2,
                        AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
            }
            updateDropUpOrDownDialogPosition();
            if (!dialogPane.isVisible())
                dialogPane.setVisible(true);
        }
    }

    private void updateDropUpOrDownDialogPosition() {
        TextField searchTextField = getSearchTextField();
        DialogUtil.setDropDialogBounded (dialogPane, searchTextField != null && searchTextField.isFocused());
        DialogUtil.setDropDialogUp(dialogPane, decidedShowMode == ShowMode.DROP_UP);
        DialogUtil.updateDropUpOrDownDialogPosition(dialogPane);
    }

    private void installSearchBoxForDecidedShowModeIfEnabled() {
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
    }

    private void switchToModalDialog() {
        closeDialog();
        forceDialogRebuiltOnNextShow(); setUpDialog(false); // This line could be removed but
        decidedShowMode = ShowMode.MODAL_DIALOG;
        show();
    }

    protected void onDialogOk() {
        closeDialog();
    }

    protected void onDialogCancel() {
        closeDialog();
    }

    protected void closeDialog() {
        if (isDialogOpen()) {
            dialogCallback.closeDialog();
            if (closeHandler != null)
                closeHandler.run();
        }
    }

    private Runnable closeHandler;
    public void setCloseHandler(Runnable closeHandler) {
        this.closeHandler = closeHandler;
    }

}
