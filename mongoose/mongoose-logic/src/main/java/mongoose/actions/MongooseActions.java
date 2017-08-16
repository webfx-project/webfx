package mongoose.actions;

import naga.framework.ui.action.Action;
import naga.framework.ui.action.ActionRegistry;

/**
 * @author Bruno Salmon
 */
public class MongooseActions {

    public final static Object ADD_OPTION_ACTION_KEY = "AddOption";
    public final static Object VISIT_TERMS_AND_CONDITIONS_ACTION = "TermsAndConditions>>";
    public final static Object VISIT_PROGRAM_ACTION = "Program>>";

    public static void registerActions() {
        ActionRegistry.registerAction(ActionRegistry.ADD_ACTION_KEY, Action.create("Add", MongooseIcons.addIcon16, null));
        ActionRegistry.registerAction(ActionRegistry.REMOVE_ACTION_KEY, Action.create("Remove", MongooseIcons.removeIcon16, null));
        ActionRegistry.registerAction(ADD_OPTION_ACTION_KEY, Action.create(ADD_OPTION_ACTION_KEY, MongooseIcons.addIcon16, null));
        ActionRegistry.registerAction(VISIT_TERMS_AND_CONDITIONS_ACTION, Action.create(VISIT_TERMS_AND_CONDITIONS_ACTION, MongooseIcons.certificateIcon16, null));
        ActionRegistry.registerAction(VISIT_PROGRAM_ACTION, Action.create(VISIT_PROGRAM_ACTION, MongooseIcons.calendarIcon16, null));
    }

    public static Action newAddOptionAction(Runnable handler) {
        return ActionRegistry.newAction(ADD_OPTION_ACTION_KEY, handler);
    }

    public static Action newVisitTermsAndConditionsAction(Runnable handler) {
        return ActionRegistry.newAction(VISIT_TERMS_AND_CONDITIONS_ACTION, handler);
    }

    public static Action newVisitProgramAction(Runnable handler) {
        return ActionRegistry.newAction(VISIT_PROGRAM_ACTION, handler);
    }
}
