package mongoose.activities.shared.book.event.program;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18n;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class ProgramViewActivity extends BookingProcessViewActivity {

    private BookingCalendar bookingCalendar;
    private OptionsPreselection noAccommodationOptionsPreselection;

    public ProgramViewActivity() {
        super(null);
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        I18n i18n = getI18n();
        BorderPane calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable", i18n);
        //BorderPane teachingsPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Teachings", i18n);
        VBox panelsVBox = new VBox(calendarPanel);

        borderPane.setCenter(panelsVBox);
        borderPane.setBottom(previousButton);

        bookingCalendar = new BookingCalendar(false, i18n);

        calendarPanel.centerProperty().bind(bookingCalendar.calendarNodeProperty());

        showBookingCalendarIfReady();
    }

    private void showBookingCalendarIfReady() {
        if (bookingCalendar != null && noAccommodationOptionsPreselection != null)
            bookingCalendar.createOrUpdateCalendarGraphicFromOptionsPreselection(noAccommodationOptionsPreselection);
    }

    @Override
    public void onResume() {
        startLogic();
        super.onResume();
    }

    private void startLogic() {
        onFeesGroups().setHandler(ar -> {
            if (ar.failed())
                Platform.log(ar.cause());
            else {
                noAccommodationOptionsPreselection = findNoAccommodationOptionsPreselection(ar.result());
                showBookingCalendarIfReady();
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
