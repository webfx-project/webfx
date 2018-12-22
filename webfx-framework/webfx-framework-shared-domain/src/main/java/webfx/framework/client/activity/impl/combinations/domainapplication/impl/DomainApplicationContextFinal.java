package webfx.framework.client.activity.impl.combinations.domainapplication.impl;

import webfx.framework.client.activity.ActivityContext;

/**
 * @author Bruno Salmon
 */
public final class DomainApplicationContextFinal extends DomainApplicationContextBase<DomainApplicationContextFinal> {

    public DomainApplicationContextFinal() {
        super(DomainApplicationContextFinal::new);
    }

    private DomainApplicationContextFinal(ActivityContext parentContext) {
        super(DomainApplicationContextFinal::new);
    }
}
