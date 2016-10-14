package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.services.EventService;
import mongoose.services.PersonService;
import mongoose.util.Labels;
import naga.commons.util.Booleans;
import naga.commons.util.collection.Collections;
import naga.framework.ui.i18n.I18n;
import naga.platform.services.query.QueryResultSet;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselection {

    private final Label label;
    private final List<OptionPreselection> optionPreselections;

    OptionsPreselection(Label label, List<OptionPreselection> optionPreselections) {
        this.label = label;
        this.optionPreselections = optionPreselections;
    }

    public Label getLabel() {
        return label;
    }

    private WorkingDocument workingDocument;
    public WorkingDocument initializeNewWorkingDocument(EventService eventService) {
        if (workingDocument == null)
            workingDocument = new WorkingDocument(eventService, Collections.convert(optionPreselections, WorkingDocumentLine::new));
        else
            workingDocument.syncPersonDetails(PersonService.get(eventService.getEventDataSourceModel()).getPreselectionProfilePerson());
        return workingDocument;
    }

    public WorkingDocument getWorkingDocument() {
        return workingDocument;
    }

    public WorkingDocumentLine getAccommodationLine() {
        return workingDocument.getAccommodationLine();
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

    public int computePrice(EventService eventService) {
        return DocumentPricing.computeDocumentPrice(initializeNewWorkingDocument(eventService).applyBusinessRules());
    }

    public String getDisplayName(I18n i18n) {
        return Labels.instantTranslateLabel(label, i18n, "NoAccommodation");
    }

    public String getDisplayName(Object language) {
        return Labels.instantTranslateLabel(label, language, "NoAccommodation");
    }

    public Object getDisplayPrice(EventService eventService) {
        return PriceFormatter.SINGLETON.format(computePrice(eventService), false) + " €";
    }

    public Object getDisplayAvailability(EventService eventService) {
        QueryResultSet rs = eventService.getEventAvailabilities();
        if (rs != null) {
            WorkingDocumentLine accommodationLine = workingDocument.getAccommodationLine();
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
