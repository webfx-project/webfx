package naga.platform.activity.application.impl;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContextFinal extends ApplicationContextBase<ApplicationContextFinal> {

    public ApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
