package webfx.framework.client.activity.impl.combinations.domainapplication.impl;

import webfx.framework.client.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class DomainApplicationContextFinal extends DomainApplicationContextBase<DomainApplicationContextFinal> {

    public DomainApplicationContextFinal(String[] mainArgs) {
        super(mainArgs, DomainApplicationContextFinal::new);
    }

    public DomainApplicationContextFinal(ActivityContext parentContext) {
        super(mainArgs, DomainApplicationContextFinal::new);
    }
}
