package mongoose.activities.shared.book.event.options;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.StringConverter;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.activities.shared.logic.work.transaction.WorkingDocumentTransaction;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.properties.Properties;
import naga.fx.util.ImageStore;
import naga.util.Booleans;
import naga.util.Objects;
import naga.util.collection.Collections;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
class OptionTreeNode {

    private final Option option;
    private final OptionTree tree;
    private final OptionTreeNode parent;
    private BorderPane topLevelOptionButton;
    private BorderPane topLevelOptionSection;
    private Pane optionBodyPane;
    private ButtonBase optionButton;
    private Property<Boolean> optionButtonSelectedProperty;
    private ChoiceBox<Option> childrenChoiceBox;
    private ToggleGroup childrenToggleGroup;
    private List<OptionTreeNode> childrenOptionTreeNodes;
    private OptionTreeNode lastSelectedChildOptionTreeNode;
    private final BooleanProperty visibleProperty = new SimpleBooleanProperty(true);
    private static boolean staticSyncing;
    private boolean thisSyncing;
    private WorkingDocumentLine modelLine;
    private Boolean userExplicitSelection; /* Set to non null value to allow the ui to be temporary
     desynchronized from the model during a transitional state. Ex: The booker ticked the translation checkbox but hasn't
     yet selected the language. This is a transitional state because the translation option can't yet be added to the
     model (as no language selected) but it is necessary to keep the checkbox ticked in the ui to let the booker select
     the language. Once selected, this language selection causes the translation option to be added to the model so this
     flag should be set back to false because the ui and model are synchronized again.*/


    /*******************************************************************************************************************
     *  CONSTRUCTORS  **************************************************************************************************
     ******************************************************************************************************************/

    OptionTreeNode(Option option, OptionTree tree) {
        this(option, tree, null);
    }

    private OptionTreeNode(Option option, OptionTreeNode parent) {
        this(option, parent.tree, parent);
    }

    private OptionTreeNode(Option option, OptionTree tree, OptionTreeNode parent) {
        this.option = option;
        this.tree = tree;
        this.parent = parent;
        tree.registerOptionTreeNode(this);
    }


    /*******************************************************************************************************************
     *  GETTERS AND SETTERS  *******************************************************************************************
     ******************************************************************************************************************/

    public Option getOption() {
        return option;
    }

    private ToggleGroup getChildrenToggleGroup() {
        return childrenToggleGroup;
    }

    protected I18n getI18n() {
        return tree.getI18n();
    }

    private String bestTranslationOrName(Object o) {
        return Labels.instantTranslateLabel(Labels.bestLabelOrName(o), getI18n());
    }

    private WorkingDocumentTransaction getWorkingDocumentTransaction() {
        return tree.getWorkingDocumentTransaction();
    }

    private WorkingDocument getWorkingDocument() {
        return getWorkingDocumentTransaction().getWorkingDocument();
    }

    private void setUserExplicitSelection(Boolean value) {
        userExplicitSelection = value;
    }

    private boolean isUserExplicitlySelected() {
        return Booleans.isTrue(userExplicitSelection);
    }

    private boolean isUserExplicitlyDeselected() {
        return Booleans.isFalse(userExplicitSelection);
    }

    private List<Option> getChildrenOptions(Option parent) {
        return tree.getActivity().getChildrenOptions(parent);
    }

    private boolean isChildMultipleSelectionAllowed() {
        return childrenToggleGroup == null && childrenChoiceBox == null;
    }

    void reset() { // Called when working with a complete new working document
        userExplicitSelection = null;
        modelLine = null;
        lastSelectedChildOptionTreeNode = null;
        // Also cleaning the choice box if present
        if (childrenChoiceBox != null) {
            // Necessary to set all children sync flags to true to avoid any UI => model sync at this stage before making the call
            for (OptionTreeNode child : childrenOptionTreeNodes)
                child.thisSyncing = true;
            // Now ok to clean the choice box (all children are listening it but this won't trigger UI => model sync)
            childrenChoiceBox.getSelectionModel().select(null);
            // Restoring all sync flags
            for (OptionTreeNode child : childrenOptionTreeNodes)
                child.thisSyncing = false;
        }
    }


    /*******************************************************************************************************************
     *  UI BUILDERS  ***************************************************************************************************
     ******************************************************************************************************************/

    Node createOrUpdateTopLevelOptionButtonFromModel() {
        if (topLevelOptionButton == null)
            createTopLevelOptionButton();
        syncUiFromModel();
        return topLevelOptionButton;
    }

