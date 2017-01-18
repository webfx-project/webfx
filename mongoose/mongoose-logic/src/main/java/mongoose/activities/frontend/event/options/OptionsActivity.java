package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessActivity;
import mongoose.activities.shared.logic.calendar.CalendarTimeline;
import mongoose.activities.shared.logic.calendar.graphic.CalendarCell;
import mongoose.activities.shared.logic.calendar.graphic.CalendarClickEvent;
import mongoose.activities.shared.logic.calendar.graphic.CalendarGraphic;
import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.time.TimeInterval;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Option;
import naga.framework.orm.entity.UpdateStore;
import naga.fx.properties.Properties;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Window;
import naga.platform.spi.Platform;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
public class OptionsActivity extends BookingProcessActivity<OptionsViewModel, OptionsPresentationModel> {

    public OptionsActivity() {
        super(OptionsPresentationModel::new, "person");
        registerViewBuilder(getClass(), new OptionsViewModelBuilder());
    }

    private OptionsViewModel optionsViewModel;
    private CalendarGraphic workingDocumentCalendarGraphic;

    @Override
    protected void initializePresentationModel(OptionsPresentationModel optionsPresentationModel) {
        super.initializePresentationModel(optionsPresentationModel);
        createAndShowCalendarIfBothLogicAndViewAreReady();
    }

    @Override
    protected void bindViewModelWithPresentationModel(OptionsViewModel vm, OptionsPresentationModel pm) {
        super.bindViewModelWithPresentationModel(vm, pm);
        this.optionsViewModel = vm;
        showCalendarIfBothLogicAndViewAreReady();
    }

    private void createAndShowCalendarIfBothLogicAndViewAreReady() {
        WorkingDocument workingDocument = getWorkingDocument();
        if (workingDocument != null) {
            workingDocumentCalendarGraphic = createOrUpdateCalendarGraphicFromWorkingDocument(workingDocument, workingDocumentCalendarGraphic);
            workingDocumentCalendarGraphic.setCalendarClickHandler(this::onCalendarClick);
            showCalendarIfBothLogicAndViewAreReady();
        }
    }

    private void onCalendarClick(CalendarClickEvent event) {
        boolean editMode = event.getMouseEvent().isControlDown(); // temporary criteria
        if (!editMode) {
            CalendarCell cell = event.getCalendarCell();
            long clickedCellMinuteStart = cell.getEpochDay() * 24 * 60 + cell.getDayTimeMinuteInterval().getIncludedStart();
            TimeInterval workingDocumentInterval = getWorkingDocument().getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
            long fromArrival = Math.abs(workingDocumentInterval.getIncludedStart() - clickedCellMinuteStart);
            long fromDeparture = Math.abs(workingDocumentInterval.getExcludedEnd() - clickedCellMinuteStart);
            updateArrivalOrDepartureDateTime(event, fromArrival < fromDeparture);
            computeAndDisplayWorkingTotalPrice();
        } else {
            CalendarTimeline calendarTimeline = event.getCalendarTimeline();
            Option option = getCalendarTimelineOption(calendarTimeline);
            if (option != null) {
                String dayTimeRange = calendarTimeline.getDayTimeRange().getText();
                TextInputDialog dialog = new TextInputDialog(dayTimeRange);
                dialog.initOwner(optionsViewModel.getContentNode().getScene().getWindow());
                dialog.setTitle("Text Input Dialog");
                dialog.setHeaderText(calendarTimeline.displayNameProperty().getValue());
                dialog.setContentText("Day time range:");
/*
                Property<Boolean> okDisabledProperty = dialog.getDialogPane().lookupButton(ButtonType.OK).disabledProperty();
                Properties.runNowAndOnPropertiesChange(p -> okDisabledProperty.setValue(dialog.getEditor().getText().equals(dialog.getDefaultValue())), dialog.getEditor().textProperty());
*/
                dialog.show();
                dialog.setOnCloseRequest(dialogEvent -> {
                    String newDayTimeRange = dialog.getResult();
                    if (newDayTimeRange == null /* Cancel button */ || newDayTimeRange.equals(dayTimeRange) /* Ok button but no change */)
                        return; // No change => we return immediately and this will close the dialog
                    // The user made a change, we don't close the dialog yet, we keep it open until the change is recorded in the database
                    dialogEvent.consume(); // This will prevent the dialog to close
                    // Creating an update store
                    UpdateStore store = UpdateStore.create(getDataSourceModel());
                    // Creating an instance of Option entity
                    Option updatingOption = store.getOrCreateEntity(option.getId());
                    // Updating the option time range
                    updatingOption.setTimeRange(dayTimeRange);
                    // Asking the update record this change in the database
                    store.executeUpdate().setHandler(asyncResult -> {
                        dialog.setResult(null); // Resetting result back to null for next pass
                        if (asyncResult.failed())
                            showExceptionAlert(asyncResult.cause(), dialog.getOwner());
                        else {
                            dialog.close();
                            // Updating the UI
                            option.setTimeRange(newDayTimeRange);
                            createAndShowCalendarIfBothLogicAndViewAreReady();
                        }
                    });
                });
            }
        }
    }

