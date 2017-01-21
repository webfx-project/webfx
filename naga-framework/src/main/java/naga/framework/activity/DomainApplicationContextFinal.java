package naga.framework.activity;

import naga.platform.activity.ActivityContextFactory;

/**
 * @author Bruno Salmon
 */
final class DomainApplicationContextFinal extends DomainApplicationContextBase<DomainApplicationContextFinal> {

    DomainApplicationContextFinal(String[] mainArgs, ActivityContextFactory contextFactory) {
        super(mainArgs, contextFactory);
    }
}
