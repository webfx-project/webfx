package mongoose.client.controls.sectionpanel;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import mongoose.client.controls.bookingcalendar.BookingCalendar;
import mongoose.client.icons.MongooseIcons;
import webfx.framework.client.services.i18n.I18n;
import webfx.framework.client.ui.util.background.BackgroundUtil;
import webfx.framework.client.ui.util.border.BorderUtil;
import webfx.fxkit.extra.cell.collator.NodeCollatorRegistry;

import static webfx.framework.client.ui.util.image.JsonImageViews.createImageView;

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
        BorderPane sectionPanel = new BorderPane();
        sectionPanel.getStyleClass().add("section-panel");
        sectionPanel.setBorder(BorderUtil.newBorder(Color.grayRgb(0x0d), 5, 1));
        sectionPanel.setBackground(BackgroundUtil.WHITE_BACKGROUND);
        return setSectionPanelHeaderNodes(sectionPanel, headerNodes);
    }

    public static BorderPane setSectionPanelHeaderNodes(BorderPane sectionPanel, Node... headerNodes) {
        return setSectionPanelHeaderNode(sectionPanel, NodeCollatorRegistry.hBoxCollator().collateNodes(headerNodes));
    }

    public static BorderPane setSectionPanelHeaderNode(BorderPane sectionPanel, Node headerNode) {
        if (headerNode instanceof Region) {
            Region headerRegion = (Region) headerNode;
            headerRegion.setBackground(BackgroundUtil.newVerticalLinearGradientBackground("0xF0F0F0", "0xE0E0E0",5));
            headerRegion.setMinHeight(40d);
            headerRegion.setPadding(new Insets(0, 10, 0 , 10));
        }
        sectionPanel.setTop(headerNode);
        return sectionPanel;
    }
}
