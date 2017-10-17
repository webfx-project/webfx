package mongoose.activities.shared.book.event.options;

import javafx.scene.Node;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Event;
import mongoose.entities.ItemFamily;
import mongoose.entities.Option;
import naga.util.collection.Collections;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;

import java.util.*;

/**
 * @author Bruno Salmon
 */
class OptionTree {

    private final OptionsViewActivity activity;
    private Event event;
    private List<Option> topLevelOptions;
    private Map<Option /*parent*/, Option /*child*/> lastSelectedChildrenOptions = new HashMap<>();

    OptionTree(OptionsViewActivity activity) {
        this.activity = activity;
    }

    OptionsViewActivity getActivity() {
        return activity;
    }

    WorkingDocument getWorkingDocument() {
        return activity.getWorkingDocument();
    }

    I18n getI18n() {
        return activity.getI18n();
    }

    private void clearDataOnEventChange() {
        Event currentEvent = activity.getEvent();
        if (this.event != currentEvent) {
            topLevelOptions = null;
            lastSelectedChildrenOptions.clear();
            this.event = currentEvent;
        }
    }

    private List<Option> getTopLevelOptions() {
        clearDataOnEventChange();
        if (topLevelOptions == null) {
            topLevelOptions = Collections.filter(activity.getEventOptions(), o -> o.getParent() == null);
            //Doesn't work on Android: topLevelOptions.sort(Comparator.comparingInt(this::optionSectionOrder));
            Collections.sort(topLevelOptions, Collections.comparingInt(this::optionSectionOrder));
        }
        return topLevelOptions;
    }

    private List<Option> getTopLevelOptionsAboveAttendance() {
        return Collections.filter(getTopLevelOptions(), this::isOptionSectionAboveAttendance);
    }

    private List<Option> getTopLevelOptionsBelowAttendance() {
        return Collections.filter(getTopLevelOptions(), this::isOptionSectionBelowAttendance);
    }

    List<Node> getUpdatedTopLevelNodesAboveAttendance() {
        return Collections.map(getTopLevelOptionsAboveAttendance(), o -> getOptionTreeNode(o).createOrUpdateNodeFromWorkingDocument());
    }

    List<Node> getUpdatedTopLevelNodesBelowAttendance() {
        return Collections.map(getTopLevelOptionsBelowAttendance(), o -> getOptionTreeNode(o).createOrUpdateNodeFromWorkingDocument());
    }

    private int optionSectionOrder(Option option) {
        return itemFamilySectionOrder(option.getItemFamily());
    }

    private int itemFamilySectionOrder(ItemFamily itemFamily) {
        switch (itemFamily.getItemFamilyType()) {
            case TEACHING: return 0;
            case MEALS: return 1;
            case ACCOMMODATION: return 2;
            case TRANSLATION: return 11;
            case PARKING: return 12;
            case TRANSPORT: return 13;
        }
        return 20;
    }

    private boolean isOptionSectionAboveAttendance(Option option) {
        int order = optionSectionOrder(option);
        return order < 10 && order != 0;
    }

    private boolean isOptionSectionBelowAttendance(Option option) {
        int order = optionSectionOrder(option);
        return order >= 10 && order != 0;
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

    Option getLastSelectedChildOption(Option parent) {
        return lastSelectedChildrenOptions.get(parent);
    }

    void setLastSelectedChildOption(Option parent, Option child) {
        lastSelectedChildrenOptions.put(parent, child);
    }

    private boolean applyingBusinessRulesAndUpdateUi;

    void applyBusinessRulesAndUpdateUi() {
        if (!applyingBusinessRulesAndUpdateUi) {
            applyingBusinessRulesAndUpdateUi = true;
            Toolkit.get().scheduler().scheduleDeferred(() -> {
                getWorkingDocument().applyBusinessRules();
                getActivity().createOrUpdateOptionPanelsIfReady(true);
                applyingBusinessRulesAndUpdateUi = false;
            });
        }
    }

}
