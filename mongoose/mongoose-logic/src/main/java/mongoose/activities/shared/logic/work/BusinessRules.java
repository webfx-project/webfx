package mongoose.activities.shared.logic.work;

import mongoose.activities.shared.logic.time.DaysArrayBuilder;
import mongoose.entities.Option;

/**
 * @author Bruno Salmon
 */
class BusinessRules {

    static void applyBusinessRules(WorkingDocument workingDocument) {
        applyBreakfastRule(workingDocument);
        applyDietRule(workingDocument);
        applyTouristTaxRule(workingDocument);
        applyTranslationRule(workingDocument);
    }

    private static void applyBreakfastRule(WorkingDocument wd) {
        if (!wd.hasAccommodation())
            wd.getWorkingDocumentLines().remove(wd.getBreakfastLine());
        else if (!wd.hasBreakfast()) {
            Option breakfastOption = wd.getEventService().getBreakfastOption();
            if (breakfastOption != null)
                wd.setBreakfastLine(wd.addNewDependantLine(breakfastOption, wd.getAccommodationLine(), 1));
        }
    }

    private static void applyDietRule(WorkingDocument wd) {
        if (!wd.hasLunch() && !wd.hasSupper()) {
            if (wd.hasDiet())
                wd.getWorkingDocumentLines().remove(wd.getDietLine());
        } else {
            WorkingDocumentLine dietLine = wd.getDietLine();
            if (dietLine == null) {
                Option dietOption = wd.getEventService().getDefaultDietOption();
                if (dietOption == null)
                    return;
                dietLine = new WorkingDocumentLine(dietOption, wd);
                wd.getWorkingDocumentLines().add(dietLine);
            }
            DaysArrayBuilder dab = new DaysArrayBuilder();
            if (wd.hasLunch())
                dab.addSeries(wd.getLunchLine().getDaysArray().toSeries(), null);
            if (wd.hasSupper())
                dab.addSeries(wd.getSupperLine().getDaysArray().toSeries(), null);
            dietLine.setDaysArray(dab.build());
        }
    }

    private static void applyTouristTaxRule(WorkingDocument wd) {
        if (!wd.hasAccommodation())
            wd.getWorkingDocumentLines().remove(wd.getTouristTaxLine());
        else if (!wd.hasTouristTax()) {
            Option touristTaxOption = wd.getEventService().findFirstOption(o -> o.isTouristTax() && (o.getParent() == null || wd.getAccommodationLine() != null && o.getParent().getItem() == wd.getAccommodationLine().getItem()));
            if (touristTaxOption != null)
                wd.setTouristTaxLine(wd.addNewDependantLine(touristTaxOption, wd.getAccommodationLine(), 0));
        }
    }

    private static void applyTranslationRule(WorkingDocument wd) {
        if (!wd.hasTeaching())
            wd.getWorkingDocumentLines().remove(wd.getTranslationLine());
        else if (wd.hasTranslation())
            wd.applySameAttendances(wd.getTranslationLine(), wd.getTeachingLine(), 0);
    }


}
