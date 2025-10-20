package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class UserInteraction {

    private static int USER_INTERACTIONS_COUNT = 0;
    private static boolean IS_USER_INTERACTING;
    private static final List<Runnable> NEXT_USER_INTERACTION_RUNNABLES = new ArrayList<>();
    private static boolean NEXT_USER_INTERACTION_RUNNABLE_REQUIRES_TOUCH_EVENT_DEFAULT;
    public static void setUserInteracting(boolean on) {
        IS_USER_INTERACTING = on;
        if (on) {
            USER_INTERACTIONS_COUNT++;
            if (!NEXT_USER_INTERACTION_RUNNABLES.isEmpty()) {
                for (Iterator<Runnable> it = NEXT_USER_INTERACTION_RUNNABLES.iterator(); it.hasNext(); ) {
                    it.next().run();
                    it.remove();
                }
                NEXT_USER_INTERACTION_RUNNABLE_REQUIRES_TOUCH_EVENT_DEFAULT = false;
            }
        }
    }

    public static boolean isUserInteracting() {
        return IS_USER_INTERACTING;
    }

    public static boolean hasUserNotInteractedYet() {
        return USER_INTERACTIONS_COUNT == 0;
    }

    public static boolean nextUserRunnableRequiresTouchEventDefault() {
        return NEXT_USER_INTERACTION_RUNNABLE_REQUIRES_TOUCH_EVENT_DEFAULT;
    }

    public static void runOnNextUserInteraction(Runnable runnable) {
        runOnNextUserInteraction(runnable, false);
    }

    public static void runOnNextUserInteraction(Runnable runnable, boolean requireTouchEventDefault) {
        NEXT_USER_INTERACTION_RUNNABLES.add(runnable);
        if (requireTouchEventDefault) {
            NEXT_USER_INTERACTION_RUNNABLE_REQUIRES_TOUCH_EVENT_DEFAULT = true;
        }
    }

}
