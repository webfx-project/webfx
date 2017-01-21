package naga.platform.activity.client;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
final class ApplicationContextFinal extends ApplicationContextBase<ApplicationContextFinal> {

    ApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
