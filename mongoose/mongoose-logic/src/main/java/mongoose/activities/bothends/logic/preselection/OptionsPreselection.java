package mongoose.activities.bothends.logic.preselection;

import mongoose.activities.bothends.logic.time.DateTimeRange;
import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.formatters.PriceFormatter;
import mongoose.entities.Label;
import mongoose.entities.Option;
import mongoose.aggregates.EventAggregate;
import mongoose.util.Labels;
import naga.util.Booleans;
import naga.util.collection.Collections;
import naga.platform.services.query.QueryResult;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselection {

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
        return PriceFormatter.formatWithCurrency(computePrice(), eventAggregate.getEvent());
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
}
