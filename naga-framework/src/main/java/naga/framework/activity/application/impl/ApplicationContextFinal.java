package naga.framework.activity.application.impl;

import naga.framework.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContextFinal extends ApplicationContextBase<ApplicationContextFinal> {

    public ApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
