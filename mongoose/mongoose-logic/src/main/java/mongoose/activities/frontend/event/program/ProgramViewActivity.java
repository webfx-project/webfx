package mongoose.activities.frontend.event.program;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.frontend.event.shared.BookingProcessViewActivity;
import mongoose.activities.frontend.event.shared.FeesGroup;
import mongoose.activities.shared.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import naga.framework.ui.i18n.I18n;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class ProgramViewActivity extends BookingProcessViewActivity {

    protected BorderPane calendarPanel;
    protected BorderPane teachingsPanel;
    protected VBox panelsVBox;

    public ProgramViewActivity() {
        super(null);
    }

    @Override
    public void onResume() {
        bindPresentationModelWithLogicNow();
        super.onResume();
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        I18n i18n = getI18n();
        calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", i18n);
        teachingsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n);
        panelsVBox = new VBox(calendarPanel);
        showCalendarIfBothLogicAndViewAreReady();
    }

    @Override
    protected Node assemblyViewNodes() {
        return new BorderPane(panelsVBox, null, null, previousButton, null);
    }

    private CalendarGraphic programCalendarGraphic;

    private void showCalendarIfBothLogicAndViewAreReady() {
        if (programCalendarGraphic != null && calendarPanel != null)
            calendarPanel.setCenter(programCalendarGraphic.getNode());
    }

    private void bindPresentationModelWithLogicNow() {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else {
                programCalendarGraphic = createOrUpdateCalendarGraphicFromOptionsPreselection(findNoAccommodationOptionsPreselection(async.result()), programCalendarGraphic);
                showCalendarIfBothLogicAndViewAreReady();
            }
        });
    }

    private static OptionsPreselection findNoAccommodationOptionsPreselection(FeesGroup[] feesGroups) {
        for (FeesGroup fg : feesGroups) {
            for (OptionsPreselection op : fg.getOptionsPreselections())
                if (!op.hasAccommodation())
                    return op;
        }
        return null;
    }

}
