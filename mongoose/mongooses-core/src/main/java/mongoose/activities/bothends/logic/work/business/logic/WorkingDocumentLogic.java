package mongoose.activities.bothends.logic.work.business.logic;

import mongoose.activities.bothends.logic.work.WorkingDocument;
import mongoose.activities.bothends.logic.work.business.rules.*;
import webfx.util.Arrays;

/**
 * @author Bruno Salmon
 */
public class WorkingDocumentLogic {

    private final static BusinessRule[] BUSINESS_RULES = {
            new BreakfastRule(),
            new DietRule(),
            new TouristTaxRule(),
            new TranslationRule(),
            new HotelShuttleRule()
    };

    public static void applyBusinessRules(WorkingDocument workingDocument) {
        Arrays.forEach(BUSINESS_RULES, rule -> rule.apply(workingDocument));
    }
}
