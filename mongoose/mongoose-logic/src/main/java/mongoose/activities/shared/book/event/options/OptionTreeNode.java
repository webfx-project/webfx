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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
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
    private BorderPane buttonNode;
    private BorderPane detailedNode;
    private ChoiceBox<Option> childrenChoiceBox;
    private ToggleGroup childrenToggleGroup;
    private List<OptionTreeNode> childrenOptionTreeNodes;
    private OptionTreeNode lastSelectedChildOptionTreeNode;
    private final BooleanProperty visibleProperty = new SimpleBooleanProperty(true);
    private static boolean staticSyncing;
    private boolean thisSyncing;

    private OptionTreeNode(Option option, OptionTree tree, OptionTreeNode parent) {
        this.option = option;
        this.tree = tree;
        this.parent = parent;
        tree.registerOptionTreeNode(this);
    }

    private OptionTreeNode(Option option, OptionTreeNode parent) {
        this(option, parent.tree, parent);
    }

    OptionTreeNode(Option option, OptionTree tree) {
        this(option, tree, null);
    }

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

    Node createOrUpdateButtonNodeFromModel() {
        if (buttonNode == null)
            createButtonNode();
        syncUiFromModel();
        return buttonNode;
    }

    private void createButtonNode() {
        buttonNode = createTopLevelOptionPanel(false);
        buttonNode.setOnMouseClicked(e -> optionButtonSelectedProperty.setValue(!optionButtonSelectedProperty.getValue()));
        buttonNode.setCursor(Cursor.HAND);
        ImageView checkBoxView = new ImageView();
        checkBoxView.imageProperty().bind(Properties.compute(optionButtonSelectedProperty, selected ->
                ImageStore.getOrCreateImage(selected ? "images/16/checked.png" : "images/16/unchecked.png", 16, 16)));
        ObservableList<Node> hBoxChildren = ((HBox) buttonNode.getTop()).getChildren();
        hBoxChildren.add(0, checkBoxView);
        hBoxChildren.add(0, LayoutUtil.createHGrowable()); // To shift the button content and make it centered
        LayoutUtil.setMinWidthToPref(buttonNode);
        LayoutUtil.setMaxSizeToInfinite(buttonNode);
    }

    Node createOrUpdateDetailedNodeFromModel() {
        if (detailedNode == null)
            detailedNode = createTopLevelOptionPanel(true);
        syncUiFromModel();
        return detailedNode;
    }

    private BorderPane createTopLevelOptionPanel(boolean detailed) {
        BorderPane sectionPanel = HighLevelComponents.createSectionPanel(null, Collections.toArray(
                createOptionPanelHeaderNodes(Labels.translateLabel(Labels.bestLabelOrName(option), getI18n()))
                , Node[]::new));
        createOptionButtonAndSelectedProperty();
        if (detailed) {
            Pane panelBodyNode = createPanelBodyNode();
            if (panelBodyNode != null) {
                panelBodyNode.setPadding(new Insets(20));
                sectionPanel.setCenter(panelBodyNode);
                LayoutUtil.setUnmanagedWhenInvisible(sectionPanel, visibleProperty);
            }
        }
        return sectionPanel;
    }

    private static boolean hasVisibleContent(Pane parent) {
        for (Node child : parent.getChildren())
            if (child.isVisible() && (!(child instanceof Pane) || hasVisibleContent((Pane) child)))
                return true;
        return false;
    }

    private void updateVisibleProperty() {
        visibleProperty.set(computeIsVisible());
    }

    private boolean computeIsVisible() {
        if (option.hasNoParent())
            return detailedNode != null && (isUserExplicitlySelected() || optionButtonSelectedProperty.getValue() && hasVisibleContent((Pane) detailedNode.getCenter()));
        DateTimeRange optionDateTimeRange = option.getParsedDateTimeRangeOrParent();
        if (parent != null && optionDateTimeRange == null && parent.optionButtonSelectedProperty != null && !parent.optionButtonSelectedProperty.getValue())
            return false;
        if (!dateTimeRangeOverlapsWorkingDocument(optionDateTimeRange))
            return false;
        return true;
    }

    private boolean optionDateTimeRangeOverlapsWorkingDocument() {
        return dateTimeRangeOverlapsWorkingDocument(option.getParsedDateTimeRange());
    }

    private boolean dateTimeRangeOverlapsWorkingDocument(DateTimeRange dateTimeRange) {
        return dateTimeRange == null || getWorkingDocumentTransaction().getWorkingDocument().getDateTimeRange().overlaps(dateTimeRange);
    }

    private List<Node> createOptionPanelHeaderNodes(Property<String> i18nTitle) {
        return tree.getActivity().createOptionPanelHeaderNodes(option, i18nTitle);
    }

    private Node createLabelNode(Label label) {
        return tree.getActivity().createLabelNode(label);
    }

    private ButtonBase optionButton;
    private Property<Boolean> optionButtonSelectedProperty;
    private Boolean userExplicitSelection; /* Set to non null value to allow the ui to be temporary
     desynchronized from the model during a transitional state. Ex: The booker ticked the translation checkbox but hasn't
     yet selected the language. This is a transitional state because the translation option can't yet be added to the
     model (as no language selected) but it is necessary to keep the checkbox ticked in the ui to let the booker select
     the language. Once selected, this language selection causes the translation option to be added to the model so this
     flag should be set back to false because the ui and model are synchronized again.*/

    private void setUserExplicitSelection(Boolean value) {
        userExplicitSelection = value;
    }

    private boolean isUserExplicitlySelected() {
        return Booleans.isTrue(userExplicitSelection);
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
            }
            if (optionButton != null) {
                Label promptLabel = option.getPromptLabel();
                Label buttonLabel = promptLabel != null ? promptLabel : Labels.bestLabelOrName(option);
                Labels.translateLabel(optionButton, buttonLabel, getI18n());
            }
        }
        Properties.runOnPropertiesChange(p -> onUiOptionButtonChanged(), optionButtonSelectedProperty);
    }

    private Pane createPanelBodyNode() {
        createOptionButtonAndSelectedProperty();
        if (optionButton == null && option.isNotObligatory() && option.hasParent())
            return null;
        VBox vBox = new VBox();
        if (parent != null && parent.parent != null && parent.parent.childrenToggleGroup != null) // If under a radio button (ex: Ecommoy shuttle)
            vBox.setPadding(new Insets(0, 0, 0, 20)); // Adding a left padding
        mongoose.entities.Label topLabel = option.getTopLabel();
        ObservableList<Node> vBoxChildren = vBox.getChildren();
        if (topLabel != null)
            vBoxChildren.add(createLabelNode(topLabel));
        if (optionButton != null)
            vBoxChildren.add(optionButton);
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
                    selectNode =  new HBox(10, createLabelNode(childrenPromptLabel), selectNode);
                vBoxChildren.add(selectNode);
                LayoutUtil.setUnmanagedWhenInvisible(selectNode, visibleProperty);
                Properties.runOnPropertiesChange(p -> refreshChildrenChoiceBoxOnLanguageChange(), getI18n().languageProperty());
            } else if (option.isChildrenRadio())
                childrenToggleGroup = new ToggleGroup();
            //Doesn't work on Android: childrenOptionTreeNodes = childrenOptions.stream().map(o -> new OptionTreeNode(o, this)).collect(Collectors.toList());
            childrenOptionTreeNodes = Collections.map(childrenOptions, o -> new OptionTreeNode(o, this));
            //Doesn't work on Android: vBoxChildren.addAll(childrenOptionTreeNodes.stream().map(OptionTreeNode::createPanelBodyNode).filter(Objects::nonNull).collect(Collectors.toList()));
            vBoxChildren.addAll(Collections.mapFilter(childrenOptionTreeNodes, OptionTreeNode::createPanelBodyNode, Objects::nonNull));
        }
        if (option.hasParent())
            LayoutUtil.setUnmanagedWhenInvisible(vBox, visibleProperty);
        return vBox;
    }

    private void refreshChildrenChoiceBoxOnLanguageChange() {
        Option selectedItem = childrenChoiceBox.getSelectionModel().getSelectedItem();
        // Resetting the items with an identical duplicated list (to force the ui update)
        childrenChoiceBox.getItems().setAll(new ArrayList<>(childrenChoiceBox.getItems()));
        // The later operation removed the selected item so we restore it
        childrenChoiceBox.getSelectionModel().select(selectedItem);
    }

    private void onUiOptionButtonChanged() {
        if (!thisSyncing)
            syncModelFromUi(false);
    }

    private void syncModelFromUi(boolean unselectedParent) {
        try (SyncingContext syncingContext = new SyncingContext()) {
            boolean uiSelected = !unselectedParent && isUiOptionSelected(false);
            if (syncingContext.isUserAction())
                setUserExplicitSelection(uiSelected);
            //Logger.log("Syncing model from TreeNode uiSelected = " + uiSelected + (option.getItem() != null ? ", item = " + option.getItem() : ", option = " + option));
            if (uiSelected)
                addOptionToModelIfNotAlreadyPresent();
            else
                removeOptionFromModel();
            if (childrenOptionTreeNodes != null)
                for (OptionTreeNode childOptionTreeNode : childrenOptionTreeNodes)
                    childOptionTreeNode.syncModelFromUi(!uiSelected);
        }
    }

    private void addOptionToModelIfNotAlreadyPresent() {
        if (!isModelOptionSelected())
            addOptionToModel();
    }

    private void addOptionToModel() {
        if (option.isConcrete()) {
            getWorkingDocumentTransaction().addOption(option);
            syncUiOptionButtonSelected(true);
        }
        if (childrenOptionTreeNodes != null) {
            boolean childAdded = false;
            for (OptionTreeNode childTreeNode: childrenOptionTreeNodes) {
                Option childOption = childTreeNode.option;
                boolean addingChild = childOption.isObligatory();
                if (!addingChild && lastSelectedChildOptionTreeNode == null && isUserExplicitlySelected() && Booleans.isNotFalse(childTreeNode.userExplicitSelection)) {
                    DateTimeRange childOptionTimeRange = childOption.getParsedDateTimeRangeOrParent();
                    if (childOptionTimeRange != null) {
                        if (childTreeNode.optionButtonSelectedProperty != null)
                            childTreeNode.optionButtonSelectedProperty.setValue(childTreeNode.userExplicitSelection = true);
                        addingChild = dateTimeRangeOverlapsWorkingDocument(childOptionTimeRange);
                    }
                }
                if (addingChild) {
                    childTreeNode.addOptionToModelIfNotAlreadyPresent();
                    childAdded = true;
                }
            }
            if (!childAdded) {
                if (lastSelectedChildOptionTreeNode != null)
                    lastSelectedChildOptionTreeNode.addOptionToModelIfNotAlreadyPresent();
                else if (optionButtonSelectedProperty != null && optionButtonSelectedProperty.getValue())
                    setUserExplicitSelection(true);
            }
        }
    }

    private void removeOptionFromModel() {
        getWorkingDocumentTransaction().removeOption(option);
        if (childrenOptionTreeNodes != null)
            for (OptionTreeNode childTreeNode : childrenOptionTreeNodes)
                childTreeNode.removeOptionFromModel();
    }

    private List<Option> getChildrenOptions(Option parent) {
        return tree.getActivity().getChildrenOptions(parent);
    }


    private void syncUiFromModel() {
        try (SyncingContext syncingContext = new SyncingContext()) {
            boolean modelSelected = isModelOptionSelected();
            syncUiOptionButtonSelected(modelSelected);
            if (childrenOptionTreeNodes != null /*&& modelSelected*/)
                for (OptionTreeNode childOptionTreeNode : childrenOptionTreeNodes)
                    childOptionTreeNode.syncUiFromModel();
            updateVisibleProperty();
        }
    }

    private void syncUiOptionButtonSelected(boolean modelSelected) {
        if (optionButtonSelectedProperty != null && option.isNotObligatory()) { // obligatory options should not be ui updated (ex: Airport transfer section)
            if (modelSelected && buttonNode != null && userExplicitSelection == null) // When button node is initialized from the model
                userExplicitSelection = true; // we behave as if it was explicitly ticked by the user
            boolean uiSelected = modelSelected || isUserExplicitlySelected();
            //Logger.log("Syncing ui from model uiSelected = " + uiSelected + (option.getItem() != null ? ", item = " + option.getItem().getName() : ", option = " + option.getName()));
            optionButtonSelectedProperty.setValue(uiSelected);
        }
        if (parent != null && modelSelected)
            parent.lastSelectedChildOptionTreeNode = this;
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

    boolean isUiOptionSelected(boolean checkParents) {
        return optionButtonSelectedProperty == null /* obligatory */ || optionButtonSelectedProperty.getValue()
                && (!checkParents || parent == null || parent.isUiOptionSelected(true));
    }

    void reset() {
        userExplicitSelection = null;
        //Collections.forEach(childrenOptionTreeNodes, OptionTreeNode::reset);
    }

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
