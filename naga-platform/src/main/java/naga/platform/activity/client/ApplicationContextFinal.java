package naga.platform.activity.client;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class ApplicationContextFinal extends ApplicationContextExtendable<ApplicationContextFinal> {

    public ApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
