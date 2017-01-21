package naga.framework.activity.view;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
final class ViewApplicationContextFinal extends ViewApplicationContextBase<ViewApplicationContextFinal> {

    ViewApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
