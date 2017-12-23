package mongoose.activities.shared.book.event.program;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import naga.platform.services.log.spi.Logger;

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
        BorderPane calendarPanel = createSectionPanel("{url: 'images/calendar.svg', width: 16, height: 16}", "Timetable");
        VBox panelsVBox = new VBox(calendarPanel);

        borderPane.setCenter(panelsVBox);
        borderPane.setBottom(backButton);

        bookingCalendar = new BookingCalendar(false, getI18n());

        calendarPanel.centerProperty().bind(bookingCalendar.calendarNodeProperty());

        showBookingCalendarIfReady();
    }

    private void showBookingCalendarIfReady() {
        if (bookingCalendar != null && noAccommodationOptionsPreselection != null)
            bookingCalendar.createOrUpdateCalendarGraphicFromOptionsPreselection(noAccommodationOptionsPreselection);
    }

    @Override
    protected void startLogic() {
        onFeesGroups().setHandler(ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
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
