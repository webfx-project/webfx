package mongoose.activities.shared.book.event.options;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.commons.util.collection.Collections;
import naga.framework.ui.i18n.I18n;
import naga.fx.properties.Properties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
class OptionTreeNode {

    private final Option option;
    private final OptionTree tree;
    private final OptionTreeNode parent;
    private Node node;
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
        sectionPanel.setCenter(createPanelBodyNode());
        return sectionPanel;
    }

    private List<Node> createOptionPanelHeaderNodes(Property<String> i18nTitle) {
        return tree.getActivity().createOptionPanelHeaderNodes(option, i18nTitle);
    }

    private Property<Boolean> optionButtonSelectedProperty;

    private Node createPanelBodyNode() {
        VBox vBox = new VBox();
        mongoose.entities.Label topLabel = option.getTopLabel();
        ObservableList<Node> children = vBox.getChildren();
        if (topLabel != null)
            children.add(tree.getActivity().createLabelNode(topLabel));
        if (!option.isObligatory()) {
            ToggleGroup toggleGroup = parent == null ? null : parent.getChildrenToggleGroup();
            ButtonBase optionButton;
            if (toggleGroup == null) {
                CheckBox checkBox = new CheckBox();
                optionButton = checkBox;
                optionButtonSelectedProperty = checkBox.selectedProperty();
            } else {
                RadioButton radioButton = new RadioButton();
                radioButton.setToggleGroup(toggleGroup);
                optionButton = radioButton;
                optionButtonSelectedProperty = radioButton.selectedProperty();
            }
            mongoose.entities.Label buttonLabel = option.getPromptLabel();
            if (buttonLabel == null)
                buttonLabel = Labels.bestLabel(option);
            if (buttonLabel != null)
                Labels.translateLabel(optionButton, buttonLabel, getI18n());
            children.add(optionButton);
            Properties.runOnPropertiesChange(p -> onOptionButtonChanged(), optionButtonSelectedProperty);
        }
        //if ("select".equals(option.getLayout()))
        childrenToggleGroup = option.isChildrenRadio() ? new ToggleGroup() : null;
        List<Option> childrenOptions = getChildrenOptions(option);
        if (!Collections.isEmpty(childrenOptions)) {
            childrenOptionTreeNodes = childrenOptions.stream().map(o -> new OptionTreeNode(o, this)).collect(Collectors.toList());
            vBox.getChildren().addAll(childrenOptionTreeNodes.stream().map(OptionTreeNode::createPanelBodyNode).collect(Collectors.toList()));
        }
        return vBox;
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
        getWorkingDocument().getWorkingDocumentLines().removeIf(wdl -> isOptionBookedInWorkingDocumentLine(wdl, option));
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
