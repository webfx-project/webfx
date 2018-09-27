package mongoose.client.activities.generic;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import mongoose.client.activities.shared.BookingCalendar;
import mongoose.client.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.shared.actions.MongooseIcons;

/**
 * @author Bruno Salmon
 */
public interface MongooseSectionFactoryMixin {

    default BorderPane createSectionPanel(String i18nKey) {
        return HighLevelComponents.createSectionPanel(null, null, i18nKey);
    }

    default Node createSectionPanel(String iconImageUrl, String i18nKey, ObservableValue<Node> centerProperty) {
        BorderPane sectionPanel = createSectionPanel(iconImageUrl, i18nKey);
        sectionPanel.centerProperty().bind(centerProperty);
        return sectionPanel;
    }

    default Node createSectionPanel(String i18nKey, Node center) {
        BorderPane sectionPanel = createSectionPanel(i18nKey);
        sectionPanel.setCenter(center);
        return sectionPanel;
    }

    default BorderPane createSectionPanel(String iconImageUrl, String i18nKey) {
        return HighLevelComponents.createSectionPanel(null, iconImageUrl, i18nKey);
    }

    default Node createBookingCalendarSection(BookingCalendar bookingCalendar) {
        return createSectionPanel(MongooseIcons.attendanceIcon16JsonUrl,
                "Attendance",
                bookingCalendar.calendarNodeProperty());
    }
}
