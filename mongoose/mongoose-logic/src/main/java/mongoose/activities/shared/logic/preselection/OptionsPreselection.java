package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.time.DateTimeRange;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.services.EventService;
import mongoose.services.PersonService;
import mongoose.util.Labels;
import naga.commons.util.Booleans;
import naga.commons.util.Numbers;
import naga.commons.util.collection.Collections;
import naga.framework.ui.i18n.I18n;
import naga.platform.services.query.QueryResultSet;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselection {

    private final EventService eventService;
    private final Label label;
    private final List<OptionPreselection> optionPreselections;

    public OptionsPreselection(EventService eventService, Label label, List<OptionPreselection> optionPreselections) {
        this.eventService = eventService;
        this.label = label;
        this.optionPreselections = optionPreselections;
    }

    public Label getLabel() {
        return label;
    }

    private WorkingDocument workingDocument;
    public WorkingDocument initializeNewWorkingDocument() {
        if (workingDocument == null)
            workingDocument = createNewWorkingDocument(null);
        else
            workingDocument.syncPersonDetails(PersonService.get(eventService.getEventDataSourceModel()).getPreselectionProfilePerson());
        return workingDocument;
    }

    public WorkingDocument getWorkingDocument() {
        if (workingDocument == null)
            initializeNewWorkingDocument();
        return workingDocument;
    }

    public WorkingDocument createNewWorkingDocument(DateTimeRange workingDocumentDateTimeRange) {
        return new WorkingDocument(eventService, Collections.convert(optionPreselections, optionPreselection -> new WorkingDocumentLine(optionPreselection, workingDocumentDateTimeRange)));
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
        return DocumentPricing.computeDocumentPrice(initializeNewWorkingDocument().applyBusinessRules());
    }

    public String getDisplayName(I18n i18n) {
        return Labels.instantTranslateLabel(label, i18n, "NoAccommodation");
    }

    public String getDisplayName(Object language) {
        return Labels.instantTranslateLabel(label, language, "NoAccommodation");
    }

    public Object getDisplayPrice() {
        Object price = PriceFormatter.SINGLETON.format(computePrice(), false);
        // Temporary hard coded
        boolean isKMCF = Numbers.toInteger(eventService.getEvent().getOrganizationId().getPrimaryKey()) == 2;
        return isKMCF ? price + " €" : "£" + price;
    }

    public Object getDisplayAvailability(EventService eventService) {
        QueryResultSet rs = eventService.getEventAvailabilities();
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
}