    private void createTopLevelOptionButton() {
        topLevelOptionButton = createTopLevelOptionSection(false);
        topLevelOptionButton.setOnMouseClicked(e -> optionButtonSelectedProperty.setValue(!optionButtonSelectedProperty.getValue()));
        topLevelOptionButton.setCursor(Cursor.HAND);
        ImageView checkBoxView = new ImageView();
        checkBoxView.imageProperty().bind(Properties.compute(optionButtonSelectedProperty, selected ->
                ImageStore.getOrCreateImage(selected ? "images/16/checked.png" : "images/16/unchecked.png", 16, 16)));
        ObservableList<Node> hBoxChildren = ((HBox) topLevelOptionButton.getTop()).getChildren();
        hBoxChildren.add(0, checkBoxView);
        hBoxChildren.add(0, LayoutUtil.createHGrowable()); // To shift the button content and make it centered
        LayoutUtil.setMinWidthToPref(topLevelOptionButton);
        LayoutUtil.setMaxSizeToInfinite(topLevelOptionButton);
    }

    Node createOrUpdateTopLevelOptionSectionFromModel() {
        if (topLevelOptionSection == null)
            topLevelOptionSection = createTopLevelOptionSection(true);
        syncUiFromModel();
        return topLevelOptionSection;
    }

    private BorderPane createTopLevelOptionSection(boolean detailed) {
        BorderPane sectionPanel = HighLevelComponents.createSectionPanel(null, Collections.toArray(
                createOptionPanelHeaderNodes(Labels.translateLabel(Labels.bestLabelOrName(option), getI18n()))
                , Node[]::new));
        createOptionButtonAndSelectedProperty();
        if (detailed) {
            createOptionBodyPane();
            if (optionBodyPane != null) {
                optionBodyPane.setPadding(new Insets(20));
                sectionPanel.setCenter(optionBodyPane);
                bindToVisibleProperty(sectionPanel);
            }
        }
        return sectionPanel;
    }

    private List<Node> createOptionPanelHeaderNodes(Property<String> i18nTitle) {
        return tree.getActivity().createOptionPanelHeaderNodes(option, i18nTitle);
    }

    private Pane createOptionBodyPane() {
        createOptionButtonAndSelectedProperty();
        if (optionButton == null && option.isNotObligatory() && option.hasParent())
            return optionBodyPane = null;
        optionBodyPane = new VBox();
        if (parent != null && parent.optionButton != null) // Adding a left padding when under a parent button
            optionBodyPane.setPadding(new Insets(0, 0, 0, 20));
        Label topLabel = option.getTopLabel();
        ObservableList<Node> optionBodyChildren = optionBodyPane.getChildren();
        if (topLabel != null)
            optionBodyChildren.add(createLabelNode(topLabel));
        if (optionButton != null)
            optionBodyChildren.add(optionButton);
        List<Option> childrenOptions = getChildrenOptions(option);
        if (!Collections.isEmpty(childrenOptions)) {
            if ("select".equals(option.getLayout())) {
                childrenChoiceBox = new ChoiceBox<>();
                childrenChoiceBox.setConverter(new StringConverter<Option>() {
                    @Override
                    public String toString(Option option) {
                        String optionTranslation = bestTranslationOrName(option);
                        boolean multiSite = option.getParent() != null && option.getParent().getSite() == null;
                        if (multiSite)
                            optionTranslation = bestTranslationOrName(option.getSite()) + " - " + optionTranslation;
                        return optionTranslation;
                    }

                    @Override
                    public Option fromString(String string) {
                        return null;
                    }
                });
                Label childrenPromptLabel = option.getChildrenPromptLabel();
                Node selectNode = childrenChoiceBox;
                if (childrenPromptLabel != null)
                    selectNode = new FlowPane(10, 0, createLabelNode(childrenPromptLabel), selectNode);
                optionBodyChildren.add(selectNode);
                bindToVisibleProperty(selectNode);
                Properties.runOnPropertiesChange(p -> refreshChildrenChoiceBoxOnLanguageChange(), getI18n().languageProperty());
            } else if (option.isChildrenRadio())
                childrenToggleGroup = new ToggleGroup();
            //Doesn't work on Android: childrenOptionTreeNodes = childrenOptions.stream().map(o -> new OptionTreeNode(o, this)).collect(Collectors.toList());
            childrenOptionTreeNodes = Collections.map(childrenOptions, o -> new OptionTreeNode(o, this));
            //Doesn't work on Android: optionBodyChildren.addAll(childrenOptionTreeNodes.stream().map(OptionTreeNode::createPanelBodyNode).filter(Objects::nonNull).collect(Collectors.toList()));
            optionBodyChildren.addAll(Collections.mapFilter(childrenOptionTreeNodes, OptionTreeNode::createOptionBodyPane, Objects::nonNull));
        }
        if (parent != null) // visibility binding is not necessary at top level because the body is embed in the section where visibility is already managed
            bindToVisibleProperty(optionBodyPane);
        return optionBodyPane;
    }

