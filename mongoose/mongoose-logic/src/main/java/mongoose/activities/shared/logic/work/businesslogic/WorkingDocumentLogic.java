package mongoose.activities.shared.logic.work.businesslogic;

import mongoose.activities.shared.logic.work.WorkingDocument;
import mongoose.activities.shared.logic.work.businesslogic.rules.*;
import naga.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentLogic {

    private final static BusinessRule[] BUSINESS_RULES = {
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
