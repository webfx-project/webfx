package mongoose.activities.shared.generic;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import mongoose.actions.MongooseIcons;
import mongoose.activities.shared.book.shared.BookingCalendar;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import naga.framework.ui.i18n.I18nMixin;

/**
 * @author Bruno Salmon
 */
public interface MongooseSectionFactoryMixin extends I18nMixin {

    default BorderPane createSectionPanel(String i18nKey) {
        return HighLevelComponents.createSectionPanel(null, null, i18nKey, getI18n());
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
        return HighLevelComponents.createSectionPanel(null, iconImageUrl, i18nKey, getI18n());
    }

    default Node createBookingCalendarSection(BookingCalendar bookingCalendar) {
        return createSectionPanel(MongooseIcons.attendanceIcon16JsonUrl,
                "Attendance",
                bookingCalendar.calendarNodeProperty());
    }
}
