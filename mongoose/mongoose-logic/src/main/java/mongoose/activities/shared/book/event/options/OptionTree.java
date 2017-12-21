package mongoose.activities.shared.book.event.options;

import javafx.scene.Node;
import mongoose.activities.shared.logic.ui.validation.MongooseValidationSupport;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.transaction.WorkingDocumentTransaction;
import mongoose.entities.Event;
import mongoose.entities.Option;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;
import naga.util.collection.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class OptionTree {

    private final OptionsViewActivity activity;
    private Event event;
    private List<Option> topLevelOptions;
    private WorkingDocumentTransaction workingDocumentTransaction;
    private final MongooseValidationSupport validationSupport = new MongooseValidationSupport();

    OptionTree(OptionsViewActivity activity) {
        this.activity = activity;
    }

    OptionsViewActivity getActivity() {
        return activity;
    }

    WorkingDocument getWorkingDocument() {
        return activity.getWorkingDocument();
    }

    WorkingDocumentTransaction getWorkingDocumentTransaction() {
        WorkingDocument workingDocument = getWorkingDocument();
        workingDocument.setOptionTree(this);
        if (workingDocumentTransaction == null || workingDocumentTransaction.getWorkingDocument() != workingDocument)
            workingDocumentTransaction = new WorkingDocumentTransaction(workingDocument);
        return workingDocumentTransaction;
    }

    I18n getI18n() {
        return activity.getI18n();
    }

    MongooseValidationSupport getValidationSupport() {
        return validationSupport;
    }

    private void clearDataOnEventChange() {
        Event currentEvent = activity.getEvent();
        if (this.event != currentEvent) {
            topLevelOptions = null;
            this.event = currentEvent;
        }
    }

    private List<Option> getTopLevelOptions() {
        clearDataOnEventChange();
        if (topLevelOptions == null)
            topLevelOptions = Collections.filter(activity.getEventOptions(), Option::hasNoParent);
        return topLevelOptions;
    }

    private List<Option> getTopLevelNonObligatoryOptions() {
        return Collections.filter(getTopLevelOptions(), Option::isNotObligatory);
    }

    List<Node> getUpdatedTopLevelOptionButtons() {
        return Collections.map(getTopLevelNonObligatoryOptions(), this::getUpdatedTopLevelOptionButton);
    }

    List<Node> getUpdatedTopLevelOptionSections() {
        return Collections.map(getTopLevelOptions(), this::getUpdatedTopLevelOptionSection);
    }

    private Node getUpdatedTopLevelOptionButton(Option o) {
        return getOptionTreeNode(o).createOrUpdateTopLevelOptionButtonFromModel();
    }

    private Node getUpdatedTopLevelOptionSection(Option o) {
        return getOptionTreeNode(o).createOrUpdateTopLevelOptionSectionFromModel();
    }

    private Map<Option, OptionTreeNode> optionTreeNodes = new HashMap<>();

    private OptionTreeNode getOptionTreeNode(Option option) {
        OptionTreeNode optionTreeNode = optionTreeNodes.get(option);
        if (optionTreeNode == null)
            optionTreeNode = new OptionTreeNode(option, this);
        return optionTreeNode;
    }

    void registerOptionTreeNode(OptionTreeNode optionTreeNode) { // Called by OptionTreeNode constructor
        optionTreeNodes.put(optionTreeNode.getOption(), optionTreeNode);
    }

    public boolean isOptionSelected(Option option) {
        OptionTreeNode optionTreeNode = optionTreeNodes.get(option);
        return optionTreeNode != null && (optionTreeNode.isModelOptionSelected() || optionTreeNode.isUiOptionSelected(true));
    }

    private boolean pendingTransactionCommitAndUiSync;

    void deferTransactionCommitAndUiSync() {
        if (!pendingTransactionCommitAndUiSync) {
            pendingTransactionCommitAndUiSync = true;
            Toolkit.get().scheduler().scheduleDeferred(() -> {
                getWorkingDocumentTransaction().commit();
                getActivity().createOrUpdateOptionPanelsIfReady(true);
                pendingTransactionCommitAndUiSync = false;
            });
        }
    }

    void reset() {
        Collections.forEach(optionTreeNodes.values(), OptionTreeNode::reset);
    }
}
