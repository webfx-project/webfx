package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.price.DocumentPricing;
import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.domainmodel.format.PriceFormatter;
import mongoose.entities.Label;
import mongoose.services.EventService;
import naga.commons.util.collection.Collections;
import naga.framework.ui.i18n.I18n;

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

    public WorkingDocument initializeNewWorkingDocument(EventService eventService) {
        return new WorkingDocument(eventService, null, Collections.convert(optionPreselections, WorkingDocumentLine::new));
    }

    public int computePrice(EventService eventService) {
        return DocumentPricing.computeDocumentPrice(initializeNewWorkingDocument(eventService).applyBusinessRules());
    }

    public String getDisplayName(I18n i18n) {
        return i18n.instantTranslate(getDisplayName(i18n.getLanguage()));
    }

    public String getDisplayName(Object language) {
        return label == null ? "NoAccommodation" : label.getStringFieldValue(language);
    }

    public Object getDisplayPrice(EventService eventService) {
        return PriceFormatter.SINGLETON.format(computePrice(eventService), false) + " €";
    }

    @Override
    public String toString() {
        return getDisplayName("en");
    }
}
