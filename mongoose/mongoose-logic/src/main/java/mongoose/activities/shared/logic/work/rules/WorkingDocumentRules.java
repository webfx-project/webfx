package mongoose.activities.shared.logic.work.rules;

import mongoose.activities.shared.logic.work.WorkingDocument;
import naga.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentRules {

    private final static WorkingDocumentRule[] BUSINESS_RULES = {
            new BreakfastRule(),
            new DietRule(),
            new TouristTaxRule(),
            new TranslationRule(),
            new HotelShuttleRules()
    };

    public static void applyBusinessRules(WorkingDocument workingDocument) {
        Arrays.forEach(BUSINESS_RULES, rule -> rule.apply(workingDocument));
    }

}
