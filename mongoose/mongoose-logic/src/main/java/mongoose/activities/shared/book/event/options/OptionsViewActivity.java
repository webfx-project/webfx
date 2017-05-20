package mongoose.activities.shared.book.event.options;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.commons.util.Arrays;
import naga.framework.ui.controls.ImageViewUtil;
import naga.framework.ui.controls.LayoutUtil;
import naga.fx.spi.Toolkit;
import naga.platform.spi.Platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static naga.framework.ui.controls.LayoutUtil.setMaxWidthToInfinite;

/**
 * @author Bruno Salmon
 */
public class OptionsViewActivity extends BookingProcessViewActivity {

    private VBox vBox;
    private Node attendancePanel;
    protected Label priceText;

    public OptionsViewActivity() {
        super("person");
    }

    @Override
    public void onResume() {
        super.onResume();
        startLogic();
    }

    protected void startLogic() {
        boolean forceRefresh = true; //getEventOptions() == null; // forcing refresh in case the working document has changed (ex: going back from the personal details after having changed the age)
        onFeesGroups().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else
                createOrUpdateOptionPanelsIfReady(forceRefresh);
        });
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        borderPane.setCenter(LayoutUtil.createVerticalScrollPane(vBox = new VBox(10)));

        bookingCalendar = createBookingCalendar();
        attendancePanel = createAttendancePanel();

        priceText = new Label();
        priceText.textProperty().bind(bookingCalendar.formattedBookingPriceProperty());
        addPriceText();

        createOrUpdateOptionPanelsIfReady(true);
    }

    protected BookingCalendar createBookingCalendar() {
        return new BookingCalendar(true, getI18n());
    }

    protected BookingCalendar bookingCalendar;
    private final OptionTree optionTree = new OptionTree(this);

    protected void createOrUpdateOptionPanelsIfReady(boolean forceRefresh) {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null && bookingCalendar != null) {
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, forceRefresh);

            Toolkit.get().scheduler().runInUiThread(() -> {
                ObservableList<Node> sectionPanels = vBox.getChildren();
                sectionPanels.setAll(optionTree.getUpdatedTopLevelNodesAboveAttendance());
                sectionPanels.add(attendancePanel);
                sectionPanels.addAll(optionTree.getUpdatedTopLevelNodesBelowAttendance());
            });
        }
    }

    protected void addPriceText() {
        priceText.setAlignment(Pos.CENTER);
        borderPane.setTop(setMaxWidthToInfinite(priceText));
    }

    protected List<Node> createOptionPanelHeaderNodes(Option option, Property<String> i18nTitle) {
        Label label = new Label();
        label.textProperty().bind(i18nTitle);
        return Arrays.asList(
                ImageViewUtil.createImageView("images/16/itemFamilies/" + option.getItemFamily().getCode() + ".png"),
                label
        );
    }

    private Node createAttendancePanel() {
        return createSectionPanel("images/16/itemFamilies/attendance.png",
                "Attendance",
                bookingCalendar.calendarNodeProperty());
    }

    private Node createSectionPanel(String iconImageUrl, String translationKey, ObservableValue<Node> centerProperty) {
        BorderPane sectionPanel = createSectionPanel(iconImageUrl, translationKey);
        sectionPanel.centerProperty().bind(centerProperty);
        return sectionPanel;
    }

    private BorderPane createSectionPanel(String iconImageUrl, String translationKey) {
        return HighLevelComponents.createSectionPanel(null, iconImageUrl, translationKey, getI18n());
    }

    protected Node createLabelNode(mongoose.entities.Label label) {
        //HtmlText htmlText = new HtmlText();
        Label htmlText = new Label();
        bindTextWithLabel(htmlText.textProperty(), label);
        return htmlText;
    }

    private Map<mongoose.entities.Label, Property<String>> labelTexts = new HashMap<>();

    private void bindTextWithLabel(Property<String> textProperty, mongoose.entities.Label label) {
        textProperty.bind(Labels.translateLabel(label, getI18n()));
        labelTexts.put(label, textProperty);
    }

    protected void updateLabelText(mongoose.entities.Label label) {
        Property<String> textProperty = labelTexts.get(label);
        if (textProperty != null)
            bindTextWithLabel(textProperty, label);
    }
}
