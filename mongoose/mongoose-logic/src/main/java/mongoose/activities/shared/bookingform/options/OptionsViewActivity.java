package mongoose.activities.shared.bookingform.options;

import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import mongoose.activities.shared.bookingform.shared.BookingCalendar;
import mongoose.activities.shared.bookingform.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class OptionsViewActivity extends BookingProcessViewActivity {

    public OptionsViewActivity() {
        super("person");
    }

    @Override
    public void onResume() {
        startLogic();
        super.onResume();
    }

    private void startLogic() {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else
                showBookingCalendarIfReady();
        });
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        BorderPane calendarPanel = HighLevelComponents.createSectionPanel(null, "{url: 'images/calendar.svg', width: 16, height: 16}", "Attendance", getI18n());
        Text priceText = new Text();

        borderPane.setCenter(calendarPanel);

        bookingCalendar = new BookingCalendar(true, getI18n(), borderPane);

        calendarPanel.setTop(priceText);
        calendarPanel.centerProperty().bind(bookingCalendar.calendarNodeProperty());
        priceText.textProperty().bind(bookingCalendar.formattedBookingPriceProperty());

        showBookingCalendarIfReady();
    }

    private BookingCalendar bookingCalendar;

    private void showBookingCalendarIfReady() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null && bookingCalendar != null)
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument);
    }

}
