package mongoose.client.sectionpanel;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import mongoose.client.bookingcalendar.BookingCalendar;
import mongoose.client.icons.MongooseIcons;
import webfx.framework.services.i18n.I18n;
import webfx.framework.ui.util.background.BackgroundUtil;
import webfx.framework.ui.util.border.BorderUtil;
import webfx.fxkits.extra.cell.collator.NodeCollatorRegistry;

import static webfx.framework.ui.util.image.JsonImageViews.createImageView;

/**
 * @author Bruno Salmon
 */
public final class SectionPanelFactory {

    public static BorderPane createSectionPanel(String i18nKey) {
        return createSectionPanel(null, i18nKey);
    }

    public static Node createSectionPanel(String iconImageUrl, String i18nKey, ObservableValue<Node> centerProperty) {
        BorderPane sectionPanel = createSectionPanel(iconImageUrl, i18nKey);
        sectionPanel.centerProperty().bind(centerProperty);
        return sectionPanel;
    }

    public static BorderPane createSectionPanel(String i18nKey, Node center) {
        BorderPane sectionPanel = createSectionPanel(i18nKey);
        sectionPanel.setCenter(center);
        return sectionPanel;
    }

    public static Node createBookingCalendarSection(BookingCalendar bookingCalendar) {
        return createSectionPanel(MongooseIcons.attendanceIcon16JsonUrl,
                "Attendance",
                bookingCalendar.calendarNodeProperty());
    }

    public static BorderPane createSectionPanel() {
        return new BorderPane();
    }

    public static BorderPane createSectionPanel(String iconImageUrl, String translationKey) {
        BorderPane panel = new BorderPane();
        panel.getStyleClass().add("section-panel");
        panel.setBorder(BorderUtil.newBorder(Color.grayRgb(0x0d), 5, 1));
        panel.setBackground(BackgroundUtil.WHITE_BACKGROUND);
        HBox hBox = (HBox) NodeCollatorRegistry.hBoxCollator().collateNodes(createImageView(iconImageUrl), I18n.translateText(new Label(), translationKey));
        hBox.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("0xF0F0F0", "0xE0E0E0",5));
        hBox.setMinHeight(40d);
        hBox.setPadding(new Insets(0, 10, 0 , 10));
        panel.setTop(hBox);
        return panel;
    }

    public static BorderPane createSectionPanel(Node... headerNodes) {
        BorderPane panel = new BorderPane();
        panel.getStyleClass().add("section-panel");
        panel.setBorder(BorderUtil.newBorder(Color.grayRgb(0x0d), 5, 1));
        panel.setBackground(BackgroundUtil.WHITE_BACKGROUND);
        HBox hBox = (HBox) NodeCollatorRegistry.hBoxCollator().collateNodes(headerNodes);
        hBox.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("0xF0F0F0", "0xE0E0E0",5));
        hBox.setMinHeight(40d);
        hBox.setPadding(new Insets(0, 10, 0 , 10));
        panel.setTop(hBox);
        return panel;
    }

    public static Button createBookButton() {
        return createToBottomGradientButton("#7fd504", "#2a8236");
    }

    public static Button createSoldoutButton() {
        return createToBottomGradientButton("#e92c04", "#853416");
    }

    public static Button createToBottomGradientButton(String topWebColor, String bottomWebColor) {
        return createButton(topWebColor + " 0%, " + bottomWebColor + " 100%", bottomWebColor);
    }

    public static Button createButton(String linearGradient, String borderWebColor) {
        Button button = new Button();
        button.setBackground(BackgroundUtil.newLinearGradientBackground(linearGradient, 5));
        button.setBorder(BorderUtil.newBorder(Color.web(borderWebColor), 5));
        button.setTextFill(Color.WHITE);
        button.setStyle("-fx-text-fill: white;");
        return button;
    }
}
