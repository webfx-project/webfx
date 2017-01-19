package naga.framework.activity.client;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class UiDomainApplicationContextFinal extends UiDomainApplicationContextExtendable<UiDomainApplicationContextFinal> {

    public UiDomainApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
