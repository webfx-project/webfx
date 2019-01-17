package mongoose.client.businesslogic.workingdocument;

import mongoose.client.businessdata.workingdocument.WorkingDocument;
import mongoose.client.businesslogic.rules.*;
import webfx.platform.shared.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class WorkingDocumentLogic {

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
