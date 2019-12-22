package mongoose.frontend.activities.options;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businessdata.workingdocument.WorkingDocumentLine;
import mongoose.client.businessdata.workingdocument.WorkingDocumentTransaction;
import mongoose.client.icons.MongooseIcons;
import mongoose.client.controls.sectionpanel.SectionPanelFactory;
import mongoose.shared.entities.Label;
import mongoose.shared.entities.Option;
import mongoose.shared.businessdata.time.DateTimeRange;
import mongoose.client.entities.util.Labels;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.client.ui.controls.entity.selector.EntityButtonSelector;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.kit.util.properties.Properties;
import webfx.extras.imagestore.ImageStore;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Objects;
import webfx.platform.shared.util.collection.Collections;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
final class OptionTreeNode {

    private final Option option;
    private final OptionTree tree;
    private final OptionTreeNode parent;
    private BorderPane topLevelOptionButton;
    private BorderPane topLevelOptionSection;
    private VBox optionBodyPane;
    private ButtonBase optionButton;
    private BooleanProperty optionButtonSelectedProperty;
    private EntityButtonSelector<Option> childrenOptionSelector;
    private ToggleGroup childrenToggleGroup;
    private boolean childrenToggleGroupValidationInitialized;
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
        return childrenToggleGroup == null && childrenOptionSelector == null;
    }

    private boolean canOptionSelectionBeChanged() {
        return option.isNotObligatory() && option.hasParent();
    }

    private boolean canOptionButtonSelectionBeChanged() {
        return optionButtonSelectedProperty != null && option.isNotObligatory();
    }

    private void setOptionButtonSelected(boolean selected) {
        optionButtonSelectedProperty.set(selected);
    }

    private boolean isOptionButtonSelected() {
        return optionButtonSelectedProperty == null /* happens when obligatory */ || optionButtonSelectedProperty.get();
    }

    void reset() { // Called when working with a completely new working document
        userExplicitSelection = null;
        modelLine = null;
        lastSelectedChildOptionTreeNode = null;
        if (childrenOptionSelector != null)
            childrenOptionSelector.setSelectedItem(null);
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
        // Creating the button (actually a BorderPane) with the option name and icon
        topLevelOptionButton = createTopLevelOptionSection(false);
        topLevelOptionButton.setOnMouseClicked(e -> setOptionButtonSelected(!isOptionButtonSelected()));
        topLevelOptionButton.setCursor(Cursor.HAND);
        ImageView checkBoxView = new ImageView();
        checkBoxView.setFitWidth(16d);
        checkBoxView.setFitHeight(16d);
        checkBoxView.imageProperty().bind(Properties.compute(optionButtonSelectedProperty, selected ->
                ImageStore.getOrCreateImage(selected ? MongooseIcons.checkedIcon16Url : MongooseIcons.uncheckedIcon16Url)));
        ObservableList<Node> hBoxChildren = ((HBox) topLevelOptionButton.getTop()).getChildren();
        // Adding the checkbox before the already present icon and text
        hBoxChildren.add(0, checkBoxView);
        // Making the button centered by surrounding the children with 2 HGrowables
        hBoxChildren.add(0, LayoutUtil.createHGrowable()); // One in first position
        hBoxChildren.add(LayoutUtil.createHGrowable()); // One in last position
        // Setting min width to pref to avoid short button with ellipsis text (the flexbox will rearrange buttons instead)
        LayoutUtil.setMinWidthToPref(topLevelOptionButton);
        //LayoutUtil.setMaxSizeToInfinite(topLevelOptionButton);
    }

    Node createOrUpdateTopLevelOptionSectionFromModel() {
        if (topLevelOptionSection == null)
            topLevelOptionSection = createTopLevelOptionSection(true);
        syncUiFromModel();
        return topLevelOptionSection;
    }

    private BorderPane createTopLevelOptionSection(boolean detailed) {
        BorderPane sectionPanel = SectionPanelFactory.createSectionPanelWithHeaderNodes(Collections.toArray(
                createOptionPanelHeaderNodes(Labels.translateLabel(Labels.bestLabelOrName(option)))
                , Node[]::new));
        createOptionButtonAndSelectedProperty();
        if (detailed) {
            createOptionBodyPane();
            if (optionBodyPane != null) {
                LayoutUtil.setMinWidth(optionBodyPane, 0);
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
        // If this node is actually "inside" a button selector, there is no need for a body pane
        if (optionButton == null && parent != null && parent.childrenOptionSelector != null)
            return optionBodyPane = null;
        optionBodyPane = new VBox();
        optionBodyPane.setFillWidth(false);
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
                OptionsActivity activity = tree.getActivity();
                childrenOptionSelector = new EntityButtonSelector<>(
                        // Note: translationOption() expression function has been registered in OptionTree constructor
                        "{class: 'Option', columns: ['translateOption(this)'], where: 'parent=" + option.getPrimaryKey() + "'}",
                        activity,
                        () -> (Pane) activity.getNode(), // passing the parent getter for a future access because it is not immediately available (since we haven't yet finished building the activity UI)
                        activity.getDataSourceModel());
                childrenOptionSelector.setRestrictedFilterList(new ArrayList<>());
                Node selectNode = childrenOptionSelector.toMaterialButton(null, Labels.translateLabel(option.getChildrenPromptLabel()));
                optionBodyChildren.add(selectNode);
                bindToVisibleProperty(selectNode);
                Properties.runOnPropertiesChange(childrenOptionSelector::updateButtonContentOnNewSelectedItem, I18n.languageProperty());
                tree.getValidationSupport().addRequiredInput(childrenOptionSelector.selectedItemProperty(), childrenOptionSelector.getButton());
            } else if (option.isChildrenRadio())
                childrenToggleGroup = new ToggleGroup();
            childrenOptionTreeNodes = Collections.map(childrenOptions, o -> new OptionTreeNode(o, this));
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
        if (optionButtonSelectedProperty != null) // Already done!
            return; // So don't do it again
        if (!canOptionSelectionBeChanged())
            optionButtonSelectedProperty = new SimpleBooleanProperty(true);
        else {
            ToggleGroup toggleGroup = parent != null ? parent.getChildrenToggleGroup() : null;
            EntityButtonSelector<Option> optionSelector = parent != null ? parent.childrenOptionSelector : null;
            if (toggleGroup != null) {
                RadioButton radioButton = new RadioButton();
                radioButton.setToggleGroup(toggleGroup);
                optionButtonSelectedProperty = radioButton.selectedProperty();
                optionButton = radioButton;
                if (!parent.childrenToggleGroupValidationInitialized) {
                    tree.getValidationSupport().addRequiredInput(toggleGroup.selectedToggleProperty(), radioButton);
                    parent.childrenToggleGroupValidationInitialized = true;
                }
            } else if (optionSelector != null) {
                optionSelector.getRestrictedFilterList().add(option);
                optionButtonSelectedProperty = new SimpleBooleanProperty(false) {
                    @Override
                    protected void invalidated() {
                        if (get())
                            optionSelector.setSelectedItem(option);
                        //Logger.log(bestTranslationOrName(option) + ": " + getValue());
                    }
                };
                Properties.runOnPropertiesChange(p -> setOptionButtonSelected(p.getValue() == option), optionSelector.selectedItemProperty());
                optionButton = null;
            } else {
                CheckBox checkBox = new CheckBox();
                optionButtonSelectedProperty = checkBox.selectedProperty();
                optionButton = checkBox;
                setUserExplicitSelection(false); // A checkbox selection is always explicit and it is not selected at application
                LayoutUtil.setUnmanagedWhenInvisible(checkBox);
            }
            if (optionButton != null) {
                Label promptLabel = option.getPromptLabel();
                Label buttonLabel = promptLabel != null ? promptLabel : Labels.bestLabelOrName(option);
                Labels.translateLabel(optionButton, buttonLabel);
            }
        }
        Properties.runOnPropertiesChange(this::onUiOptionButtonChanged, optionButtonSelectedProperty);
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
        return isOptionButtonSelected() && (!checkParents || parent == null || parent.isUiOptionSelected(true));
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
        return isOptionSelectedInModel() || addOptionToModel();
    }

    private boolean addOptionToModel() {
        boolean result = false;
        DateTimeRange optionDateTimeRange = option.getParsedDateTimeRangeOrParent();
        if (canOptionButtonSelectionBeChanged() && optionDateTimeRange != null && parent != null && parent.isUserExplicitlySelected() && Booleans.isNotFalse(userExplicitSelection))
            setOptionButtonSelected(userExplicitSelection = true);
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
            boolean modelSelected = isOptionSelectedInModel();
            syncUiOptionButtonSelected(modelSelected);
            if (childrenOptionTreeNodes != null)
                for (OptionTreeNode childOptionTreeNode : childrenOptionTreeNodes)
                    childOptionTreeNode.syncUiFromModel();
            updateVisibleProperty();
        } // syncingContext.close() implicitly called when exiting try block
    }

    boolean isOptionSelectedInModel() {
        return getWorkingDocumentTransaction().isOptionBooked(option)
               || Collections.anyMatch(childrenOptionTreeNodes, OptionTreeNode::isOptionSelectedInModel);
    }

    private void syncUiOptionButtonSelected(boolean modelSelected) {
        if (canOptionButtonSelectionBeChanged()) { // obligatory options should not be ui updated (ex: Airport transfer section)
            if (modelSelected && topLevelOptionButton != null && userExplicitSelection == null) // When button node is initialized from the model
                userExplicitSelection = true; // we behave as if it was explicitly ticked by the user
            boolean uiSelected = modelSelected || isUserExplicitlySelected();
            //Logger.log("Syncing ui from model uiSelected = " + uiSelected + (option.getItem() != null ? ", item = " + option.getItem().getName() : ", option = " + option.getName()));
            setOptionButtonSelected(uiSelected);
        }
        if (parent != null && modelSelected)
            parent.lastSelectedChildOptionTreeNode = this;
    }

    private void updateVisibleProperty() {
        boolean visible = computeIsVisible();
        visibleProperty.set(visible);
        // Also hiding checkbox if it is the only one applicable whereas the booker already pressed the top level option button
        if (visible && parent != null && parent.topLevelOptionButton != null && optionButton instanceof CheckBox)
            optionButton.setVisible(Collections.anyMatch(parent.childrenOptionTreeNodes, this::isSiblingWithVisibleCheckbox));
    }

    private boolean isSiblingWithVisibleCheckbox(OptionTreeNode sibling) {
        return sibling != this && sibling.optionButton instanceof CheckBox && sibling.computeIsVisible();
    }

    private boolean computeIsVisible() {
        if (parent == null)
            return topLevelOptionSection != null && ((isUserExplicitlySelected() || isOptionButtonSelected()) && hasVisibleOptionBody());
        if (parent.isUserExplicitlyDeselected()) // Ex: node under a deselected checkbox
            return false;
        DateTimeRange modelDateTimeRange = modelLine != null ? modelLine.getDateTimeRange() : null;
        if (modelDateTimeRange == null)
            modelDateTimeRange = WorkingDocumentLine.computeCroppedOptionDateTimeRange(option, getWorkingDocument().getDateTimeRange());
        return modelDateTimeRange == null || !modelDateTimeRange.isEmpty();
    }

    private boolean hasVisibleOptionBody() {
        return hasPaneVisibleContent(optionBodyPane)
            || Collections.anyMatch(childrenOptionTreeNodes, OptionTreeNode::isVisibleAndHasVisibleOptionBody);
    }

    private boolean isVisibleAndHasVisibleOptionBody() {
        return visibleProperty.get() && hasVisibleOptionBody();
    }

    private boolean hasPaneVisibleContent(Pane pane) {
        return pane != null && Collections.anyMatch(pane.getChildren(), this::isNodeVisibleAndHasVisibleContent);
    }

    private boolean isNodeVisibleAndHasVisibleContent(Node node) {
        return (node.isVisible() || node.getProperties().get("visiblePropertyOptionTreeNode") == this)
                && (!(node instanceof Pane) || hasPaneVisibleContent((Pane) node));
    }

    /*******************************************************************************************************************
     *  SYNCHRONIZATION CONTEXT CLASS  *********************************************************************************
     ******************************************************************************************************************/

    private final class SyncingContext implements Closeable {

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
