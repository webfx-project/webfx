package mongoose.frontend.activities.options;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import mongoose.client.businessdata.feesgroup.FeesGroup;
import mongoose.client.controls.bookingcalendar.BookingCalendar;
import mongoose.client.activity.bookingprocess.BookingProcessActivity;
import mongoose.client.businessdata.preselection.OptionsPreselection;
import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.icons.MongooseIcons;
import mongoose.client.controls.sectionpanel.SectionPanelFactory;
import mongoose.frontend.operations.person.RouteToPersonRequest;
import mongoose.shared.entities.Option;
import mongoose.client.entities.util.Labels;
import webfx.extras.flexbox.FlexBox;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.Arrays;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static webfx.framework.client.ui.util.layout.LayoutUtil.setMaxWidthToInfinite;

/**
 * @author Bruno Salmon
 */
public class OptionsActivity extends BookingProcessActivity {

    private FlexBox topLevelOptionButtonsContainer;
    private Node bookingCalendarSection;
    protected Label priceText;
    private WorkingDocument lastWorkingDocument;

    @Override
    public void onPause() {
        super.onPause();
        lastWorkingDocument = getEventActiveWorkingDocument();
    }

    @Override
    protected void startLogic() {
        boolean forceRefresh = true; //getEventOptions() == null; // forcing refresh in case the working document has changed (ex: going back from the personal details after having changed the age)
        onEventFeesGroups().setHandler(ar -> {
            if (ar.failed())
                Logger.log(ar.cause());
            else {
                OptionsPreselection selectedOptionsPreselection = getEventActiveOptionsPreselection();
                WorkingDocument workingDocument = getEventActiveWorkingDocument();
                // Detecting if it's a new booking
                if (workingDocument == null || selectedOptionsPreselection != null && selectedOptionsPreselection.getWorkingDocument() == workingDocument) {
                    // Using no accommodation option by default if no preselection was selected
                    if (selectedOptionsPreselection == null) {
                        selectedOptionsPreselection = findNoAccommodationOptionsPreselection(ar.result());
                        selectedOptionsPreselection.setEventActive();
                    }
                    // Ensuring the working document is a duplication of the preselection one to not alter the original one
                    selectedOptionsPreselection.createNewWorkingDocument(null).setEventActive(); // And make it active
                }
                if (lastWorkingDocument != workingDocument) {
                    if (verticalScrollPane != null)
                        verticalScrollPane.setVvalue(0);
                    optionTree.reset();
                }
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

        topLevelOptionButtonsContainer = new FlexBox(4, 4);
        bookingCalendar = createBookingCalendar();
        bookingCalendar.setOnAttendanceChangedRunnable(optionTree::getUpdatedTopLevelOptionSections);
        bookingCalendarSection = SectionPanelFactory.createBookingCalendarSection(bookingCalendar);

        priceText = new Label();
        priceText.textProperty().bind(bookingCalendar.formattedBookingPriceProperty());
        addPriceText();

        createOrUpdateOptionPanelsIfReady(true);
    }

    protected BookingCalendar createBookingCalendar() {
        return new BookingCalendar(true);
    }

    protected BookingCalendar bookingCalendar;
    private final OptionTree optionTree = new OptionTree(this);

    void createOrUpdateOptionPanelsIfReady(boolean forceRefresh) {
        WorkingDocument workingDocument = getEventActiveWorkingDocument();
        if (workingDocument != null && bookingCalendar != null) {
            bookingCalendar.createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, forceRefresh);

            UiScheduler.runInUiThread(() -> {
                topLevelOptionButtonsContainer.getChildren().setAll(optionTree.getUpdatedTopLevelOptionButtons());
                ObservableList<Node> verticalStackChildren = verticalStack.getChildren();
                verticalStackChildren.setAll(topLevelOptionButtonsContainer);
                verticalStackChildren.addAll(optionTree.getUpdatedTopLevelOptionSections());
                verticalStackChildren.add(bookingCalendarSection);
                verticalStackChildren.add(nextButton);
            });
        }
    }

    protected void addPriceText() {
        priceText.setAlignment(Pos.CENTER);
        pageContainer.setTop(setMaxWidthToInfinite(priceText));
    }

    protected List<Node> createOptionPanelHeaderNodes(Option option, Property<String> i18nTitle) {
        Label label = new Label();
        label.textProperty().bind(i18nTitle);
        return Arrays.asList(MongooseIcons.getItemFamilyIcon16(option), label);
    }

    protected Node createLabelNode(mongoose.shared.entities.Label label) {
        //HtmlText htmlText = new HtmlText();
        Label htmlText = new Label();
        bindTextWithLabel(htmlText.textProperty(), label);
        return htmlText;
    }

    private final Map<mongoose.shared.entities.Label, Property<String>> labelTexts = new HashMap<>();

    private void bindTextWithLabel(Property<String> textProperty, mongoose.shared.entities.Label label) {
        textProperty.bind(Labels.translateLabel(label));
        labelTexts.put(label, textProperty);
    }

    protected void updateLabelText(mongoose.shared.entities.Label label) {
        Property<String> textProperty = labelTexts.get(label);
        if (textProperty != null)
            bindTextWithLabel(textProperty, label);
    }

    @Override
    protected void onNextButtonPressed(ActionEvent event) {
        if (optionTree.getValidationSupport().isValid())
            new RouteToPersonRequest(getEventId(), getHistory()).execute();
    }
}
