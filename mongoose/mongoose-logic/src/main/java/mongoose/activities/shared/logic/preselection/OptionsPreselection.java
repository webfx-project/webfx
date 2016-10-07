package mongoose.activities.shared.logic.preselection;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.WorkingDocumentLine;
import mongoose.entities.Label;
import mongoose.services.EventService;
import naga.commons.util.collection.Collections;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class OptionsPreselection {

    private final Label label;
    private final List<OptionPreselection> optionPreselections;

    public OptionsPreselection(Label label, List<OptionPreselection> optionPreselections) {
        this.label = label;
        this.optionPreselections = optionPreselections;
    }

    public Label getLabel() {
        return label;
    }

    public WorkingDocument initializeNewWorkingDocument(EventService eventService) {
        return new WorkingDocument(eventService, null, Collections.convert(optionPreselections, WorkingDocumentLine::new));
    }

    @Override
    public String toString() {
        return label == null ? "NoAccommodation" : label.getStringFieldValue("en");
    }
}
