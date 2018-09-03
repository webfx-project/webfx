package mongooses.core.activities.sharedends.logic.work.business.logic;

import mongooses.core.activities.sharedends.logic.work.WorkingDocument;
import mongooses.core.activities.sharedends.logic.work.business.rules.*;
import webfx.platforms.core.util.Arrays;

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
