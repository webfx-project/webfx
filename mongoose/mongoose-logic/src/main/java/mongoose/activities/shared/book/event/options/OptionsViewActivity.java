package mongoose.activities.shared.book.event.options;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.activities.shared.book.event.shared.BookingProcessViewActivity;
import mongoose.activities.shared.book.event.shared.FeesGroup;
import mongoose.activities.shared.logic.preselection.OptionsPreselection;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.entities.Option;
import mongoose.util.Labels;
import naga.framework.ui.controls.ImageViewUtil;
import naga.framework.ui.layouts.FlexBox;
import naga.framework.ui.layouts.LayoutUtil;
import naga.fx.spi.Toolkit;
import naga.platform.services.log.spi.Logger;
import naga.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static naga.framework.ui.layouts.LayoutUtil.setMaxWidthToInfinite;

/**
 * @author Bruno Salmon
 */
public class OptionsViewActivity extends BookingProcessViewActivity {

    private VBox vBox;
    private FlexBox primaryOptionsFlowPane;
    private Node attendancePanel;
    protected Label priceText;
    private WorkingDocument lastWorkingDocument;

    public OptionsViewActivity() {
        super("person");
    }

    @Override
    public void onResume() {
        super.onResume();
        startLogic();
    }

    @Override
    public void onPause() {
        super.onPause();
        lastWorkingDocument = getWorkingDocument();

    }

    protected void startLogic() {
        boolean forceRefresh = true; //getEventOptions() == null; // forcing refresh in case the working document has changed (ex: going back from the personal details after having changed the age)
        onFeesGroups().setHandler(ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
            else {
                OptionsPreselection selectedOptionsPreselection = getSelectedOptionsPreselection();
                WorkingDocument workingDocument = getWorkingDocument();
                // Detecting if it's a new booking
                if (workingDocument == null || selectedOptionsPreselection != null && selectedOptionsPreselection.getWorkingDocument() == workingDocument) {
                    // Using no accommodation option by default if no preselection was selected
                    if (selectedOptionsPreselection == null)
                        setSelectedOptionsPreselection(selectedOptionsPreselection = findNoAccommodationOptionsPreselection(ar.result()));
                    // Ensuring the working document is a duplication of the preselection one to not alter the original one
                    setWorkingDocument(selectedOptionsPreselection.createNewWorkingDocument(null));
                }
                if (lastWorkingDocument != workingDocument)
                    optionTree.reset();
                createOrUpdateOptionPanelsIfReady(forceRefresh);
            }
        });
    }

    private OptionsPreselection findNoAccommodationOptionsPreselection(FeesGroup[] feesGroups) {
        for (FeesGroup feesGroup : feesGroups) {
            OptionsPreselection noAccommodationPreselection = Arrays.findFirst(feesGroup.getOptionsPreselections(), op -> !op.hasAccommodation());
            if (noAccommodationPreselection != null)
                return noAccommodationPreselection;
        }
        return null;
    }

    @Override
    protected void createViewNodes() {
        super.createViewNodes();
        borderPane.setCenter(LayoutUtil.createVerticalScrollPaneWithPadding(vBox = new VBox(10)));

        primaryOptionsFlowPane = new FlexBox();
        primaryOptionsFlowPane.setHorizontalSpace(4);
        primaryOptionsFlowPane.setVerticalSpace(4);
        bookingCalendar = createBookingCalendar();
        bookingCalendar.setOnAttendanceChangedRunnable(optionTree::getUpdatedTopLevelNodesBelowAttendance);
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

    void createOrUpdateOptionPanelsIfReady(boolean forceRefresh) {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null && bookingCalendar != null) {
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, forceRefresh);

            Toolkit.get().scheduler().runInUiThread(() -> {
                ObservableList<Node> sectionPanels = vBox.getChildren();
                primaryOptionsFlowPane.getChildren().setAll(optionTree.getUpdatedTopLevelNodesAboveAttendance());
                sectionPanels.setAll(primaryOptionsFlowPane);
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
                ImageViewUtil.createImageView("images/16/itemFamilies/" + option.getItemFamilyCode() + ".png"),
                label
        );
    }

    private Node createAttendancePanel() {
        return createSectionPanel("images/16/itemFamilies/attendance.png",
                "Attendance",
                bookingCalendar.calendarNodeProperty());
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
