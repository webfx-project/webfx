package naga.framework.activity.client;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class UiApplicationContextFinal extends UiApplicationContextExtendable<UiApplicationContextFinal> {

    public UiApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
