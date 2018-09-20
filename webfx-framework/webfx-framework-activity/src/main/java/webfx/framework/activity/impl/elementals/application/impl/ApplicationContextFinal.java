package webfx.framework.activity.impl.elementals.application.impl;

import webfx.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContextFinal extends ApplicationContextBase<ApplicationContextFinal> {

    public ApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
