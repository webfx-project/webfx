package mongoose.frontend.activities.program;

import javafx.scene.layout.BorderPane;
import mongoose.client.businesslogic.fees.FeesGroup;
import mongoose.client.bookingcalendar.BookingCalendar;
import mongoose.client.bookingprocess.activity.BookingProcessActivity;
import mongoose.client.businesslogic.preselection.OptionsPreselection;
import mongoose.client.icons.MongooseIcons;
import mongoose.client.sectionpanel.SectionPanelFactory;
import webfx.framework.client.ui.layouts.LayoutUtil;
import webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
final class ProgramActivity extends BookingProcessActivity {

    private BookingCalendar bookingCalendar;
    private OptionsPreselection noAccommodationOptionsPreselection;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        bookingCalendar = new BookingCalendar(false);
        BorderPane calendarSection = SectionPanelFactory.createSectionPanel(MongooseIcons.calendarMonoSvg16JsonUrl, "Timetable");
        calendarSection.centerProperty().bind(bookingCalendar.calendarNodeProperty());
        verticalStack.getChildren().setAll(calendarSection, LayoutUtil.setMaxWidthToInfinite(backButton));
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