    private void bindToVisibleProperty(Node node) {
        LayoutUtil.setUnmanagedWhenInvisible(node, visibleProperty);
        node.getProperties().put("visiblePropertyOptionTreeNode", this); // used by computeVisibleProperty()
    }

    private Node createLabelNode(Label label) {
        return tree.getActivity().createLabelNode(label);
    }

    private void createOptionButtonAndSelectedProperty() {
        if (optionButtonSelectedProperty != null)
            return;
        if (option.isObligatory() || option.hasNoParent())
            optionButtonSelectedProperty = new SimpleBooleanProperty(option.isObligatory());
        else {
            ToggleGroup toggleGroup = parent == null ? null : parent.getChildrenToggleGroup();
            ChoiceBox<Option> choiceBox = parent == null ? null : parent.childrenChoiceBox;
            if (toggleGroup != null) {
                RadioButton radioButton = new RadioButton();
                radioButton.setToggleGroup(toggleGroup);
                optionButtonSelectedProperty = radioButton.selectedProperty();
                optionButton = radioButton;
            } else if (choiceBox != null) {
                choiceBox.getItems().add(option);
                optionButtonSelectedProperty = new SimpleObjectProperty<Boolean>(false) {
                    @Override
                    protected void invalidated() {
                        if (getValue())
                            choiceBox.getSelectionModel().select(option);
                        //Logger.log(bestTranslationOrName(option) + ": " + getValue());
                    }
                };
                Properties.runOnPropertiesChange(p -> optionButtonSelectedProperty.setValue(p.getValue() == option), choiceBox.getSelectionModel().selectedItemProperty());
                optionButton = null;
            } else {
                CheckBox checkBox = new CheckBox();
                optionButtonSelectedProperty = checkBox.selectedProperty();
                optionButton = checkBox;
                setUserExplicitSelection(false); // A checkbox selection is always explicit and it is not selected at start
            }
            if (optionButton != null) {
                Label promptLabel = option.getPromptLabel();
                Label buttonLabel = promptLabel != null ? promptLabel : Labels.bestLabelOrName(option);
                Labels.translateLabel(optionButton, buttonLabel, getI18n());
            }
        }
        Properties.runOnPropertiesChange(p -> onUiOptionButtonChanged(), optionButtonSelectedProperty);
    }

    private void refreshChildrenChoiceBoxOnLanguageChange() {
        Option selectedItem = childrenChoiceBox.getSelectionModel().getSelectedItem();
        // Resetting the items with an identical duplicated list (to force the ui update)
        childrenChoiceBox.getItems().setAll(new ArrayList<>(childrenChoiceBox.getItems()));
        // The later operation removed the selected item so we restore it
        childrenChoiceBox.getSelectionModel().select(selectedItem);
    }


    /*******************************************************************************************************************
     *  UI => MODEL SYNCHRONIZATION  ***********************************************************************************
     ******************************************************************************************************************/

    private void onUiOptionButtonChanged() {
        if (!thisSyncing)
            syncModelFromUi();
    }

    private void syncModelFromUi() {
        syncModel(isUiOptionSelected(false));
    }

    boolean isUiOptionSelected(boolean checkParents) {
        return optionButtonSelectedProperty == null /* obligatory */ || optionButtonSelectedProperty.getValue()
                && (!checkParents || parent == null || parent.isUiOptionSelected(true));
    }

    private boolean syncModel(boolean selected) {
        try (SyncingContext syncingContext = new SyncingContext()) {
            if (syncingContext.isUserAction())
                setUserExplicitSelection(selected);
            //Logger.log("Syncing model from TreeNode selected = " + selected + (option.getItem() != null ? ", item = " + option.getItem() : ", option = " + option));
            if (selected)
                return addOptionToModelIfNotAlreadyPresent();
            else
                return removeOptionFromModel();
        } // syncingContext.close() implicitly called when exiting try block
    }

    private boolean addOptionToModelIfNotAlreadyPresent() {
        return isModelOptionSelected() || addOptionToModel();
    }

    private boolean addOptionToModel() {
        boolean result = false;
        DateTimeRange optionDateTimeRange = option.getParsedDateTimeRangeOrParent();
        if (optionButtonSelectedProperty != null && optionDateTimeRange != null && option.isNotObligatory() && parent != null && parent.isUserExplicitlySelected() && Booleans.isNotFalse(userExplicitSelection))
            optionButtonSelectedProperty.setValue(userExplicitSelection = true);
        if (option.isConcrete()) {
            modelLine = getWorkingDocumentTransaction().addOption(option);
            result = true;
            syncUiOptionButtonSelected(true);
        }
        if (childrenOptionTreeNodes != null) {
            boolean multipleSelection = isChildMultipleSelectionAllowed();
            boolean singleSelection = !multipleSelection;
            boolean childAdded = false;
            if (singleSelection) {
                if (lastSelectedChildOptionTreeNode != null)
                    childAdded = lastSelectedChildOptionTreeNode.syncModel(true);
            }
            if (multipleSelection || !childAdded && option.getParsedTimeRangeOrParent() != null)
                for (OptionTreeNode childTreeNode: childrenOptionTreeNodes) {
                    childAdded = childTreeNode.syncModel(true);
                    if (childAdded && singleSelection)
                        break;
                }
        }
        return result;
    }

