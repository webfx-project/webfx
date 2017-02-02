package mongoose.activities.shared.book.event.options;

import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
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
        borderPane.setCenter(createAttendancePanel());

        Text priceText = new Text();
        priceText.textProperty().bind(bookingCalendar.formattedBookingPriceProperty());
        priceText.setManaged(false);
        priceText.setTextOrigin(VPos.TOP);
        priceText.setTextAlignment(TextAlignment.RIGHT);
        priceText.wrappingWidthProperty().bind(borderPane.widthProperty());
        borderPane.getChildren().add(priceText);

        showBookingCalendarIfReady();
    }

    protected BookingCalendar createBookingCalendar() {
        return new BookingCalendar(true, getI18n());
    }

    protected BookingCalendar bookingCalendar;

    private void showBookingCalendarIfReady() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null && bookingCalendar != null)
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, false);
    }

    private BorderPane createAttendancePanel() {
        bookingCalendar = createBookingCalendar();
        return createSectionPanel("{url: 'images/calendar.svg', width: 16, height: 16}",
                "Attendance",
                bookingCalendar.calendarNodeProperty());
    }

    private BorderPane createSectionPanel(String iconImageUrl, String translationKey, ObservableValue<Node> centerProperty) {
        BorderPane sectionPanel = createSectionPanel(iconImageUrl, translationKey);
        sectionPanel.centerProperty().bind(centerProperty);
        return sectionPanel;
    }

    private BorderPane createSectionPanel(String iconImageUrl, String translationKey) {
        return HighLevelComponents.createSectionPanel(null, iconImageUrl, translationKey, getI18n());
    }

}