    private static Option getCalendarTimelineOption(CalendarTimeline calendarTimeline) {
        Object source = calendarTimeline.getSource();
        if (source instanceof Option)
            return (Option) source;
        if (source instanceof WorkingDocumentLine)
            return ((WorkingDocumentLine) source).getOption();
        return null;
    }

    private static void showExceptionAlert(Throwable e, Window owner) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText(e.getMessage());

// Create expandable Exception.

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace())
            sb.append(element).append('\n');
        String exceptionText = sb.toString();

        Label label = new Label("The exception stacktrace was:");

        Label textArea = new Label(exceptionText);
/*
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
*/

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.show();
    }

    private void updateArrivalOrDepartureDateTime(CalendarClickEvent event, boolean arrival) {
        CalendarCell cell = event.getCalendarCell();
        TimeInterval dayTimeMinuteInterval = cell.getDayTimeMinuteInterval();
        long clickedDayMinute = cell.getEpochDay() * 24 * 60;
        TimeInterval currentWorkingDocumentInterval = getWorkingDocument().getDateTimeRange().getInterval().changeTimeUnit(TimeUnit.MINUTES);
        TimeInterval newRequestedDocumentInterval =
                arrival ? new TimeInterval(clickedDayMinute + dayTimeMinuteInterval.getIncludedStart(), currentWorkingDocumentInterval.getExcludedEnd(), TimeUnit.MINUTES)
                :         new TimeInterval(currentWorkingDocumentInterval.getIncludedStart(), clickedDayMinute + cell.getDayTimeMinuteInterval().getExcludedEnd(), TimeUnit.MINUTES);
        DateTimeRange newWorkingDocumentDateTimeRange = getEventMaxDateTimeRange().intersect(newRequestedDocumentInterval.toSeries());
        setWorkingDocument(createNewDateTimeRangeWorkingDocument(newWorkingDocumentDateTimeRange));
        createAndShowCalendarIfBothLogicAndViewAreReady();
    }

    private void computeAndDisplayWorkingTotalPrice() {
        optionsViewModel.getPriceText().setText(PriceFormatter.SINGLETON.formatWithCurrency(DocumentPricing.computeDocumentPrice(getWorkingDocument()), getEvent()));
    }

    private void showCalendarIfBothLogicAndViewAreReady() {
        if (workingDocumentCalendarGraphic != null && optionsViewModel != null) {
            optionsViewModel.getCalendarPanel().setCenter(new VBox(optionsViewModel.getPriceText(), workingDocumentCalendarGraphic.getNode()));
            computeAndDisplayWorkingTotalPrice();
        }
    }

    @Override
    protected void bindPresentationModelWithLogic(OptionsPresentationModel pm) {
        // Doing now but also on event change
        Properties.runNowAndOnPropertiesChange(property -> bindPresentationModelWithLogicNow(), pm.eventIdProperty());
    }

    private void bindPresentationModelWithLogicNow() {
        onFeesGroup().setHandler(async -> {
            if (async.failed())
                Platform.log(async.cause());
            else
                createAndShowCalendarIfBothLogicAndViewAreReady();
        });
    }
}
