package mongoose.client.businesslogic.preselection;

import mongoose.client.aggregates.event.EventAggregate;
import mongoose.client.businesslogic.workingdocument.WorkingDocument;
import mongoose.client.businesslogic.workingdocument.WorkingDocumentLine;
import mongoose.shared.entities.formatters.EventPriceFormatter;
import mongoose.shared.entities.Event;
import mongoose.shared.entities.Label;
import mongoose.shared.entities.Option;
import mongoose.shared.time.DateTimeRange;
import mongoose.shared.util.Labels;
import webfx.framework.shared.orm.entity.EntityId;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.collection.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public final class OptionsPreselection {

    private final EventAggregate eventAggregate;
    private final Label label;
    private final String i18nKey; // alternative i18n key if label is null
    private final List<OptionPreselection> optionPreselections;

    public OptionsPreselection(EventAggregate eventAggregate, Label label, String i18nKey, List<OptionPreselection> optionPreselections) {
        this.eventAggregate = eventAggregate;
        this.label = label;
        this.i18nKey = i18nKey;
        this.optionPreselections = optionPreselections;
    }

    public Label getLabel() {
        return label;
    }

    public List<OptionPreselection> getOptionPreselections() {
        return optionPreselections;
    }

    private WorkingDocument workingDocument;
    public WorkingDocument initializeNewWorkingDocument() {
        if (workingDocument == null)
            workingDocument = createNewWorkingDocument(null);
        else
            workingDocument.syncPersonDetails(eventAggregate.getPersonAggregate().getPreselectionProfilePerson());
        return workingDocument;
    }

    public WorkingDocument getWorkingDocument() {
        if (workingDocument == null)
            initializeNewWorkingDocument();
        return workingDocument;
    }

    public WorkingDocument createNewWorkingDocument(DateTimeRange workingDocumentDateTimeRange) {
        return new WorkingDocument(eventAggregate, Collections.map(optionPreselections, optionPreselection -> new WorkingDocumentLine(optionPreselection, workingDocumentDateTimeRange)));
    }

    public WorkingDocumentLine getAccommodationLine() {
        return getWorkingDocument().getAccommodationLine();
    }

    public boolean hasAccommodation() {
        return getAccommodationLine() != null;
    }

    public boolean hasAccommodationExcludingSharing() {
        WorkingDocumentLine accommodationLine = getAccommodationLine();
        return accommodationLine != null && Booleans.isFalse(accommodationLine.getItem().isShare_mate());
    }

    public Option getAccommodationOption() {
        WorkingDocumentLine accommodationLine = getAccommodationLine();
        return accommodationLine == null ? null : accommodationLine.getOption();
    }

    public boolean isForceSoldout() {
        Option accommodationOption = getAccommodationOption();
        return accommodationOption != null && Booleans.isTrue(accommodationOption.isForceSoldout());
    }

    public int computePrice() {
        return initializeNewWorkingDocument().applyBusinessRules().computePrice();
    }

    public String getDisplayName() {
        return Labels.instantTranslateLabel(label, i18nKey);
    }

    public String getDisplayName(Object language) {
        return Labels.instantTranslateLabel(label, language, i18nKey);
    }

    public Object getDisplayPrice() {
        return EventPriceFormatter.formatWithCurrency(computePrice(), eventAggregate.getEvent());
    }

    public Object getDisplayAvailability(EventAggregate eventAggregate) {
        QueryResult rs = eventAggregate.getEventAvailabilities();
        if (rs != null) {
            WorkingDocumentLine accommodationLine = getWorkingDocument().getAccommodationLine();
            if (accommodationLine != null) {
                Object sitePk = accommodationLine.getSite().getPrimaryKey();
                Object itemPk = accommodationLine.getItem().getPrimaryKey();
                for (int rowIndex = 0; rowIndex < rs.getRowCount(); rowIndex++) {
                    if (rs.getValue(rowIndex, 1).equals(sitePk) && rs.getValue(rowIndex, 2).equals(itemPk))
                        return rs.getValue(rowIndex, 4);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getDisplayName("en");
    }

    // Events selected options preselections storage

    private final static Map<EntityId, OptionsPreselection> eventsSelectedOptionsPreselections = new HashMap<>();

    public static void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection, EventAggregate eventAggregate) {
        setSelectedOptionsPreselection(selectedOptionsPreselection, eventAggregate.getEvent());
    }

    public static void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection, Event event) {
        setSelectedOptionsPreselection(selectedOptionsPreselection, event.getId());
    }

    public static void setSelectedOptionsPreselection(OptionsPreselection selectedOptionsPreselection, EntityId eventId) {
        eventsSelectedOptionsPreselections.put(eventId, selectedOptionsPreselection);
    }

    public static OptionsPreselection getSelectedOptionsPreselection(EventAggregate eventAggregate) {
        return getSelectedOptionsPreselection(eventAggregate.getEvent());
    }

    public static OptionsPreselection getSelectedOptionsPreselection(Event event) {
        return getSelectedOptionsPreselection(event.getId());
    }

    public static OptionsPreselection getSelectedOptionsPreselection(EntityId eventId) {
        return eventsSelectedOptionsPreselections.get(eventId);
    }

}
