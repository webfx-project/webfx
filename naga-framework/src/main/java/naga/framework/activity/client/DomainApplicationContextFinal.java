package naga.framework.activity.client;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
public final class DomainApplicationContextFinal extends DomainApplicationContextExtendable<DomainApplicationContextFinal> {

    public DomainApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
