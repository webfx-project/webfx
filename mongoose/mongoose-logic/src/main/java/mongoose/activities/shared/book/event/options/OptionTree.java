package mongoose.activities.shared.book.event.options;

import javafx.scene.Node;
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
class OptionTree {

    private final OptionsViewActivity activity;
    private Event event;
    private List<Option> topLevelOptions;
    private WorkingDocumentTransaction workingDocumentTransaction;

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
        if (workingDocumentTransaction == null || workingDocumentTransaction.getWorkingDocument() != workingDocument)
            workingDocumentTransaction = new WorkingDocumentTransaction(workingDocument);
        return workingDocumentTransaction;
    }

    I18n getI18n() {
        return activity.getI18n();
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

    List<Node> getUpdatedTopLevelNodesAboveAttendance() {
        return Collections.map(getTopLevelNonObligatoryOptions(), this::getUpdatedOptionButtonNode);
    }

    List<Node> getUpdatedTopLevelNodesBelowAttendance() {
        return Collections.map(getTopLevelOptions(), this::getUpdatedOptionDetailedNode);
    }

    private Node getUpdatedOptionButtonNode(Option o) {
        return getOptionTreeNode(o).createOrUpdateButtonNodeFromModel();
    }

    private Node getUpdatedOptionDetailedNode(Option o) {
        return getOptionTreeNode(o).createOrUpdateDetailedNodeFromModel();
    }

    private Map<Option, OptionTreeNode> optionTreeNodes = new HashMap<>();

    private OptionTreeNode getOptionTreeNode(Option option) {
        //Doesn't work on Android: return optionTreeNodes.computeIfAbsent(option, this::newOptionTreeNode);
        OptionTreeNode optionTreeNode = optionTreeNodes.get(option);
        if (optionTreeNode == null)
            optionTreeNodes.put(option, optionTreeNode = newOptionTreeNode(option));
        return optionTreeNode;
    }

    private OptionTreeNode newOptionTreeNode(Option option) {
        return new OptionTreeNode(option, this);
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

    public void reset() {
        Collections.forEach(optionTreeNodes.values(), OptionTreeNode::reset);
    }
}