    private boolean removeOptionFromModel() {
        getWorkingDocumentTransaction().removeOption(option);
        modelLine = null;
        if (childrenOptionTreeNodes != null)
            for (OptionTreeNode childTreeNode : childrenOptionTreeNodes)
                childTreeNode.syncModel(false);
        return true;
    }


    /*******************************************************************************************************************
     *  MODEL => UI SYNCHRONIZATION  ***********************************************************************************
     ******************************************************************************************************************/

    private void syncUiFromModel() {
        try (SyncingContext syncingContext = new SyncingContext()) {
            boolean modelSelected = isModelOptionSelected();
            syncUiOptionButtonSelected(modelSelected);
            if (childrenOptionTreeNodes != null)
                for (OptionTreeNode childOptionTreeNode : childrenOptionTreeNodes)
                    childOptionTreeNode.syncUiFromModel();
            updateVisibleProperty();
        } // syncingContext.close() implicitly called when exiting try block
    }

    boolean isModelOptionSelected() {
        if (getWorkingDocumentTransaction().isOptionBooked(option))
            return true;
        if (childrenOptionTreeNodes != null) {
            for (OptionTreeNode childTreeNode: childrenOptionTreeNodes) {
                if (childTreeNode.isModelOptionSelected())
                    return true;
            }
        }
        return false;
    }

    private void syncUiOptionButtonSelected(boolean modelSelected) {
        if (optionButtonSelectedProperty != null && option.isNotObligatory()) { // obligatory options should not be ui updated (ex: Airport transfer section)
            if (modelSelected && topLevelOptionButton != null && userExplicitSelection == null) // When button node is initialized from the model
                userExplicitSelection = true; // we behave as if it was explicitly ticked by the user
            boolean uiSelected = modelSelected || isUserExplicitlySelected();
            //Logger.log("Syncing ui from model uiSelected = " + uiSelected + (option.getItem() != null ? ", item = " + option.getItem().getName() : ", option = " + option.getName()));
            optionButtonSelectedProperty.setValue(uiSelected);
        }
        if (parent != null && modelSelected)
            parent.lastSelectedChildOptionTreeNode = this;
    }

    private void updateVisibleProperty() {
        visibleProperty.set(computeIsVisible());
    }

    private boolean computeIsVisible() {
        DateTimeRange modelDateTimeRange = modelLine != null ? modelLine.getDateTimeRange() : null;
        if (modelDateTimeRange == null) {
            if (parent == null)
                return topLevelOptionSection != null && ((isUserExplicitlySelected() || optionButtonSelectedProperty.getValue()) && hasVisibleOptionBody());
            if (parent.isUserExplicitlyDeselected()) // Ex: node under a deselected checkbox
                return false;
            modelDateTimeRange = WorkingDocumentLine.computeCroppedOptionDateTimeRange(option, getWorkingDocument().getDateTimeRange());
        }
        return modelDateTimeRange == null || !modelDateTimeRange.isEmpty();
    }

    private boolean hasVisibleOptionBody() {
        if (hasVisibleContent(optionBodyPane, this))
            return true;
        if (childrenOptionTreeNodes != null)
            for (OptionTreeNode child : childrenOptionTreeNodes)
                if (child.visibleProperty.get() && child.hasVisibleOptionBody())
                    return true;
        return false;
    }

    private static boolean hasVisibleContent(Pane parent, OptionTreeNode testingNode) {
        if (parent != null)
            for (Node child : parent.getChildren())
                if ((child.isVisible() || child.getProperties().get("visiblePropertyOptionTreeNode") == testingNode) &&
                        (!(child instanceof Pane) || hasVisibleContent((Pane) child, testingNode)))
                    return true;
        return false;
    }

    /*******************************************************************************************************************
     *  SYNCHRONIZATION CONTEXT CLASS  *********************************************************************************
     ******************************************************************************************************************/

    private class SyncingContext implements Closeable {

        private final boolean staticSyncingSnapshot = staticSyncing;
        private final boolean thisSyncingSnapshot = thisSyncing;

        SyncingContext() {
            staticSyncing = thisSyncing = true;
        }

        boolean isUserAction() {
            return !staticSyncingSnapshot;
        }

        @Override
        public void close() {
            staticSyncing = staticSyncingSnapshot;
            thisSyncing = thisSyncingSnapshot;
            tree.deferTransactionCommitAndUiSync();
        }
    }
}
