package mongoose.actions;

import naga.framework.ui.action.Action;
import naga.framework.ui.action.ActionRegistry;

/**
 * @author Bruno Salmon
 */
public class MongooseActions {

    public final static Object VISIT_BOOK_ACTION_KEY = "Book>>";
    public final static Object VISIT_FEES_ACTION_KEY = "Fees>>";
    public final static Object VISIT_TERMS_AND_CONDITIONS_ACTION_KEY = "TermsAndConditions>>";
    public final static Object VISIT_PROGRAM_ACTION_KEY = "Program>>";
    public final static Object ADD_OPTION_ACTION_KEY = "AddOption";

    public static void registerActions() {
        ActionRegistry.registerAction(ActionRegistry.ADD_ACTION_KEY, Action.create("Add", MongooseIcons.addIcon16JsonUrl, null));
        ActionRegistry.registerAction(ActionRegistry.REMOVE_ACTION_KEY, Action.create("Remove", MongooseIcons.removeIcon16JsonUrl, null));
        ActionRegistry.registerAction(VISIT_FEES_ACTION_KEY, Action.create(VISIT_FEES_ACTION_KEY, MongooseIcons.priceTagMonoSvg16JsonUrl, null));
        ActionRegistry.registerAction(VISIT_TERMS_AND_CONDITIONS_ACTION_KEY, Action.create(VISIT_TERMS_AND_CONDITIONS_ACTION_KEY, MongooseIcons.certificateMonoSvg16JsonUrl, null));
        ActionRegistry.registerAction(VISIT_PROGRAM_ACTION_KEY, Action.create(VISIT_PROGRAM_ACTION_KEY, MongooseIcons.calendarMonoSvg16JsonUrl, null));
        ActionRegistry.registerAction(ADD_OPTION_ACTION_KEY, Action.create(ADD_OPTION_ACTION_KEY, MongooseIcons.addIcon16JsonUrl, null));
    }

    public static Action newVisitTermsAndConditionsAction(Runnable handler) {
        return ActionRegistry.newAction(VISIT_TERMS_AND_CONDITIONS_ACTION_KEY, handler);
    }

    public static Action newVisitProgramAction(Runnable handler) {
        return ActionRegistry.newAction(VISIT_PROGRAM_ACTION_KEY, handler);
    }

    public static Action newVisitFeesAction(Runnable handler) {
        return ActionRegistry.newAction(VISIT_FEES_ACTION_KEY, handler);
    }

    public static Action newVisitBookAction(Runnable handler) {
        return ActionRegistry.newAction(VISIT_BOOK_ACTION_KEY, handler);
    }

    public static Action newAddOptionAction(Runnable handler) {
        return ActionRegistry.newAction(ADD_OPTION_ACTION_KEY, handler);
    }
}
