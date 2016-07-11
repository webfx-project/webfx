package naga.framework.ui.activity;

/**
 * @author Bruno Salmon
 */
public interface UiDomainApplicationContext<C extends UiDomainApplicationContext<C>> extends UiApplicationContext<C>, UiDomainActivityContext<C> {

    static UiDomainActivityContext create(String[] mainArgs) {
        return new UiDomainApplicationContextImpl(mainArgs, UiDomainActivityContext::create);
    }
}
