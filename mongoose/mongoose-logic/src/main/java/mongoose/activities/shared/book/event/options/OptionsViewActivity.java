package mongoose.activities.shared.book.event.options;

import javafx.beans.property.Property;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.logic.ui.highlevelcomponents.HighLevelComponents;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.ItemFamily;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.commons.util.Arrays;
import naga.commons.util.collection.Collections;
import naga.fx.spi.Toolkit;
import naga.platform.spi.Platform;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        boolean forceRefresh = getEventOptions() == null;
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else
                createOrUpdateOptionPanelsIfReady(forceRefresh);
        });
    }

    protected BookingCalendar createBookingCalendar() {
        return new BookingCalendar(true, getI18n());
    }

    protected BookingCalendar bookingCalendar;

    private void createOrUpdateOptionPanelsIfReady(boolean forceRefresh) {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null && bookingCalendar != null) {
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, forceRefresh);

            List<Option> toLevelOptions = Collections.filter(getEventOptions(), o -> o.getParent() == null);
            toLevelOptions.sort(Comparator.comparingInt(this::optionSectionOrder));

            Toolkit.get().scheduler().runInUiThread(() -> {
                ObservableList<Node> sectionPanels = vBox.getChildren();
                sectionPanels.setAll(toLevelOptions.stream().filter(this::isOptionSectionAboveAttendance).map(this::createTopLevelOptionPanel).collect(Collectors.toList()));
                sectionPanels.add(attendancePanel);
                sectionPanels.addAll(toLevelOptions.stream().filter(this::isOptionSectionBelowAttendance).map(this::createTopLevelOptionPanel).collect(Collectors.toList()));
            });
        }
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        borderPane.setCenter(vBox = new VBox(10));
        vBox.setPadding(new Insets(10));

        bookingCalendar = createBookingCalendar();
        attendancePanel = createAttendancePanel();

        priceText = new Label();
        priceText.textProperty().bind(bookingCalendar.formattedBookingPriceProperty());
        addPriceText();

        createOrUpdateOptionPanelsIfReady(true);
    }

    protected void addPriceText() {
        priceText.setAlignment(Pos.CENTER);
        borderPane.setTop(setMaxWidthToInfinite(priceText));
    }

    private int optionSectionOrder(Option option) {
        return itemFamilySectionOrder(option.getItemFamily());
    }

    private int itemFamilySectionOrder(ItemFamily itemFamily) {
        switch (itemFamily.getItemFamilyType()) {
            case TEACHING: return 0;
            case MEALS: return 1;
            case ACCOMMODATION: return 2;
            case TRANSLATION: return 11;
            case PARKING: return 12;
            case TRANSPORT: return 13;
        }
        return 20;
    }

    private boolean isOptionSectionAboveAttendance(Option option) {
        return optionSectionOrder(option) < 10;
    }

    private boolean isOptionSectionBelowAttendance(Option option) {
        return !isOptionSectionAboveAttendance(option);
    }

    private Node createTopLevelOptionPanel(Option option) {
        return HighLevelComponents.createSectionPanel(null, Collections.toArray(
                createOptionPanelHeaderNodes(
                        option,
                        Labels.translateLabel(Labels.bestLabelOrName(option), getI18n()))
                , Node[]::new));
    }

    protected List<Node> createOptionPanelHeaderNodes(Option option, Property<String> i18nTitle) {
        Label label = new Label();
        label.textProperty().bind(i18nTitle);
        return Arrays.asList(
                createImageView("images/16/itemFamilies/" + option.getItemFamily().getCode() + ".png"),
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

}
