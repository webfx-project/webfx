package mongoose.activities.backend.book.shared;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.time.DayTimeRange;
import mongoose.activities.shared.logic.ui.calendargraphic.impl.DayColumnBodyBlockViewModel;
import mongoose.activities.shared.logic.ui.calendargraphic.impl.DayColumnHeaderViewModel;
import naga.util.function.BiConsumer;
import naga.framework.ui.graphic.controls.dialog.DialogCallback;
import naga.framework.ui.graphic.controls.dialog.DialogUtil;
import naga.framework.ui.graphic.controls.dialog.GridPaneBuilder;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
class DayTimeRangeEditor {

    static void showDayTimeRangeEditorDialog(DayTimeRange dayTimeRange, long epochDay, CalendarTimeline timeline, BiConsumer<DayTimeRange, DialogCallback> okConsumer, Node parentOwner, I18n i18n) {
        showDayTimeRangeInternDialog(dayTimeRange, epochDay, timeline, okConsumer, parentOwner, i18n);
    }

    private static void showDayTimeRangeInternDialog(DayTimeRange dayTimeRange, long epochDay, CalendarTimeline timeline, BiConsumer<DayTimeRange, DialogCallback> okConsumer, Node parentOwner, I18n i18n) {
        DayTimeRange.TimeRangeRule generalRule = dayTimeRange.getGeneralRule();
        DayTimeRange.TimeRangeRule ruleForDay = dayTimeRange.getRuleForDay(epochDay);

        String generalText = generalRule.getDayTimeSeries().toText();
        String exceptionText = ruleForDay.getDayTimeSeries().toText();

        TextField generalTextField = new TextField(generalText);
        CheckBox exceptionCheckBox = new CheckBox();
        TextField exceptionTextField = new TextField(exceptionText);
        Button okButton = new Button();
        Button cancelButton = new Button();

        boolean hasException = !exceptionText.equals(generalText);
        exceptionCheckBox.setSelected(hasException);

        DialogCallback dialogCallback = DialogUtil.showModalNodeInGoldLayout(new GridPaneBuilder(i18n)
                        .addNodeFillingRow(new DayColumnBodyBlockViewModel(null, epochDay, generalRule.getDayTimeInterval(), timeline, true).getNode(), 2)
                        .addLabelTextInputRow("Hours", generalTextField)
                        .addNodeFillingRow(20, new DayColumnHeaderViewModel(epochDay, i18n).getNode())
                        .addCheckBoxTextInputRow("Exception", exceptionCheckBox, exceptionTextField)
                        .addButtons("Ok", okButton, "Cancel", cancelButton)
                        .build(),
                (Pane) parentOwner);

        okButton.setOnAction(e -> {
            String newGeneralText = generalTextField.getText();
            String newExceptionText = exceptionTextField.getText();
            DayTimeRange newDayTimeRange = dayTimeRange.changeGeneralRule(newGeneralText);
            boolean newHasException = exceptionCheckBox.isSelected() && !newExceptionText.equals(newGeneralText);
            if (newHasException != hasException || newHasException && !newExceptionText.equals(exceptionText)) {
                if (newHasException)
                    newDayTimeRange = newDayTimeRange.addExceptionRuleForDay(epochDay, newExceptionText);
                else
                    newDayTimeRange = newDayTimeRange.removeExceptionRuleForDay(epochDay);
            }
            okConsumer.accept(newDayTimeRange, dialogCallback);
        });
        cancelButton.setOnAction(e -> dialogCallback.closeDialog());
    }

}
