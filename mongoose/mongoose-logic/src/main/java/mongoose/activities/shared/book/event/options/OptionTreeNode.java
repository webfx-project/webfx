package mongoose.activities.shared.book.event.options;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.commons.util.collection.Collections;
import naga.framework.ui.controls.LayoutUtil;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
class OptionTreeNode {

    private final Option option;
    private final OptionTree tree;
    private final OptionTreeNode parent;
    private Node node;
    private ChoiceBox<Label> childrenChoiceBox;
    private ToggleGroup childrenToggleGroup;
    private List<OptionTreeNode> childrenOptionTreeNodes;

    private OptionTreeNode(Option option, OptionTree tree, OptionTreeNode parent) {
        this.option = option;
        this.tree = tree;
        this.parent = parent;
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

    public Node getNode() {
        return node;
    }

    private ToggleGroup getChildrenToggleGroup() {
        return childrenToggleGroup;
    }

    protected I18n getI18n() {
        return tree.getI18n();
    }

    private WorkingDocument getWorkingDocument() {
        return tree.getWorkingDocument();
    }

    Node createOrUpdateNodeFromWorkingDocument() {
        if (node == null)
            node = createTopLevelOptionPanel();
        updateNodeFromWorkingDocument();
        return node;
    }

    private Node createTopLevelOptionPanel() {
        BorderPane sectionPanel = HighLevelComponents.createSectionPanel(null, Collections.toArray(
                createOptionPanelHeaderNodes(Labels.translateLabel(Labels.bestLabelOrName(option), getI18n()))
                , Node[]::new));
        Region panelBodyNode = createPanelBodyNode();
        panelBodyNode.setPadding(new Insets(20));
        sectionPanel.setCenter(panelBodyNode);
        return sectionPanel;
    }

    private List<Node> createOptionPanelHeaderNodes(Property<String> i18nTitle) {
        return tree.getActivity().createOptionPanelHeaderNodes(option, i18nTitle);
    }

    private Node createLabelNode(Label label) {
        return tree.getActivity().createLabelNode(label);
    }

    private Property<Boolean> optionButtonSelectedProperty;

    private Region createPanelBodyNode() {
        VBox vBox = new VBox();
        if (parent != null && parent.parent != null && parent.parent.childrenToggleGroup != null) // If under a radio button (ex: Ecommoy shuttle)
            vBox.setPadding(new Insets(0, 0, 0, 20)); // Adding a left padding
        mongoose.entities.Label topLabel = option.getTopLabel();
        ObservableList<Node> vBoxChildren = vBox.getChildren();
        if (topLabel != null)
            vBoxChildren.add(createLabelNode(topLabel));
        if (!option.isObligatory()) {
            Label promptLabel = option.getPromptLabel();
            Label buttonLabel = promptLabel != null ? promptLabel : Labels.bestLabelOrName(option);
            ButtonBase optionButton = null;
            ToggleGroup toggleGroup = parent == null ? null : parent.getChildrenToggleGroup();
            ChoiceBox<Label> childrenChoiceBox = parent == null ? null : parent.childrenChoiceBox;
            if (toggleGroup != null) {
                RadioButton radioButton = new RadioButton();
                radioButton.setToggleGroup(toggleGroup);
                optionButtonSelectedProperty = radioButton.selectedProperty();
                optionButton = radioButton;
            } else if (childrenChoiceBox != null) {
                childrenChoiceBox.getItems().add(buttonLabel);
                optionButtonSelectedProperty = new SimpleBooleanProperty() {
                    @Override
                    protected void invalidated() {
                        if (getValue())
                            childrenChoiceBox.getSelectionModel().select(buttonLabel);
                    }
                };
                Properties.runOnPropertiesChange(p -> optionButtonSelectedProperty.setValue(p.getValue() == buttonLabel), childrenChoiceBox.getSelectionModel().selectedItemProperty());
                vBox = null;
            } else {
                CheckBox checkBox = new CheckBox();
                optionButtonSelectedProperty = checkBox.selectedProperty();
                optionButton = checkBox;
            }
            if (optionButton != null)
                vBoxChildren.add(Labels.translateLabel(optionButton, buttonLabel, getI18n()));
            Properties.runOnPropertiesChange(p -> onOptionButtonChanged(), optionButtonSelectedProperty);
        }
        List<Option> childrenOptions = getChildrenOptions(option);
        if (vBox != null && !Collections.isEmpty(childrenOptions)) {
            if ("select".equals(option.getLayout())) {
                childrenChoiceBox = new ChoiceBox<>();
                childrenChoiceBox.setConverter(new StringConverter<Label>() {
                    @Override
                    public String toString(Label label) {
                        return Labels.instantTranslateLabel(label, getI18n());
                    }

                    @Override
                    public Label fromString(String string) {
                        return null;
                    }
                });
                Label childrenPromptLabel = option.getChildrenPromptLabel();
                if (childrenPromptLabel == null)
                    vBoxChildren.add(childrenChoiceBox);
                else {
                    Node labelNode = createLabelNode(childrenPromptLabel);
                    HBox hBox = new HBox(10, labelNode, childrenChoiceBox);
                    vBoxChildren.add(hBox);
                }
                Properties.runOnPropertiesChange(p -> refreshChildrenChoiceBoxOnLanguageChange(), getI18n().languageProperty());
            } else if (option.isChildrenRadio())
                childrenToggleGroup = new ToggleGroup();
            //Doesn't work on Android: childrenOptionTreeNodes = childrenOptions.stream().map(o -> new OptionTreeNode(o, this)).collect(Collectors.toList());
            childrenOptionTreeNodes = Collections.convert(childrenOptions, o -> new OptionTreeNode(o, this));
            //Doesn't work on Android: vBox.getChildren().addAll(childrenOptionTreeNodes.stream().map(OptionTreeNode::createPanelBodyNode).filter(Objects::nonNull).collect(Collectors.toList()));
            vBox.getChildren().addAll(Collections.convertFilter(childrenOptionTreeNodes, OptionTreeNode::createPanelBodyNode, naga.commons.util.Objects::nonNull));
        }
        if (parent != null && parent.optionButtonSelectedProperty != null)
            LayoutUtil.setUnmanagedWhenInvisible(vBox, parent.optionButtonSelectedProperty);
        return vBox;
    }

    private void refreshChildrenChoiceBoxOnLanguageChange() {
        Label selectedItem = childrenChoiceBox.getSelectionModel().getSelectedItem();
        // Resetting the items with an identical duplicated list (to force the ui update)
        childrenChoiceBox.getItems().setAll(new ArrayList<>(childrenChoiceBox.getItems()));
        // The later operation removed the selected item so we restore it
        childrenChoiceBox.getSelectionModel().select(selectedItem);
    }

    private void onOptionButtonChanged() {
        if (!updatingNodeFromWorkingDocument)
            updateWorkingDocumentFromNode(false);
    }

    private void updateWorkingDocumentFromNode(boolean unselectedParent) {
        boolean selected = !unselectedParent && (optionButtonSelectedProperty == null /* obligatory */ || optionButtonSelectedProperty.getValue());
        if (selected)
            addOptionToWorkingDocument();
        else
            removeOptionFromWorkingDocument();
        if (childrenOptionTreeNodes != null)
            for (OptionTreeNode childOptionTreeNode : childrenOptionTreeNodes)
                childOptionTreeNode.updateWorkingDocumentFromNode(!selected);
        tree.applyBusinessRulesAndUpdateUi();
    }

    private void addOptionToWorkingDocument() {
        if (!isWorkingOptionSelected(option))
            addOptionToWorkingDocument(option);
    }

    private void addOptionToWorkingDocument(Option option) {
        if (option.getItem() != null) {
            WorkingDocument workingDocument = getWorkingDocument();
            workingDocument.getWorkingDocumentLines().add(new WorkingDocumentLine(option, workingDocument));
        }
        boolean childAdded = false;
        for (Option childOption: getChildrenOptions(option))
            if (childOption.isObligatory()) {
                addOptionToWorkingDocument(childOption);
                childAdded = true;
            }
        if (!childAdded) {
            Option childOption = tree.getLastSelectedChildOption(option);
            if (childOption != null)
                addOptionToWorkingDocument(childOption);
        }
    }


    private void removeOptionFromWorkingDocument() {
        removeOptionFromWorkingDocument(option);
    }

    private void removeOptionFromWorkingDocument(Option option) {
        Option parent = option.getParent();
        if (parent != null)
            tree.setLastSelectedChildOption(parent, option);
        //Doesn't work on Android: getWorkingDocument().getWorkingDocumentLines().removeIf(wdl -> isOptionBookedInWorkingDocumentLine(wdl, option));
        Collections.removeIf(getWorkingDocument().getWorkingDocumentLines(), wdl -> isOptionBookedInWorkingDocumentLine(wdl, option));
        for (Option childOption: getChildrenOptions(option))
            removeOptionFromWorkingDocument(childOption);
    }

    private List<Option> getChildrenOptions(Option parent) {
        return tree.getActivity().getChildrenOptions(parent);
    }

    private boolean updatingNodeFromWorkingDocument;

    private void updateNodeFromWorkingDocument() {
        boolean selected = isWorkingOptionSelected(option);
        if (optionButtonSelectedProperty != null) {
            updatingNodeFromWorkingDocument = true;
            optionButtonSelectedProperty.setValue(selected);
            updatingNodeFromWorkingDocument = false;
        }
        if (childrenOptionTreeNodes != null && selected)
            for (OptionTreeNode childOptionTreeNode : childrenOptionTreeNodes)
                childOptionTreeNode.updateNodeFromWorkingDocument();
    }

    private boolean isWorkingOptionSelected(Option option) {
        if (Collections.findFirst(getWorkingDocument().getWorkingDocumentLines(), wdl -> isOptionBookedInWorkingDocumentLine(wdl, option)) != null)
            return true;
        if (option.isFolder()) {
            for (Option childOption: getChildrenOptions(option)) {
                if (isWorkingOptionSelected(childOption))
                    return true;
            }
        }
        return false;
    }

    private boolean isOptionBookedInWorkingDocumentLine(WorkingDocumentLine wdl, Option option) {
        Option wdlOption = wdl.getOption();
        return wdlOption != null ? wdlOption == option : wdl.getSite() == option.getSite() && wdl.getItem() == option.getItem();
    }
}
