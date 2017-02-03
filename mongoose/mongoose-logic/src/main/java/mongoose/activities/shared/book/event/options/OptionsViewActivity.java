package mongoose.activities.shared.book.event.options;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.platform.spi.Platform;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bruno Salmon
 */
public class OptionsViewActivity extends BookingProcessViewActivity {

    private VBox vBox;

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
        borderPane.setCenter(vBox = new VBox(20));

        bookingCalendar = createBookingCalendar();

        List<Option> toLevelOptions = Collections.filter(getEventOptions(), o -> o.getParent() == null);

        vBox.getChildren().addAll(toLevelOptions.stream().map(this::createTopLevelOptionPanel).collect(Collectors.toList()));

        vBox.getChildren().add(createAttendancePanel());

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
        if (workingDocument != null && bookingCalendar != null) {
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, false);
        }
    }

    private Node createTopLevelOptionPanel(Option option) {
        return createItemFamilyPanel(option.getItemFamily().getCode(), Labels.translateLabel(Labels.bestLabelOrName(option), getI18n()));
    }

    private Node createItemFamilyPanel(String familyCode, Property<String> i18nTitle) {
        return HighLevelComponents.createSectionPanel(null, Collections.toArray(createItemFamilyPanelHeaderNodes(familyCode, i18nTitle), Node[]::new));
    }

    protected List<Node> createItemFamilyPanelHeaderNodes(String familyCode, Property<String> i18nTitle) {
        Text text = new Text();
        text.textProperty().bind(i18nTitle);
        return Arrays.asList(
                createImageView("images/16/itemFamilies/" + familyCode + ".png"),
                text
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

}
